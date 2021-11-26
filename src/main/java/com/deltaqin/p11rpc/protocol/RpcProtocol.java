package com.deltaqin.p11rpc.protocol;

import com.deltaqin.p11rpc.entity.User;
import com.deltaqin.p11rpc.util.ByteConverter;
import lombok.Data;

import java.io.Serializable;

/**
 * @author deltaqin
 * @date 2021/10/14 下午12:31
 */
@Data
public class RpcProtocol implements Serializable {
    public static int CMD_CREATE_USER = 1;
    private int version;
    private int cmd;
    private int magicNum;
    private int bodyLen = 0;
    private byte[] body;
    final public static int HEAD_LEN = 16;

    /**
     * userInfo 转换为 byte 数组 作为 body
     * @param info
     * @return
     */
    public byte[] userInfoToByteArray(User info) {
        byte[] data = new byte[Long.BYTES + Short.BYTES + Short.BYTES];
        int index = 0;
        System.arraycopy(ByteConverter.longToBytes(info.getUid()), 0, data, index, Long.BYTES);
        index += Long.BYTES;
        System.arraycopy(ByteConverter.shortToBytes(info.getAge()), 0, data, index, Short.BYTES);
        index += Short.BYTES;
        System.arraycopy(ByteConverter.shortToBytes(info.getSex()), 0, data, index, Short.BYTES);
        return data;
    }

    /**
     * header + body ==> 等待发送的 byte[]
     * @return
     */
    public byte[] generateByteArray() {
        byte[] data = new byte[HEAD_LEN + bodyLen];
        int index = 0;
        System.arraycopy(ByteConverter.intToBytes(version), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;
        System.arraycopy(ByteConverter.intToBytes(cmd), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;
        System.arraycopy(ByteConverter.intToBytes(magicNum), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;
        System.arraycopy(ByteConverter.intToBytes(bodyLen), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;
        System.arraycopy(body, 0, data, index, body.length);
        return data;
    }

    /**
     * 收到返回的消息之后将字节数组 反序列化 为对象。
     * @param recvData
     * @return
     */
    public RpcProtocol byteArrayToRpcHeader(byte[] recvData) {
        int index = 0;
        this.setVersion(ByteConverter.byteToInt(recvData, index));
        index += Integer.BYTES;

        this.setCmd(ByteConverter.byteToInt(recvData, index));
        index += Integer.BYTES;

        this.setMagicNum(ByteConverter.byteToInt(recvData, index));
        index += Integer.BYTES;

        this.setBodyLen(ByteConverter.byteToInt(recvData, index));
        index += Integer.BYTES;

        this.body = new byte[this.bodyLen];
        System.arraycopy(recvData, index, this.body, 0, this.bodyLen);

        return this;
    }

    /**
     * body 字节数组转换为 user
     * @param data
     * @return
     */
    public User byteArrayToUserInfo(byte[] data) {
        User user = new User();
        int index = 0;
        user.setUid(ByteConverter.byteToLong(data, index));
        index += Long.BYTES;

        user.setAge(ByteConverter.byteToShort(data, index));
        index += Short.BYTES;

        user.setSex(ByteConverter.byteToShort(data, index));
        index += Short.BYTES;
        return user;
    }
}
