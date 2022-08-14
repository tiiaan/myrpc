package com.tiiaan.myrpc.example.client;

import com.tiiaan.rpc.spring.annotation.EnableMyRpcScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableMyRpcScanner(basePackage = {"com.tiiaan.rpc"})
@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

}
