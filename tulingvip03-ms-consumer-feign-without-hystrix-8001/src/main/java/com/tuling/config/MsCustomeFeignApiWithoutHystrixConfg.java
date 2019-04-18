package com.tuling.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Created by smlz on 2019/4/15.
 */
public class MsCustomeFeignApiWithoutHystrixConfg {

    @Scope("prototype")
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
