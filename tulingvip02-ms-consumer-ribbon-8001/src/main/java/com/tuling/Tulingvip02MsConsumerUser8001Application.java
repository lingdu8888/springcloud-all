package com.tuling;

import com.tuling.config.TulingLbCfg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "MS-PROVIDER-ORDER",configuration = TulingLbCfg.class)
public class Tulingvip02MsConsumerUser8001Application {

	public static void main(String[] args) {
		SpringApplication.run(Tulingvip02MsConsumerUser8001Application.class, args);
	}

}
