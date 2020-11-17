package com.lxtest.springbootdemo;

import com.lxtest.springbootdemo.Service.ServiceTest1;
import com.lxtest.springbootdemo.Service.ServiceTest2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootdemoApplicationTests {

	@Autowired
	private ServiceTest2 serviceTest2;

	@Test
	public void contextLoads() {
		serviceTest2.printName();
	}

}
