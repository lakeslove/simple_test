package com.lxtest.springbootdemo;

import com.lxtest.springbootdemo.Service.ServiceTest1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import javax.management.monitor.Monitor;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import org.springframework.boot.SpringApplication;


public class Test {
  private static ServiceTest1 test1 = new ServiceTest1();
  private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "2");

  protected ServiceTest1 initialValue() {
    return test1;
  }

  public static void main(String[] args) {
    System.out.println(division2(2));
    System.out.println(move2(2));



//    int value = -1;
////    System.out.println(Integer.toBinaryString(-1));
//    System.out.println(Integer.toBinaryString(Integer.MIN_VALUE<<1));
//    System.out.println(Integer.toBinaryString(-1>>>1<<1));
//    oneCount(value);
  }

  private static int division2(int i){
    return i/2;
  }

  private static int move2(int i){
    return i>>1;
  }

  private static void oneCount(int value) {
    int a = value;
    if(a<0){
      a = a - Integer.MIN_VALUE;
    }
    int sum = 0;
    while (a > 0) {
      int temp = a >> 10;
      int temp2 = temp << 1;
      int lastBit = a - temp2;
      sum = sum + lastBit;
      a = temp;
    }
    System.out.println(Integer.toBinaryString(value) + "中1的个数为：" + sum);
  }


//    System.out.println(-1 >>> 1);
//    System.out.println((-1) >>> 1);
//    System.out.println(Integer.MAX_VALUE);
//    String input = "-333-444-111-";
////    String regex = "-";
////    print(input.split(regex, 0));
////    print(input.split("--", 1));
////    print(input.split(regex, 1));
////    print(input.split(regex, 2));
////    print(input.split(regex, 3));
////    print(input.split(regex, 4));
////    print(input.split(regex, 5));
////    print(input.split(regex, 6));

//    System.out.println(Double.toHexString(0.2));


//    BlockingDeque
//    Monitor
//    ProxyGenerator

//    int[] sortedNums = new int[]{0,4};
//    System.out.println(dichotomySerchDead(9,sortedNums));
//    System.out.println(dichotomySerch(5,sortedNums));
//    System.out.println(dichotomySerch(0,sortedNums));
//    System.out.println(dichotomySerch(6,sortedNums));
//    System.out.println(dichotomySerch(7,sortedNums));
//    System.out.println(dichotomySerchWhile(3,sortedNums));
//    System.out.println(dichotomySerchWhile(5,sortedNums));
//    System.out.println(dichotomySerchWhile(0,sortedNums));


  public static Integer dichotomySerchDead(int targetNum, int[] sortedNums) {
    return dichotomySerchDead(targetNum, sortedNums, 0, sortedNums.length);
  }

  public static Integer dichotomySerchDead(int targetNum, int[] sortedNums, int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      return null;
    }
    int middleIndex = (fromIndex + toIndex) / 2;
    int middleNum = sortedNums[middleIndex];
    if (targetNum == middleNum) {
      return middleIndex;
    } else if (targetNum > middleNum) {
      return dichotomySerchDead(targetNum, sortedNums, middleIndex + 1, toIndex);
    } else {
      return dichotomySerchDead(targetNum, sortedNums, fromIndex, middleIndex);
    }
  }

  public static Integer dichotomySerch(int targetNum, int[] sortedNums) {
    return dichotomySerch(targetNum, sortedNums, 0, sortedNums.length);
  }

  public static Integer dichotomySerch(int targetNum, int[] sortedNums, int fromIndex, int toIndex) {
    if (fromIndex >= toIndex) {
      return null;
    }
    int middleIndex = (fromIndex + toIndex) / 2;
    int middleNum = sortedNums[middleIndex];
    if (targetNum == middleNum) {
      return middleIndex;
    } else if (targetNum > middleNum) {
      return dichotomySerch(targetNum, sortedNums, middleIndex + 1, toIndex);
    } else {
      return dichotomySerch(targetNum, sortedNums, fromIndex, middleIndex);
    }
  }


  public static Integer dichotomySerchWhile(int targetNum, int[] sortedNums) {
    return dichotomySerchWhile(targetNum, sortedNums, 0, sortedNums.length - 1);
  }

  public static Integer dichotomySerchWhile(int targetNum, int[] sortedNums, int fromIndex, int toIndex) {
    while (toIndex >= fromIndex) {
      int middleIndex = fromIndex + ((toIndex - fromIndex) >>> 1);
      int middleNum = sortedNums[middleIndex];
      if (targetNum == middleNum) {
        return middleIndex;
      } else if (targetNum > middleNum) {
        fromIndex = middleIndex + 1;
      } else {
        toIndex = middleIndex - 1;
      }
    }
    return null;
  }

  public static Integer dichotomySerchDeadWhile(int targetNum, int[] sortedNums) {
    int fromIndex = 0;
    int toIndex = sortedNums.length;
    while (toIndex >= fromIndex) {
      int middleIndex = fromIndex + ((toIndex - fromIndex) >>> 1);
      int middleNum = sortedNums[middleIndex];
      if (targetNum == middleNum) {
        return middleIndex;
      } else if (targetNum > middleNum) {
        fromIndex = middleIndex + 1;
      } else {
        toIndex = middleIndex;
      }
    }
    return null;
  }


  public static void print(String[] strings) {

//    for (String s : strings) {
//
//      System.out.print("-");
//    }
//    System.out.println(strings.length);
//    System.out.println(Arrays.asList(strings));
    // Arrays.binarySearch()


  }
}
