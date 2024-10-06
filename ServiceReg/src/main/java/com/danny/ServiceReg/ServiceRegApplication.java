package com.danny.ServiceReg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//TODO: Solve problem with host register as "host.docker.internal:" / "NTT.."
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegApplication.class, args);
    }

}
