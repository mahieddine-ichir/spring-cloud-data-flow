package com.thinatech.scs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.common.serialization.Serializer

class UploadSerializer : Serializer<Upload> {

    private lateinit var objectMapper: ObjectMapper

    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {
        objectMapper = ObjectMapper().registerModule(KotlinModule())
    }

    override fun serialize(topic: String?, data: Upload?): ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }
}
