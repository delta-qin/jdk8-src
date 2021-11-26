package com.deltaqin.p06lamda.srccode;

public class Lambda {

  public static void simple() {
    Runnable runnable = () -> {
      System.out.println("lambda is run");
      throw new RuntimeException("exceprion");
    };
    runnable.run();
  }
  public static void main(String[] args) throws Exception {
    simple();
  }
}
