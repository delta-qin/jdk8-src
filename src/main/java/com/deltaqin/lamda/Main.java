package com.deltaqin.lamda;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.*;
//
//public class Main {
//    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//
//
////        ArrayList<String> array  = new ArrayList<String>();
////        array.add("a");
////        //反射方式,获取出集合ArrayList类的class文件对象
////        Class c = array.getClass();
////        //获取ArrayList.class文件中的方法add
////
////        Method method = c.getMethod("add",Object.class);
////
////        //使用invoke运行ArrayList方法add
////        method.invoke(array, 150);
////        method.invoke(array, 1500);
////        method.invoke(array, 15000);
////        System.out.println(array);
//
//
////        String[] strs = new String[]{"a","f","c","d"};
////        String[] strs1 = new String[5];
////        System.arraycopy(strs, 0, strs1, 0 , strs.length);
////        for(String str : strs1){
////            System.out.println(str);
////        }
//
////        String s = Arrays.toString(strs1);
////        System.out.println(s);
////        System.out.println(strs1);
//
////        Person[] arr = {new Person("a",1), new Person("q", 3)
////                , new Person("c", 2)};
////        Arrays.sort(arr);
////        System.out.println(Arrays.toString(arr));
////
////        Calendar calendar = new GregorianCalendar();
////        int year = calendar.get(Calendar.YEAR); // 打印：1999
////        int month = calendar.get(Calendar.MONTH); // 打印：10
////        int day = calendar.get(Calendar.DAY_OF_MONTH); // 打印：9
////        int day2 = calendar.get(Calendar.DATE); // 打印：9
////        // 日：Calendar.DATE 和 Calendar.DAY_OF_MONTH 同义
////        int date = calendar.get(Calendar.DAY_OF_WEEK); // 打印：3
////        System.out.println(year);
////        System.out.println(month); System.out.println(day);
////        System.out.println(day2); System.out.println(date);
////
////        GregorianCalendar calendar2 = new GregorianCalendar();
////        calendar2.set(Calendar.YEAR, 2999);
////        calendar2.set(Calendar.MONTH, Calendar.FEBRUARY);// 月份数：0-11 calendar2.set(Calendar.DATE, 3);
////        calendar2.set(Calendar.HOUR_OF_DAY, 10);
////        calendar2.set(Calendar.MINUTE, 20);
////        calendar2.set(Calendar.SECOND, 23);
////        printCalendar(calendar2); // 日期计算
////
////        GregorianCalendar calendar3 = new GregorianCalendar(2999, 10, 9, 22, 10,50);
////        calendar3.add(Calendar.MONTH, -7); // 月份减 7
////        calendar3.add(Calendar.DATE, 7); // 增加 7 天
////        printCalendar(calendar3); // 日历对象和时间对象转化
////
////        Date d = calendar3.getTime();
////        GregorianCalendar calendar4 = new GregorianCalendar();
////        calendar4.setTime(new Date());
//
////        Random random = new Random();
////        System.out.println(20 + random.nextInt(10));
//
//    }
//
////    static void printCalendar(Calendar calendar) {
////
////        int year = calendar.get(Calendar.YEAR);
////        int month = calendar.get(Calendar.MONTH) + 1;
////        int day = calendar.get(Calendar.DAY_OF_MONTH);
////        int date = calendar.get(Calendar.DAY_OF_WEEK) - 1;
////        // 星期几
////        String week = "" + ((date == 0) ? "日" : date);
////        int hour = calendar.get(Calendar.HOUR);
////        int minute = calendar.get(Calendar.MINUTE);
////        int second = calendar.get(Calendar.SECOND);
////        System.out.printf("%d 年%d 月%d 日,星期%s %d:%d:%d\n", year, month, day, week, hour, minute, second);
////    }
//}

//
//public class Main{
//    public static void main(String[] args) {
//        vRunnable v = new vRunnable();
//
//        for(int i=0; i<10; i++){
//            new Thread(v).start();
//        }
//    }
//}
//
//class vRunnable implements Runnable{
//    private volatile int num = 0;
//
//    @Override
//    public void run(){
//        // 放大问题
//        try{
//            Thread.sleep(200);
//        } catch(InterruptedException e){}
//
//        System.out.println(Thread.currentThread().getName()+ ": " + getnum());
//    }
//
//    public int getnum(){
//        return num++;
//    }
//}
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class Main{
//    public static void main(String[] args) {
//        ARunnable a = new ARunnable();
//        for(int i=0; i<10; i++) {
//            new Thread(a).start();
//        }
//    }
//}
//
//class ARunnable implements Runnable{
//    private AtomicInteger num = new AtomicInteger();
//
//    public void run(){
//        try{
//            Thread.sleep(200);
//        } catch (InterruptedException e){}
//
//        System.out.println(getA());
//    }
//
//    public int  getA(){
//        return num.getAndIncrement();
//    }
//}
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//
//public class Main{
//    public static void main(String[] args){
//        tRunnable t = new tRunnable();
//
//        new Thread(t, "1号").start();
//        new Thread(t, "2号").start();
//        new Thread(t, "3号").start();
//    }
//}
//
//class tRunnable implements Runnable{
//    private int ticket = 100;
//    private Lock lock = new ReentrantLock();
//    public void run(){
//        while(ticket > 0){
//            lock.lock();
//            // 一旦sleep是不是就切换到别的线程继续执行了
//            // 所以多线程问题就产生了。因为毕竟此时主存里面的变量已经加载到缓存里面了
//
//            // 但是即使满足了内存可见性。由于不是互斥的，对于ticket--这种非原子操作，也会导致出现异常结果
//            // 因为 i-- 是会出现 int temp = i;
//            // 综上所述，多睡一会儿的作用就是为了让他有线程切换的机会
//            try{
//                try{
//                    Thread.sleep(200);
//                } catch(InterruptedException e){
//                }
//                System.out.println(Thread.currentThread().getName() + "售票剩余"+ticket--);
//            } finally {
//                // 一定写到finally里面
//                lock.unlock();
//            }
//        }
//    }
//}
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class Main{
//    public static void main(String[] args) {
//        ARunnable a = new ARunnable();
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                for(int i=0; i<10; i++){
//                    a.loopA(i);
//                }
//            }
//        }, "A").start();
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                for(int i=0; i<10; i++){
//                    a.loopB(i);
//                }
//            }
//        }, "B").start();
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                for(int i=0; i<10; i++){
//                    a.loopC(i);
//                }
//            }
//        }, "C").start();
//    }
//}
//
//class ARunnable{
//    // 当前执行的线程标志
//    private int num = 1;
//
//    private Lock lock = new ReentrantLock();
//    private Condition con1 = lock.newCondition();
//    private Condition con2 = lock.newCondition();
//    private Condition con3 = lock.newCondition();
//
//    public void loopA(int loopNum){
//        lock.lock();
//
//        try{
//            if(num !=1){
//                con1.await();
//            }
//
//            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");
//
//            // 唤醒下一个
//            num = 2;
//            con2.signal();
//        } catch(Exception e){
//
//        }finally {
//            lock.unlock();
//        }
//    }
//
//    public void loopB(int loopNum){
//        lock.lock();
//
//        try{
//            if(num !=2){
//                con2.await();
//            }
//
//            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");
//            // 唤醒下一个
//            num = 3;
//            con3.signal();
//        } catch(Exception e){
//
//        }finally {
//            lock.unlock();
//        }
//    }
//
//    public void loopC(int loopNum){
//        lock.lock();
//
//        try{
//            if(num !=3){
//                con3.await();
//            }
//
//            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");
//            // 唤醒下一个
//            num = 1;
//            con1.signal();
//        } catch(Exception e){
//
//        }finally {
//            lock.unlock();
//        }
//    }
//}
//
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
//public class Main {
//    public static void main(String[] args) {
//        final Operator operator = new Operator();
//        //创建5个读数据的线程
//        for (int i = 0; i <5 ; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        operator.read();
//                    }
//                }
//            }, "读线程"+i).start();
//        }
//        //创建5个写数据的线程
//        for (int i = 0; i <5 ; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(true){
//                        operator.write();
//                    }
//                }
//            }, "写线程" + i).start();
//        }
//    }
//}
//
//class Operator {
//    //private Lock lock = new ReentrantLock();
//    private ReentrantReadWriteLock rwl
//            = new ReentrantReadWriteLock();
//
//    /**
//     * 读操作，要添加读锁，希望多个线程同时读取，提高效率
//     */
//    public void read() {
//        //lock.lock();
//        rwl.readLock().lock();
//        try {
//            System.out.println(Thread.currentThread().getName()
//                    + "开始读取数据........");
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName()
//                    + "读取数据完毕.............");
//        } finally {
//            //lock.unlock();
//            rwl.readLock().unlock();
//        }
//    }
//
//    /**
//     * 写操作，要使用写锁，保证安全性，只有一个线程独占
//     */
//    public void write() {
//        //lock.lock();
//        rwl.writeLock().lock();
//        try {
//            System.out.println(Thread.currentThread().getName()
//                    + "开始写数据........");
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName()
//                    + "写数据完毕.............");
//        } finally {
//            //lock.unlock();
//            rwl.writeLock().unlock();
//        }
//    }
//}
//
//import org.junit.Test;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.ForkJoinTask;
//import java.util.concurrent.RecursiveTask;
//import java.util.stream.LongStream;
//
//public class Main {
//    public static void main(String[] args) {
//        Instant start=Instant.now();
//        // 需要ForkJoinPool的支持
//        ForkJoinPool pool=new ForkJoinPool();
//        // Future的实现，有返回值
//        ForkJoinTask<Long> task=new ForkJoinSumCalculate(0L, 50000000000L);
//        Long sum=pool.invoke(task);
//
//
//        System.out.println(sum);
//        Instant end=Instant.now();
//        // 耗费时间为：6204
//        System.out.println("耗费时间为："+Duration.between(start, end).toMillis());//耗费时间为：21020
//    }
//
//    //一般的方法
//    @Test
//    public void test1(){
//        Instant start=Instant.now();
//        long sum=0L;
//        for(long i=0L;i<=50000000000L;i++){
//            sum+=i;
//        }
//        System.out.println(sum);
//        Instant end=Instant.now();
//        // 耗费时间为：12740
//        System.out.println("耗费时间为："+Duration.between(start, end).toMillis());//耗费时间为：27040
//    }
//
//    //java8 新特性
//    @Test
//    public void test2(){
//        Instant start=Instant.now();
//        Long sum= LongStream.rangeClosed(0L,50000000000L).parallel().reduce(0L, Long::sum);
//        System.out.println(sum);
//        Instant end= Instant.now();
//        // 耗费时间为：3700
//        System.out.println("耗费时间为："+ Duration.between(start, end).toMillis());//耗费时间为：14281
//    }
//
//}
//
//class ForkJoinSumCalculate extends RecursiveTask<Long> {
//    private static final long serialVersionUID=-54565646543212315L;
//
//    private long start;
//    private long end;
//
//    private static final long THURSHOLD=10000L;//临界值，小于这个值就不拆了，直接运算
//
//    public ForkJoinSumCalculate(long start,long end){
//        this.start=start;
//        this.end=end;
//    }
//    @Override
//    protected Long compute() {
//        long length=end-start;
//        if(length<=THURSHOLD){
//            long sum=0L;
//            for(long i=start;i<=end;i++){
//                sum+=i;
//            }
//            return sum;
//        }else{
//            //进行拆分，同时压入线程队列
//            long middle=(start+end)/2;
//            ForkJoinSumCalculate left=new ForkJoinSumCalculate(start, middle);
//            left.fork();
//            ForkJoinSumCalculate right=new ForkJoinSumCalculate(middle+1, end);
//            right.fork();
//            return left.join()+right.join();
//        }
//    }
//
//}

//
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class Main{
//    private List<Person> list = Arrays.asList(
//            new Person("a", 12),
//            new Person("c", 16),
//            new Person("b", 14),
//            new Person("n", 18),
//            new Person("g", 10)
//    );
//
//    @Test
//    public void test1(){
//        Collections.sort(list, (p1,p2)->{
//            if (p1.getAge() == p2.getAge()){
//                return p1.getName().compareTo(p2.getName());
//            }
////            return Integer.compare(p1.getAge(),p2.getAge());
//            // 倒排
//            return -Integer.compare(p1.getAge(),p2.getAge());
//        });
//
//        list.forEach(System.out::println);
//        System.out.println("-----------");
//        list.stream().forEach(System.out::println);
//    }
//
//    class Person{
//        private String name;
//        private Integer age;
//
//        public Person(String name, Integer age) {
//            this.name = name;
//            this.age = age;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public Integer getAge() {
//            return age;
//        }
//
//        public void setAge(Integer age) {
//            this.age = age;
//        }
//
//        @Override
//        public String toString() {
//            return "Person{" +
//                    "name='" + name + '\'' +
//                    ", age=" + age +
//                    '}';
//        }
//    }
//}

import org.junit.Test;

public class Main{

    @Test
    public void test(){
        String hello = new TestLamda().strHandler("hello", s -> s.toUpperCase());
        String hello1 = new TestLamda().strHandler("hello1", s -> s.substring(2,5));
        System.out.println(hello);
        System.out.println(hello1);

    }

    @Test
    public void test1(){
        new TestLamda().LongHandler(1L,2L,(l1,l2)-> l1+l2);
        new TestLamda().LongHandler(1L,2L,(l1,l2)-> l1*l2);
    }
}

class TestLamda{
     public String strHandler(String str, LamdaInterface lamdaInterface){
         return lamdaInterface.getVal(str);
     }

     public void LongHandler(Long l1, Long l2, MyFunction<Long, Long> mf){
         System.out.println(mf.getVal(l1,l2));
     }
}