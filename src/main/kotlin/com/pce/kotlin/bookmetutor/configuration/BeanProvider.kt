package com.pce.kotlin.bookmetutor.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.concurrent.Executor

@Configuration
class BeanProvider {
    @Bean
    fun getEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean("taskExecutor")
    fun getExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 2
            setQueueCapacity(500)
            initialize()
        }
    }
}
