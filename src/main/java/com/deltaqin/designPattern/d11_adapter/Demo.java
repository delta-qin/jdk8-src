package com.deltaqin.designPattern.d11_adapter;

import java.io.*;

/**
 * @author deltaqin
 * @date 2021/3/27 1:38 下午
 */
public class Demo {
    public static void main(String[] args) throws Exception {

        File f = new File("./test.data");
        // 字节输出流
        FileOutputStream fos = new FileOutputStream(f);
        // 字符一个一个输出流
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        // 字符一行一行输出流
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write("http://www.com");
        bw.flush();
        bw.close();


        FileInputStream fis = new FileInputStream("./test.data");
        // InputStreamReader 转换，一次读取一个字符
        InputStreamReader isr = new InputStreamReader(fis);
        // 一次读取一行字符
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();
        while (line != null && !line.equals("")) {
            System.out.println(line);
            line = br.readLine();
        }
        br.close();
    }

}
