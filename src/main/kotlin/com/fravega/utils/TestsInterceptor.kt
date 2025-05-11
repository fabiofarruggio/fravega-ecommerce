package com.fravega.utils

import org.slf4j.LoggerFactory
import org.testng.IMethodInstance
import org.testng.IMethodInterceptor
import org.testng.ITestContext
import org.testng.annotations.Test
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Filters test methods based on the -Dgroups and -DgroupsIntersection system properties.
 * Supports intersection (default) or union mode.
 */
class TestsInterceptor : IMethodInterceptor {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TestsInterceptor::class.java)
    }

    override fun intercept(
        allMethods: List<IMethodInstance>,
        context: ITestContext
    ): List<IMethodInstance> {
        LOGGER.info("Intercepting test methods to apply group filtering...")
        val groupProp = System.getProperty("groups")
        val groupList: List<String> = if (!groupProp.isNullOrBlank()) {
            Stream.of(*groupProp.split(",".toRegex()).toTypedArray())
                .map(String::trim)
                .filter { it.isNotEmpty() }
                .collect(Collectors.toList())
        } else {
            emptyList()
        }
        LOGGER.info("Requested groups: {}", if (groupList.isEmpty()) "[All]" else groupList)

        val intersectionMode = System.getProperty("groupsIntersection", "y").equals("y", ignoreCase = true)
        if (groupList.isEmpty()) return allMethods

        val filtered = if (intersectionMode) {
            LOGGER.info("Using intersection mode: test must contain all groups")
            allMethods.filter { instance ->
                val methodGroups = instance.method.constructorOrMethod.method.getAnnotation(Test::class.java).groups
                groupList.all { it in methodGroups }
            }
        } else {
            LOGGER.info("Using union mode: test must contain at least one group")
            allMethods.filter { instance ->
                val methodGroups = instance.method.constructorOrMethod.method.getAnnotation(Test::class.java).groups
                methodGroups.any { it in groupList }
            }
        }

        LOGGER.info("Methods selected: {} out of {}", filtered.size, allMethods.size)
        return filtered
    }
}