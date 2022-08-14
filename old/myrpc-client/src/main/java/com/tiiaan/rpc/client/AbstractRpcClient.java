package com.tiiaan.rpc.client;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Data
@Accessors(chain = true)
public abstract class AbstractRpcClient implements MyRpcClient {

    protected String serverHost;
    protected Integer serverPort;

}
