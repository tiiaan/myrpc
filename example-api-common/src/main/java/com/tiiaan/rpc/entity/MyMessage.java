package com.tiiaan.rpc.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage implements Serializable {

    private String id;
    private String message;

    public MyMessage(String message) {
        this(UUID.randomUUID().toString(), message);
    }

}