package com.thinatech.scs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.support.GenericMessage
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableIntegration
class FileUploadApplication {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Bean
    fun numChannel(): MessageChannel {
        val directChannel = DirectChannel()
        directChannel.setDatatypes(Number::class.java)
        directChannel.subscribe {
            println(">>>>> Received message payload: ${it.payload} - headers: ${it.headers}")
        }
        return directChannel
    }

    @PostConstruct
    fun init() {
        println(">>>>> sending message ... of type Int")
        val channel = applicationContext.getBean("numChannel", MessageChannel::class.java)
        channel.send(GenericMessage<Int>(2))
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
    runApplication<FileUploadApplication>(*args)
}
