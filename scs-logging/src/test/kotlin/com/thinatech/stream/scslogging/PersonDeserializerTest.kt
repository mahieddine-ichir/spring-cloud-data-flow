package com.thinatech.stream.scslogging

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PersonDeserializerTest {

    @Test
    fun deserialize_json() {
        val person = PersonDeserializer().deserialize("any", """
            {"name": "ichir"}
        """.toByteArray())

        Assertions.assertEquals("ichir", person.name)
    }
}