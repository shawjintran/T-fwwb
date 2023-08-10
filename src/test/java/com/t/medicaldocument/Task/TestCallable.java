package com.t.medicaldocument.Task;

import com.t.medicaldocument.entity.Test;
import com.t.medicaldocument.service.TestService;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Transactional
@Slf4j
public class TestCallable implements Callable<String> {


  TestService testService;

  Test t;
  public TestCallable( int a, int b,TestService testService) {
    this.t=new Test(a,b);
    this.testService=testService;
  }


  @Override
  public String call() throws Exception {
    boolean save = testService.save(t);
    Thread.sleep(5000);
    if (t.getA()==3)
      return null;
    if (save)
      return "true";
    return null;
  }
}
