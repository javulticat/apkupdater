package com.apkupdater.util

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream


class Downloader(
    private val client: OkHttpClient,
    private val apkPureClient: OkHttpClient,
    private val auroraClient: OkHttpClient,
    private val dir: File
) {

    fun download(url: String): File {
        val file = File(dir, randomUUID())
        client.newCall(downloadRequest(url)).execute().use { response ->
            if (response.isSuccessful) {
                file.outputStream().use { output ->
                    response.body?.byteStream()?.copyTo(output)
                }
            }
        }
        return file
    }

    fun downloadStream(url: String): InputStream? = runCatching {
        val c = when {
            url.contains("apkpure") -> apkPureClient
            url.contains("aurora") -> auroraClient
            else -> client
        }
        val response = c.newCall(downloadRequest(url)).execute()
        if (response.isSuccessful) {
            response.body?.byteStream() ?: run {
                response.close()
                null
            }
        } else {
            response.close()
            Log.e("Downloader", "Download failed with error code: ${response.code}")
            null
        }
    }.getOrElse {
        Log.e("Downloader", "Error downloading", it)
        null
    }

    private fun downloadRequest(url: String) = Request.Builder().url(url).build()

}
