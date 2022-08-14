package com.tiiaan.rpc.client.socket;

import com.tiiaan.rpc.client.AbstractRpcClient;
import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
@Data
@Accessors(chain = true)
public class SocketRpcClient extends AbstractRpcClient {

    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest) {
        try (Socket socket = new Socket(serverHost, serverPort);) {
            log.info("连接到服务器 {}:{}", serverHost, serverPort);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(myRpcRequest);
            objectOutputStream.flush();
            log.info("发送 RPC 调用请求 {}", myRpcRequest);
            return objectInputStream.readObject();
        } catch (UnknownHostException e) {
            log.error("请求发送失败", e);
            throw new MyRpcException(MyRpcError.SERVER_NOT_FOUND);
        } catch (IOException | ClassNotFoundException e) {
            log.error("请求发送失败", e);
            throw new MyRpcException(MyRpcError.IO_ERROR);
        }
    }

}
