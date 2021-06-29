package com.thinatech.scs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.ResolvableType
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.GenericSelector
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.http.dsl.Http
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Bean
    fun flow1(kafkaTemplate: KafkaTemplate<String, Int>,
              consumerFactory: ConsumerFactory<String, String>): IntegrationFlow {

        return IntegrationFlows
                .from(Http.inboundChannelAdapter("/"))
                .filter(GenericSelector<String> {
                    try {
                        (it.toInt()) % 3 == 0
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                })
                .transform(GenericTransformer<String, Int> {
                    println(">>>> transforming $it")
                    it.toInt()
                })
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate)
                        .messageKey<String> { it.headers["id"].toString() }
                        .topic("flow-output")
                )
                .get()
    }
}

fun main(args: Array<String>) {
    runApplication<FileUploadApplication>(*args)
}
