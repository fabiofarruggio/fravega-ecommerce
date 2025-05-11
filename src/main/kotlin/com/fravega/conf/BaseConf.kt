package com.fravega.conf

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/application.properties", encoding = "UTF-8")
@ComponentScan("com.fravega")
class BaseConf
