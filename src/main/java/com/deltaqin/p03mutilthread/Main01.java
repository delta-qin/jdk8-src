package com.deltaqin.p03mutilthread;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @author ：杨过
 * @date ：Created in 2020/8/16
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
public class Main01 {

    public void method(){
        try {
            boolean flag = true;
            if(flag){
                throw new IllegalMonitorStateException();
            }
        } finally {
            System.out.println("finally");
        }
    }

    public static void main(String[] args) {
        //Main main = new Main();
        //main.method();
    }

}
