package com.thinatech.scs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.http.dsl.Http
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.MessageChannel
import org.springframework.web.multipart.commons.CommonsMultipartResolver

@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Autowired
    lateinit var outputChannel: Source;

    @Bean
    fun channel(): MessageChannel {
        return MessageChannels.direct()
                .get()
    }

    @Bean
    fun resolver() = CommonsMultipartResolver()

    @Bean
    fun config() = UploadConfig()

    @Bean
    fun transform() = MultiPartReceiver(config())

    @Bean
    fun flow1(kafkaTemplate: KafkaTemplate<String, Int>,
              consumerFactory: ConsumerFactory<String, String>): IntegrationFlow {

        return IntegrationFlows
                .from(Http.inboundChannelAdapter("/")
                        .requestMapping { m -> m.methods(HttpMethod.POST) }
                        .requestChannel(channel())
                )
                .transform(transform())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate)
                        .messageKey<String> { it.headers["id"].toString() }
                        .topic("flow-output"))
                .get()
    }
}

fun main(args: Array<String>) {
    runApplication<FileUploadApplication>(*args)
}
