package com.thinatech.stream.scslogging

import java.util.function.Supplier

class DataSource : Supplier<Person> {

    override fun get(): Person {
        return Person("any person")
    }
}