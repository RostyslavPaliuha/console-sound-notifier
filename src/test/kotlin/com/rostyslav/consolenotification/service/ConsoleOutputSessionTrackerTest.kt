package com.rostyslav.consolenotification.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class ConsoleOutputSessionTrackerTest {

    @Test
    fun `first occurrence of binding should play sound`() {
        val tracker = ConsoleOutputSessionTracker()
        assertTrue(tracker.recordOccurrence("ERROR"))
        assertEquals(mapOf("ERROR" to 1), tracker.occurrencesSnapshot())
    }

    @Test
    fun `repeated occurrence of binding should not play sound again`() {
        val tracker = ConsoleOutputSessionTracker()
        assertTrue(tracker.recordOccurrence("ERROR"))
        assertFalse(tracker.recordOccurrence("ERROR"))
        assertEquals(mapOf("ERROR" to 2), tracker.occurrencesSnapshot())
    }

    @Test
    fun `different bindings should each play once during same session`() {
        val tracker = ConsoleOutputSessionTracker()
        assertTrue(tracker.recordOccurrence("ERROR"))
        assertTrue(tracker.recordOccurrence("DONE"))
        assertFalse(tracker.recordOccurrence("ERROR"))
        assertEquals(mapOf("ERROR" to 2, "DONE" to 1), tracker.occurrencesSnapshot())
    }

    @Test
    fun `repeated occurrences should include only bindings with count greater than one`() {
        val tracker = ConsoleOutputSessionTracker()
        tracker.recordOccurrence("ERROR")
        tracker.recordOccurrence("DONE")
        tracker.recordOccurrence("ERROR")
        assertEquals(mapOf("ERROR" to 2), tracker.repeatedOccurrences())
    }

    @Test
    fun `reset should clear session state`() {
        val tracker = ConsoleOutputSessionTracker()
        tracker.recordOccurrence("ERROR")
        tracker.recordOccurrence("ERROR")
        tracker.reset()
        assertEquals(emptyMap<String,Int>(), tracker.occurrencesSnapshot())
        assertTrue(tracker.recordOccurrence("ERROR"))
    }
}
