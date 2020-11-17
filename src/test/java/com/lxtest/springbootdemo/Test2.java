package com.lxtest.springbootdemo;

import java.util.ArrayList;
import java.util.List;

public class Test2 {
  public static void main(String[] args) {
//    generate(new ArrayList<>(),0,0,3,"");
    String value = "a";
    generate(value);
    System.out.println(value);
  }
  public static void generate(String val){
    val = val +1;
  }


  public static void generate(List<String> list,int left,int right,int n,String result){
    if(left == n && right == n){
      System.out.println(result);
      list.add(result);
      return;
    }
    if(left<n){
      generate(list,left+1,right,n,result+"(");
    }
    if(left>right && right <n){
      generate(list,left,right+1,n,result+")");
    }



  }

  private static void DFS() {
    Node node6 = new Node(6);
    Node node5 = new Node(5, null, node6);
    Node node4 = new Node(4);
    Node node3 = new Node(3, null, node5);
    Node node2 = new Node(2, null, node4);
    Node root = new Node(1, node2, node3);
    System.out.println(minDepth(root));
  }


  public static int minDepth(Node root) {
    if (root == null) {
      return 0;
    }
    int leftMinDepth = minDepth(root.leftChild);
    int rightMinDepth = minDepth(root.rightChild);
    if (leftMinDepth == 0 || rightMinDepth == 0) {
      return leftMinDepth + rightMinDepth + 1;
    } else {
      return Math.min(leftMinDepth, rightMinDepth) + 1;
    }
  }

  static class Node {
    int value;
    Node leftChild;
    Node rightChild;

    public Node(int val) {
      value = val;
    }

    public Node(int val, Node left, Node right) {
      value = val;
      leftChild = left;
      rightChild = right;
    }
  }


  private static int division2(int i) {
    return i / 2;
  }

  private static int move2(int i) {
    return i >> 1;
  }


}
