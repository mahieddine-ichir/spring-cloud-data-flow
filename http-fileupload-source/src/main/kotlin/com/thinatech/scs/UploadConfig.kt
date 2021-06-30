package com.thinatech.scs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("upload")
@Configuration
class UploadConfig {

    lateinit var tmpDirectory: String

    lateinit var parts: List<String>
}
