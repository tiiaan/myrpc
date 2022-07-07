package com.tiiaan.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
