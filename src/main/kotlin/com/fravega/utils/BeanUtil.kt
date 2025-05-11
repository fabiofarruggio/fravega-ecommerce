package com.fravega.utils

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service

/*
    This class is for testing. Is not used yet.
    The porpouse is implement a class that it can instantiate retry class in order to use @value from spring.
    More Info:
    https://dzone.com/articles/autowiring-spring-beans-into-classes-not-managed-by-spring
 */

@Service
class BeanUtil : ApplicationContextAware {

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {
        private var context: ApplicationContext? = null
        fun <T> getBean(beanClass: Class<T>?): T {
            return context!!.getBean(beanClass)
        }
    }
}
