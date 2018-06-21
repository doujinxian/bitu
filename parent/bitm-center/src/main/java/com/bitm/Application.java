package com.bitm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Description: @ServletComponentScan 设置启动时spring能够扫描到我们自己编写的servlet和filter, 用于Druid监控
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/23 19:41
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.bitm.**.mapper") //配置扫描mapper接口的地址
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

}
