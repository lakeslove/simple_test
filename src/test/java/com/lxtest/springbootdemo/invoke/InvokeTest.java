package com.lxtest.springbootdemo.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class InvokeTest {

  public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Scanner scanner = new Scanner(System.in);
    InvokeTest invokeTest = new InvokeTest();
    invokeTest.aaa();
    invokeTest.t1();
    Method method = InvokeTest.class.getMethod("t1");
    for(int i = 0;i<20;i++){
      invokeTest.t2(method);
    }
    invokeTest.t2(method);
    System.out.println(scanner.next());
  }

  private final int age = 123;

  public void aaa() {

  }

  public void t1() {
    this.aaa();
  }

  public void t2(Method m) throws InvocationTargetException, IllegalAccessException {
    m.invoke(this);
  }
}
