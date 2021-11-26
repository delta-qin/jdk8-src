package com.deltaqin.p02trycatch;

/**
 * return写在try 或者catch 里面或者finally的后面，
 *      执行完finally 返回return里面的值
 *
 * finally 没有return，try 或者 catch里面有return
 *      即使在finally修改了返回变量的值，return的时候也还是返回修改之前的值
 *
 * （一般不会这么写）finally 有return， try 或者 catch 有return，
 *      返回的是finally里面的值
 *
 * 都没有return，return在 finally之后
 *      返回的是上面所有执行完的值
 *
 * （一般不会这么写）在try 里面使用System.exit(0),JVM直接退出
 *      不会返回任何东西
 */
public class Demo {
    public int add(int a, int b) {

        if (a < 0 && b < 0) {
            // 1. return 在try之前是不会执行finally
            return a + b;
        }

        try {
            return a + b;
        } catch (Exception e) {
            System.out.println("catch 语句块");
        } finally {
            System.out.println("finally 语句块");
            return 0;
        }
        //return 0;

    }

    public int add1(int a, int b) {

        if (a < 0 && b < 0) {
            return a + b;
        }

        try {
            System.out.println("JVM直接退出");
            // 2. JVM直接退出，finally不会执行
            System.exit(0);
            return a + b;
        } catch (Exception e) {
            System.out.println("catch 语句块");
        } finally {
            System.out.println("finally 语句块");
            //return 0;
        }
        return 0;
    }

    public int add2(int a, int b) {

        if (a < 0 && b < 0) {
            return a + b;
        }

        try {
            // 3. try块有异常，finally会在异常return前执行
            a = 1/0;
            // 4. try块有return，finally会在return前执行
            return 1;
        } catch (Exception e) {
            System.out.println("catch 语句块");
            return a + b;
        } finally {
            System.out.println("finally 语句块");
            //return 0;
        }
    }


    public int add3(int a, int b) {

        if (a < 0 && b < 0) {
            return a + b;
        }

        try {
            //a = 1/0;
            // 4. try块有return，finally会在return前执行
            return 1;
        } catch (Exception e) {
            System.out.println("catch 语句块");
            // 3. try块有异常，finally会在异常return前执行
            return a + b;
        } finally {
            System.out.println("finally 语句块");
            return 0;
        }
    }

    public int add4() {

        int i = 1;
        try {
            // finally 里面对这个值的修改会影响返回结果
            return i;
        } catch (Exception e) {
            System.out.println("catch 语句块");
        } finally {
            System.out.println("finally 语句块");
            ++i;
        }
        // 不会执行
        return i;
    }


    public static void main(String[] args) {
        Demo demo = new Demo();
        //和是：-43
        System.out.println("和是：" + demo.add(-9, -34));

        //finally 语句块
        //和是：0
        //System.out.println("和是：" + demo.add1(9, 34));

        // catch 语句块
        //finally 语句块
        //和是：43
        System.out.println("和是：" + demo.add2(9, 34));


        System.out.println("和是：" + demo.add3(9, 34));

        System.out.println(demo.add4());
    }
}
