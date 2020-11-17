package com.lxtest.springbootdemo.thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadContextTest {
  private static ThreadLocal<String> CONTEXT = new ThreadLocal<>();
  private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
      60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512));
  private  String name = "name";
      public String getName(){
        return name;
      }


  public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class clazz = ThreadContextTest.class;
    ThreadContextTest tt = new ThreadContextTest();
    Method method = clazz.getMethod("getName");
    Object object = method.invoke(tt);

//    CONTEXT.set("main context");
//    String name = "name";
//
//    // 方式1：在用户任务中直接进行手动获取/设置上下文逻辑
//    executor.submit(new RunnableWrap(() -> System.out.println(name+"hello world: " + CONTEXT.get())));
  }
  static class RunnableWrap implements Runnable {
    private String contextValue;
    private Runnable task;

    public RunnableWrap(Runnable task) {
      this.contextValue = CONTEXT.get();
      this.task = task;
    }

    @Override
    public void run() {
      try {
        CONTEXT.set(contextValue);
        // 用户任务逻辑
        task.run();
      } finally {
        CONTEXT.remove();
      }
    }
  }

}
