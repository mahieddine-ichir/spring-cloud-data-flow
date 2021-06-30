package com.thinatech.scs

import org.reactivestreams.Publisher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.http.dsl.Http
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.Message
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import reactor.core.publisher.Flux
import java.util.function.Supplier


@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Bean
    fun resolver() = CommonsMultipartResolver()

    @Bean
    fun config() = UploadConfig()

    @Bean
    fun flow1(kafkaTemplate: KafkaTemplate<String, Int>,
              consumerFactory: ConsumerFactory<String, String>): Publisher<Message<Upload>> {

        return IntegrationFlows
                .from(Http.inboundChannelAdapter("/")
                        .requestMapping { m -> m.methods(HttpMethod.POST) }
                        //.requestChannel(channel())
                )
                .transform(MultiPartReceiver(config()))
                .toReactivePublisher()
    }

    @Bean
    fun output(message: Publisher<Message<Upload>>): Supplier<Flux<Message<Upload>>> {
        return Supplier<Flux<Message<Upload>>> { Flux.from(message) }
    }

}

fun main(args: Array<String>) {
    runApplication<FileUploadApplication>(*args)
}
