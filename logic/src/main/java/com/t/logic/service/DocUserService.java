package com.t.logic.service;

import com.t.logic.entity.DocUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.logic.entity.Vo.ShareUserVo;
import java.util.List;

/**
* @author sky
* @description 针对表【doc_user】的数据库操作Service
* @createDate 2024-02-15 21:46:13
*/
public interface DocUserService extends IService<DocUser> {

  List<Long> selectShareDoc(Long userId);
  List<ShareUserVo> selectDocUsers(Long docId);
}
