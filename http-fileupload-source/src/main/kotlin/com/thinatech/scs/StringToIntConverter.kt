package com.thinatech.scs

import org.springframework.core.convert.converter.Converter

class StringToIntConverter : Converter<String, Number> {
    override fun convert(source: String): Number {
        println(">>>>> Converting $source")
        return source.toInt()
    }
}
