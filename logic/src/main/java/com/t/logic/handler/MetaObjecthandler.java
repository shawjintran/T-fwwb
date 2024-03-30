package com.t.logic.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 * 需要加 @Component注解，让spring框架进行管理，放入到ioc容器中
 */
@Slf4j
@Component
public class MetaObjecthandler implements MetaObjectHandler {
	/**
	 * 插入时自动填充
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		metaObject.setValue("createTime", LocalDateTime.now());
		metaObject.setValue("updateTime", LocalDateTime.now());
		metaObject.setValue("isDelete",0);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		metaObject.setValue("updateTime", LocalDateTime.now());
	}
}
