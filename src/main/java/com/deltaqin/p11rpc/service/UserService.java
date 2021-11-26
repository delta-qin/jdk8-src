package com.deltaqin.p11rpc.service;

import com.deltaqin.p11rpc.connect.TcpClient;
import com.deltaqin.p11rpc.entity.User;
import com.deltaqin.p11rpc.protocol.RpcProtocol;
import com.deltaqin.p11rpc.util.ByteConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author deltaqin
 * @date 2021/10/14 下午1:00
 */
@Slf4j
public class UserService {
    public int addUser(User user) {
        // 初始化连接
        TcpClient tcpClient = TcpClient.getInstance();
        try {
            tcpClient.init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("init rpc client error");
        }

        // 构造请求数据
        RpcProtocol rpcReq = new RpcProtocol();
        rpcReq.setCmd(RpcProtocol.CMD_CREATE_USER);
        rpcReq.setVersion(0x01);
        rpcReq.setMagicNum(0x20211014);
        byte[] body = rpcReq.userInfoToByteArray(user);
        rpcReq.setBodyLen(body.length);
        rpcReq.setBody(body);

        // 序列化数据
        byte[] reqData = rpcReq.generateByteArray();

        // 发送请求等待返回
        tcpClient.sendData(reqData);

        // 接收请求
        byte[] recvData = tcpClient.recvData();

        // 反序列化返回数据
        RpcProtocol rpcRespHeader = new RpcProtocol();
        rpcRespHeader.byteArrayToRpcHeader(recvData);
        int res = ByteConverter.bytesToIntBigEndian(rpcRespHeader.getBody(), 0);

        return res;
    }
}
