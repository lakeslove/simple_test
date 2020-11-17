package com.lxtest.springbootdemo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTest2 {
  @Autowired
  private ServiceTest1 serviceTest1;
  public void printName(){
    System.out.println("ServiceTest2调用了"+serviceTest1.printName());
  }
}
