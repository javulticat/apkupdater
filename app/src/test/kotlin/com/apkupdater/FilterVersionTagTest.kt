package com.apkupdater

import com.apkupdater.util.filterVersionTag
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterVersionTagTest {

    @Test
    fun numericVersionUnchanged() {
        assertEquals("1.0.0", filterVersionTag("1.0.0"))
    }

    @Test
    fun removesLeadingV() {
        assertEquals("1.2.3", filterVersionTag("v1.2.3"))
    }

    @Test
    fun removesLeadingText() {
        assertEquals("2.0.0-beta", filterVersionTag("release-2.0.0-beta"))
    }

    @Test
    fun removesMultipleLeadingNonDigits() {
        assertEquals("3.1.4", filterVersionTag("vvv3.1.4"))
    }

    @Test
    fun emptyStringReturnsEmpty() {
        assertEquals("", filterVersionTag(""))
    }

    @Test
    fun noDigitsReturnsEmpty() {
        assertEquals("", filterVersionTag("release"))
    }

    @Test
    fun preservesTrailingText() {
        assertEquals("1.0.0-rc1", filterVersionTag("v1.0.0-rc1"))
    }

    @Test
    fun handlesVersionWithSpaces() {
        assertEquals("1.0.0 build", filterVersionTag("version 1.0.0 build"))
    }
}
