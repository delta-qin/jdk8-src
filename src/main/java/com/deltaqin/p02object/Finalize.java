package com.deltaqin.p02object;

/**
 * @author deltaqin
 * @date 2021/5/10 4:10 下午
 */
public class Finalize {
    public  static Finalize fin;

    @Override
    protected void finalize() throws Throwable {
        Finalize.fin = this;
    }
}
