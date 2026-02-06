package com.apkupdater

import com.apkupdater.transform.iconUri
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformsTest {

    @Test
    fun iconUri_formatsCorrectly() {
        val uri = iconUri("com.example.app", 12345)
        assertEquals("android.resource://com.example.app/12345", uri.toString())
    }

    @Test
    fun iconUri_handlesZeroId() {
        val uri = iconUri("com.test", 0)
        assertEquals("android.resource://com.test/0", uri.toString())
    }
}
