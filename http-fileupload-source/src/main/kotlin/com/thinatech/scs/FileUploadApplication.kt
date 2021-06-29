package com.thinatech.scs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.GenericSelector
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.support.MessageBuilder

@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Bean
    fun numChannel(): MessageChannel {
        return MessageChannels.direct()
                .datatype(String::class.java)
                .interceptor(MyChannelInterceptor())
                .get()
    }

    @Bean
    fun flow1(): IntegrationFlow {
        return IntegrationFlows.from("numChannel")
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
                .handle(MessageHandler {
                    println(">>>>> $it")
                })
                .get()
    }

    /*
    @Bean
    fun output(): QueueChannelSpec? {
        return MessageChannels.queue("output")
    }

    @Bean
    fun buildHttpSupport(): HttpRequestHandlingEndpointSupport {
        return this.build()!!.get()
    }

    fun build(): HttpRequestHandlerEndpointSpec? {
        return Http.inboundChannelAdapter("/")
                .requestMapping { m -> m.methods(HttpMethod.POST)
                        .consumes(MediaType.MULTIPART_FORM_DATA_VALUE) }
                .requestChannel("output")
    }
     */
}

fun main(args: Array<String>) {
    val context = runApplication<FileUploadApplication>(*args)

    println(">>>>> sending message ... of type Int")
    val channel = context.getBean("numChannel", MessageChannel::class.java)

    (1..100)
            .map { it.toString() }
            .map { MessageBuilder.withPayload(it).build() }
            .forEach { channel.send(it) }
}
