package com.t.logic.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import java.sql.SQLException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public class IdInterceptor implements InnerInterceptor {

  @Override
  public void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
      RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
    InnerInterceptor.super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
  }
}
