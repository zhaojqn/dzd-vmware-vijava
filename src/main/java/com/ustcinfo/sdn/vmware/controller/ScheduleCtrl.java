package com.ustcinfo.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



 
/**
 * 
 * @author yu.dong
 * @desc 定时器
 */

@Component
 @Order(value=3)
public class ScheduleCtrl {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleCtrl.class);

 
	/*
	 * @Scheduled(fixedRate=3000)：上一次开始执行时间点后3秒再次执行；
	 * @Scheduled(fixedDelay=3000)：上一次执行完毕时间点后3秒再次执行；
	 * @Scheduled(initialDelay=1000,fixedDelay=3000)：第一次延迟1秒执行，然后在上一次执行完毕时间点后3秒再次执行；
	 * 
	 * @Scheduled(cron="* * * * * ?")：按cron规则执行。
	 * 
	 */
	//@Scheduled(cron="1 0 0 * * ?") //每天00:00:01执行一次
	//@Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
	@Scheduled(initialDelay=1000,fixedDelay=30000)
	public boolean synInfoHostCluster() {
	 
		return true;
	}
	 
}
