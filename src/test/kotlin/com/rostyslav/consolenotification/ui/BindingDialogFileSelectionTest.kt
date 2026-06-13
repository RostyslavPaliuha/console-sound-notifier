package com.rostyslav.consolenotification.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class BindingDialogFileSelectionTest {

    @Test
    fun `wav files are supported case-insensitively`() {
        assertTrue(isSupportedSoundFileExtension("wav"))
        assertTrue(isSupportedSoundFileExtension("WAV"))
    }

    @Test
    fun `non-wav and missing extensions are unsupported`() {
        assertFalse(isSupportedSoundFileExtension("mp3"))
        assertFalse(isSupportedSoundFileExtension(null))
    }

    @Test
    fun `valid binding input requires nonblank text and wav file path`() {
        assertTrue(isValidBindingInput("ERROR", "/tmp/error.wav"))
        assertTrue(isValidBindingInput("ERROR", "/tmp/error.WAV"))
    }

    @Test
    fun `blank text and placeholder file path are invalid binding input`() {
        assertFalse(isValidBindingInput("", "/tmp/error.wav"))
        assertFalse(isValidBindingInput("   ", "/tmp/error.wav"))
        assertFalse(isValidBindingInput("ERROR", "press to select file..."))
        assertFalse(isValidBindingInput("ERROR", ""))
    }

    @Test
    fun `non-wav file path is invalid binding input`() {
        assertFalse(isValidBindingInput("ERROR", "/tmp/error.mp3"))
        assertFalse(isValidBindingInput("ERROR", "/tmp/error"))
    }
}
