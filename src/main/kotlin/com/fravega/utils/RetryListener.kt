package com.fravega.utils

import org.slf4j.LoggerFactory
import org.testng.IAnnotationTransformer
import org.testng.annotations.ITestAnnotation
import java.lang.reflect.Constructor
import java.lang.reflect.Method

class RetryListener : IAnnotationTransformer {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RetryListener::class.java)
    }

    override fun transform(
        annotation: ITestAnnotation?,
        testClass: Class<*>?,
        testConstructor: Constructor<*>?,
        testMethod: Method?
    ) {
        LOGGER.info("Applying RetryListener to test method: ${testMethod?.name}")
        if (annotation?.retryAnalyzer == null) {
            annotation?.setRetryAnalyzer(Retry::class.java)
        }
    }
}
