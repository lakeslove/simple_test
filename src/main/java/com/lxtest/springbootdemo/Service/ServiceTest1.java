package com.lxtest.springbootdemo.Service;

import org.springframework.stereotype.Service;

@Service
public class ServiceTest1 {
  public String printName(){
    System.out.println("ServiceTest1");
    return "ServiceTest1";
  }
}
