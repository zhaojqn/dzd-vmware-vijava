package com.dzd.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dzd.sdn.vmware.Service.ClientSesion;



@Component
@Order(value=2)
public class StartupRunner<T> implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupRunner.class);

	@Autowired
	private ClientSesion cs;
    @Override
    public void run(String... args) throws Exception {
    	LOGGER.info(">>>>>>>>>>>>>>>服务启动执行，执行  <<<<<<<<<<<<<");
    	cs.KeepAlive();
     }

}