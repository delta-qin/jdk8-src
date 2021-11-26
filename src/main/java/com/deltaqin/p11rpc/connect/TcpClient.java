package com.deltaqin.p11rpc.connect;

/**
 * @author deltaqin
 * @date 2021/10/14 下午2:01
 */
public class TcpClient {
    private static volatile TcpClient INSTANCE;

    private TcpClient() {

    }

    public static TcpClient getInstance() {
        if (INSTANCE == null) {
            synchronized (TcpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TcpClient();
                }
            }
        }
        return INSTANCE;
    }

    public void init() {

    }

    public void sendData(byte[] reqData) {

    }

    public byte[] recvData() {
        return new byte[0];
    }
}
