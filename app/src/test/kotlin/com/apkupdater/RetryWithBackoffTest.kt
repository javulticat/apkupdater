package com.apkupdater

import com.apkupdater.util.retryWithBackoff
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class RetryWithBackoffTest {

    @Test
    fun successfulFlowNoRetries() = runBlocking {
        val result = flow {
            emit(42)
        }.retryWithBackoff(maxRetries = 3, initialDelayMs = 10L).toList()

        assertEquals(listOf(42), result)
    }

    @Test
    fun retriesOnIOException() = runBlocking {
        var attempts = 0
        val result = flow {
            attempts++
            if (attempts < 3) throw IOException("Network error")
            emit("success")
        }.retryWithBackoff(maxRetries = 3, initialDelayMs = 10L).toList()

        assertEquals(listOf("success"), result)
        assertEquals(3, attempts)
    }

    @Test
    fun doesNotRetryNonIOException() = runBlocking {
        var attempts = 0
        try {
            flow<Int> {
                attempts++
                throw IllegalArgumentException("Bad input")
            }.retryWithBackoff(maxRetries = 3, initialDelayMs = 10L).toList()
            fail("Should have thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(1, attempts)
        }
    }

    @Test
    fun stopsAfterMaxRetries() = runBlocking {
        var attempts = 0
        try {
            flow<Int> {
                attempts++
                throw IOException("Persistent failure")
            }.retryWithBackoff(maxRetries = 2, initialDelayMs = 10L).toList()
            fail("Should have thrown")
        } catch (e: IOException) {
            assertEquals(3, attempts) // 1 initial + 2 retries
        }
    }

    @Test
    fun customRetryPredicate() = runBlocking {
        var attempts = 0
        val result = flow {
            attempts++
            if (attempts < 2) throw IllegalStateException("Temporary")
            emit("ok")
        }.retryWithBackoff(
            maxRetries = 3,
            initialDelayMs = 10L,
            isRetryable = { it is IllegalStateException }
        ).toList()

        assertEquals(listOf("ok"), result)
        assertEquals(2, attempts)
    }
}
