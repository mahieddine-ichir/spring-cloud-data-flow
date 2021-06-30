package com.thinatech.scs

import org.springframework.integration.annotation.Transformer
import org.springframework.integration.http.multipart.UploadedMultipartFile
import org.springframework.util.LinkedMultiValueMap
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class MultiPartReceiver(private val config: UploadConfig) {

    @Transformer
    fun receive(payload: LinkedMultiValueMap<String, Any>): Upload {
        val items = config.parts
                .asSequence()
                .filter { payload.keys.contains(it) }
                .map {
                    payload.entries
                            .filter { (k, _) -> k.equals(it) }
                            .map { (_, v) -> v[0] as UploadedMultipartFile }[0]
                }
                .map {
                    val createTempFile = Files.createTempFile(Paths.get(config.tmpDirectory), "uploads-", ".tmp")
                    Files.copy(it.inputStream, createTempFile, StandardCopyOption.REPLACE_EXISTING)

                    UploadItem(createTempFile.toFile().absolutePath, it.originalFilename)
                }
                .toList()

        return Upload(items)
    }
}
