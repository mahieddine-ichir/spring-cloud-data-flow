package com.thinatech.stream.scslogging

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer

class PersonDeserializer: Deserializer<Person> {

    private val objectMapper = ObjectMapper()

    override fun deserialize(p0: String?, p1: ByteArray?): Person {
        println("deserialize ${p1?.let { String(it) }} on topic $p0")
        return objectMapper.readValue(p1, Person::class.java)
    }

}