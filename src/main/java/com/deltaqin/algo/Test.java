
package com.deltaqin.algo;

public class Test{
    private volatile static int n=0;
    void methodA(){
        n++;
    }
    public static void main(String[] args){
        Test test = new Test();
        int i = 0;
        for( i=0;i<10;i++){
            new Thread(()->{
                for(int j=0;j < 11;j++){
                    test.methodA();
                }
            }).start();
        }
        System.out.println(n);
    }
}
