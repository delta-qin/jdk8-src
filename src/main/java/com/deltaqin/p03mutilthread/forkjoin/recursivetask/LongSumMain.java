package com.deltaqin.p03mutilthread.forkjoin.recursivetask;



import com.deltaqin.p03mutilthread.forkjoin.utils.Utils;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author ：杨过
 * @date ：Created in 2020/9/4 19:28
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: -Xms1024m -Xmx1024m
 **/
public class LongSumMain {
	//获取逻辑处理器数量
	static final int NCPU = Runtime.getRuntime().availableProcessors();
	/** for time conversion */
	static final long NPS = (1000L * 1000 * 1000);

	static long calcSum;

	static final boolean reportSteals = true;

	public static void main(String[] args) throws Exception {
		int[] array = Utils.buildRandomIntArray(20000000);
		System.out.println("cpu-num:" + NCPU);
		//单线程下计算数组数据总和
		long start = System.currentTimeMillis();
 		calcSum = seqSum(array);
		System.out.println("seq sum=" + calcSum);
		System.out.println("single thread sort:->"+(System.currentTimeMillis()-start));

		start = System.currentTimeMillis();
		//采用fork/join方式将数组求和任务进行拆分执行，最后合并结果
		LongSum ls = new LongSum(array, 0, array.length);
  		ForkJoinPool fjp  = new ForkJoinPool(NCPU); //使用的线程数
		ForkJoinTask<Long> task = fjp.submit(ls);
		System.out.println("forkjoin sum=" + task.get());
		System.out.println("fork join pool sort:->"+(System.currentTimeMillis()-start));

		if(task.isCompletedAbnormally()){
			System.out.println(task.getException());
		}

		fjp.shutdown();
	}


	static long seqSum(int[] array) {
		long sum = 0;
		for (int i = 0; i < array.length; ++i)
			sum += array[i];
		return sum;
	}
}
