package com.thinatech.stream.scslogging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import kotlin.random.Random

@SpringBootApplication
class ScsLoggingApplication {

	@Bean
	fun supply(): Supplier<Person> {
		return Supplier { Person("person-${Random.nextInt()}") }
	}

	@Bean
	fun transform(): Function<Person, String> {
		return Function<Person, String> { it.name?:"null" }
	}

	@Bean
	fun trim(): Function<String, String> {
		return Function { if (it.length > 3) it.substring(0, 3) + "..." + it.substring(it.length-3, it.length) else it }
	}

	@Bean
	fun log(): Consumer<String> {
		return Consumer<String>{ println(it)}
	}
}

fun main(args: Array<String>) {
	runApplication<ScsLoggingApplication>(*args)
}
