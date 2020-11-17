package com.lxtest.springbootdemo.java36jiang;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class Reference {
  static class HeapObject {
    byte[] bs = new byte[1024 * 1024];
  }

  public static void main(String[] args) {
    SoftReference<HeapObject> softReference = new SoftReference<>(new HeapObject());

    List<HeapObject> list = new ArrayList<>();

    while (true) {
      if (softReference.get() != null) {
        list.add(new HeapObject());
        System.out.println("list.add");
      } else {
        System.out.println("---------软引用已被回收---------");
        break;
      }
      System.gc();
    }
  }
}
