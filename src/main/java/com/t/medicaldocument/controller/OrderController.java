package com.t.medicaldocument.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.WriterException;
import com.t.medicaldocument.config.MException;
import com.t.medicaldocument.entity.Business;
import com.t.medicaldocument.entity.Order;
import com.t.medicaldocument.service.BusinessService;
import com.t.medicaldocument.service.OrderService;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.QrCodeUtil;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/order/")
@Api(tags = "用户订单的相关操作")
@CrossOrigin
public class OrderController {
	@Autowired
	BusinessService businessService;
	@Autowired
	OrderService orderService;
	@GetMapping("update/{orderId}")
	@ApiOperation("(已定)用户支付订单成功，更新订单状态")
	public R updateStatus(@PathVariable Long orderId){
		if (orderId==null)
			return R.fail().setMes("订单ID不存在");
		Order byId = orderService.getById(orderId);
		if (byId==null)
			return R.fail().setMes("订单不存在");
		UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
		wrapper.eq("order_id",orderId).set("order_status",1);
		boolean update = orderService.update(wrapper);
		if (!update)
			return R.fail().setMes("订单支付出错");
		return R.ok().setMes("订单支付成功");};

	@GetMapping("list/{page}/{size}/{userId}")
	@ApiOperation("(已定)列出用户的所有订单")
	public R listOrder(@PathVariable Long userId,
					   @PathVariable Integer page,
					   @PathVariable Integer size ){
		Page<Order> orderPage = orderService.page(new Page<Order>(page, size),
				new QueryWrapper<Order>().eq("user_Id", userId));
		HashMap<String, Object> map = new HashMap<>();
		map.put("data",orderPage.getRecords());
		map.put("total",orderPage.getTotal());
		return R.ok(map);
	};
	@PostMapping(value = "add")
	@ApiOperation(value = "(初版已定)用户添加订单",produces = "image/png")
	public void addOrder(@ApiParam(required = true)
									 Long bizId,
						 @ApiParam(required = true)
								 Long userId,
						 HttpServletResponse response) throws IOException, WriterException, MException {
		// 添加业务错误类型捕捉
		if (bizId==null||userId==null)
			throw new MException().put("desc","用户未登录").setCode(301);
		Business byId = businessService.getById(bizId);
		if (byId==null)
			throw new MException().put("desc","业务不存在").setCode(301);
		Order order = new Order().setUserId(userId)
				.setOrderPoint(byId.getBizPoint())
				.setOrderPrice(byId.getBizPrice())
				.setBizId(bizId)
				.setOrderStatus(0);
		boolean save = orderService.save(order);
		if (save)
		{
			byte[] bytes = QrCodeUtil.generateQrCodeByte(FileUtils.Server +"/order/update/" + order.getOrderId(),
					350, 350);
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
			response.getOutputStream().write(Objects.requireNonNull(bytes));
			return;
		}
		throw new MException().put("desc","订单生成失败").setCode(301);
	};
	//  2023/3/27 扫码后，更新订单状态，同时添加用户的积分
	// 2023/3/27 列出用户的订单
	// 2023/3/27 根据业务编号，添加订单后，生成二维码，订单状态为未完成，二维码直接包含更新订单状态连接
}
