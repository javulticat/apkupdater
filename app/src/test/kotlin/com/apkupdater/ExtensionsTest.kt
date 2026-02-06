package com.apkupdater

import com.apkupdater.util.filterVersionTag
import com.apkupdater.util.orFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtensionsTest {

    @Test
    fun orFalse_nullReturnsFalse() {
        val value: Boolean? = null
        assertFalse(value.orFalse())
    }

    @Test
    fun orFalse_trueReturnsTrue() {
        val value: Boolean? = true
        assertTrue(value.orFalse())
    }

    @Test
    fun orFalse_falseReturnsFalse() {
        val value: Boolean? = false
        assertFalse(value.orFalse())
    }

    @Test
    fun filterVersionTag_stripsLeadingV() {
        assertEquals("1.2.3", filterVersionTag("v1.2.3"))
    }

    @Test
    fun filterVersionTag_stripsTextPrefix() {
        assertEquals("2.0.0", filterVersionTag("release-v2.0.0"))
    }

    @Test
    fun filterVersionTag_numericUnchanged() {
        assertEquals("3.0.0", filterVersionTag("3.0.0"))
    }

    @Test
    fun filterVersionTag_preservesSuffix() {
        assertEquals("1.0-alpha1", filterVersionTag("v1.0-alpha1"))
    }

    @Test
    fun filterVersionTag_emptyInput() {
        assertEquals("", filterVersionTag(""))
    }

    @Test
    fun filterVersionTag_onlyLetters() {
        assertEquals("", filterVersionTag("abc"))
    }
}
