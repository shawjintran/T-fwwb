package com.t.logic.service.impl;


//import cn.caohd.seata.async.util.SeataAsyncUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.Test;
import com.t.logic.service.TestService;
import com.t.logic.mapper.TestMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【test】的数据库操作Service实现
* @createDate 2023-08-09 10:42:36
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
    implements TestService{

  @Override
  public void batchsave() {
    return;
  }
//  @Resource
//  SeataAsyncUtil seataAsyncUtil;
//  @GlobalTransactional(rollbackFor = Exception.class)
//  public void batchsave(){
//    int size = 3-1;
//    final int[] s = {111};
//    for (int i = 0; i < size; i++) {
//
//      seataAsyncUtil.async(() -> {
//        Thread.sleep(1000);
//        int insert = baseMapper.insert(new Test(s[0]++, s[0]++));
////        int insert = baseMapper.insert(new Test(33, 33));
//        if (insert==1)
//        {
//          System.out.println(Thread.currentThread()+"保存");
//          return true;
//        }
//        System.out.println(Thread.currentThread()+"回滚");
//        return false;
////				throw new Exception();
//      });
//    }
//  }
}




