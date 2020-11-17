package com.lxtest.springbootdemo;

import java.math.BigInteger;
import java.security.MessageDigest;

public class GradleHelper {

  public static void main(String[] args) {
    createMD5Path(args[0]);
  }

  private static void createMD5Path(String arg) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      byte[] bytes = arg.getBytes();
      messageDigest.update(bytes);
      String str = new BigInteger(1, messageDigest.digest()).toString(36);
      System.out.println(str);
    } catch (Exception e) {
      throw new RuntimeException("Could not hash input string.", e);
    }
  }


}
