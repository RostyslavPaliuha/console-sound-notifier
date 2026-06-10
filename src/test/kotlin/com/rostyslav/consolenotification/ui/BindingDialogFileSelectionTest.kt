package com.rostyslav.consolenotification.ui

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
}
