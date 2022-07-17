package com.tiiaan.rpc.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractRpcServer implements MyRpcServer {

    protected Integer port;

}
