package com.thinatech.scs

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.ChannelInterceptor

class MyChannelInterceptor : ChannelInterceptor {

    override fun postSend(message: Message<*>, channel: MessageChannel, sent: Boolean) {
        println(">>>>> preparing to send payload of type ${message.payload.javaClass} to channel $channel")
    }
}
