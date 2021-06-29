package com.thinatech.scs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.config.IntegrationConverter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.GenericMessage
import java.util.function.Function

@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Bean
    fun numChannel(): MessageChannel {
        val directChannel = DirectChannel()
        directChannel.setDatatypes(Number::class.java)
        directChannel.subscribe {
            println(">>>>> Received message payload: ${it.payload} - headers: ${it.headers}")
        }
        return directChannel
    }

    @Bean
    @IntegrationConverter
    fun stringToIntConverter(): Converter<String, Number> {
        return StringToIntConverter()
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
    // sending Int
    channel.send(GenericMessage<Int>(2))

    // sending String
    channel.send(GenericMessage<String>("3"))
}
