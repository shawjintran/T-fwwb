package com.t.logic.controller;

import com.t.logic.entity.Group;
import com.t.logic.entity.Vo.GroupUserVo;
import com.t.logic.service.GroupService;
import com.t.logic.service.GroupUserService;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group/")
@Api(tags = "用户群组的相关操作")
@CrossOrigin
public class GroupController {
  @Autowired
  GroupService groupService;
  @Autowired
  GroupUserService groupUserService;
  @GetMapping("docUsers")
  @ApiOperation("未定，查找当前文件夹共享成员")
  R selectGroupUsers(Long docId){
     List<GroupUserVo> users=groupUserService.selectDocUsers(docId);
     return R.ok().setData(users);
  }

}
