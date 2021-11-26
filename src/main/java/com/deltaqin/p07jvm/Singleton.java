package com.deltaqin.p07jvm;

public class Singleton {
  private Singleton() {}
  private static class LazyHolder {
    // 单例延迟初始化
    static final Singleton INSTANCE = new Singleton();
    static {
      System.out.println("LazyHolder.<clinit>");
    }
  }
  public static Object getInstance(boolean flag) {
    if (flag)
      // 此句不会导致链接和初始化，只是会导致加载，
      return new LazyHolder[2];
    // 到达这里，程序才会访问 LazyHolder.INSTANCE，
    // 才会触发对 LazyHolder 的初始化
    return LazyHolder.INSTANCE;
  }
  public static void main(String[] args) {
    getInstance(true); // 单例不会初始化
    System.out.println("----");
    getInstance(false); // 单例初始化
  }
}
