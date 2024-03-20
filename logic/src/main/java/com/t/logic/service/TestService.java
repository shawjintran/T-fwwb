package com.t.logic.service;

import com.t.logic.entity.Test;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sky
* @description 针对表【test】的数据库操作Service
* @createDate 2023-08-09 10:42:36
*/
public interface TestService extends IService<Test> {
  public void batchsave();
}
