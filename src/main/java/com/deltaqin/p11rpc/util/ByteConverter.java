package com.deltaqin.p11rpc.util;

/**
 * @author deltaqin
 * @date 2021/10/14 下午2:13
 */
public class ByteConverter {
    public static int bytesToIntBigEndian(byte[] body, int i) {
        return 0;
    }

    public static byte[] longToBytes(long n) {
        byte[] buf = new byte[8];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    public static byte[] shortToBytes(short n) {
        byte[] buf = new byte[2];
        for (int i = 0; i < buf.length; i++) {
            // i = 0、此时byte只会截取最后8位
            // i = 1、此时byte只会截取最后8-16位
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    /**
     * 基础类型序列化
     * @param n
     * @return
     */
    public static byte[] intToBytes(int n) {
        byte[] buf = new byte[4];
        for (int i = 0; i < buf.length; i++) {
            // i = 0、此时byte只会截取最后8位
            // i = 1、此时byte只会截取最后8-16位
            // i = 2、此时byte只会截取最后16-24位
            // i = 3、此时byte只会截取最后24-32位
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    /**
     * 反序列化
     * @param buf
     * @param offset
     * @return
     */
    public static int byteToInt(byte[] buf, int offset) {
        // buf[0] 是最后8位
        // buf[1] 是最后8-16位
        // buf[2] 是最后16-24位
        // buf[3] 是最后24-32位
        return buf[offset] & 0xff
                | ((buf[offset + 1] << 8) & 0xff00)
                | ((buf[offset + 2] << 16) & 0xff0000)
                | ((buf[offset + 3] << 24) & 0xff000000);
    }

    public static short byteToShort(byte[] buf, int offset) {
        // buf[0] 是最后8位
        // buf[1] 是最后8-16位
        // buf[2] 是最后16-24位
        // buf[3] 是最后24-32位
        //return buf[offset] & 0xff
        //        | ((buf[offset + 1] << 8) & 0xff00);
        return 1;
    }

    public static int byteToLong(byte[] buf, int offset) {
        // buf[0] 是最后8位
        // buf[1] 是最后8-16位
        // buf[2] 是最后16-24位
        // buf[3] 是最后24-32位
        return buf[offset] & 0xff
                | ((buf[offset + 1] << 8) & 0xff00)
                | ((buf[offset + 2] << 16) & 0xff0000)
                //| ((buf[offset + 3] << 24) & 0xff000000)
                //| ((buf[offset + 1] << 32) & 0xff00000000)
                //| ((buf[offset + 2] << 40) & 0xff0000)
                //| ((buf[offset + 3] << 48) & 0xff000000)
                | ((buf[offset + 3] << 56) & 0xff000000);
    }
}
