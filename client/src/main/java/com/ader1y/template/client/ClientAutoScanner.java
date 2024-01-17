package com.ader1y.template.client;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * client的自动扫描与注入入口，只需要注入该类即可自动注入所有client api
 */
@EnableFeignClients
@ComponentScan("com.ader1y.template.client")
public class ClientAutoScanner {



}
