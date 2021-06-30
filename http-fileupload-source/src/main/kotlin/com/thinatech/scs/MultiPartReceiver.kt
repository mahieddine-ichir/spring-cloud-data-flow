package com.thinatech.scs

import org.springframework.integration.http.multipart.UploadedMultipartFile
import org.springframework.util.LinkedMultiValueMap
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.function.Function

class MultiPartReceiver(private val config: UploadConfig): Function<LinkedMultiValueMap<String, Any>, Upload> {

    override fun apply(payload: LinkedMultiValueMap<String, Any>): Upload {
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

        val upload = Upload(items)
        println("generated upload data $upload")
        return upload
    }
}
