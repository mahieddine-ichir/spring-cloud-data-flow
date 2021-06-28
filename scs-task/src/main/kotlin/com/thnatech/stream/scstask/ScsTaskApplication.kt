package com.thnatech.stream.scstask

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.task.configuration.EnableTask
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableTask
class ScsTaskApplication {

    @Bean
    fun commandLineRunner(): CommandLineRunner {
        return CommandLineRunner {
            println("Hello SCDF Task")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ScsTaskApplication>(*args)
}