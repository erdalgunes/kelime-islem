package com.kelimeislem.utils

import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Simple performance monitoring utility for tracking operation durations
 */
object PerformanceMonitor {
    private val timeSource = TimeSource.Monotonic

    /**
     * Measures the execution time of a block of code
     */
    inline fun <T> measureTime(
        operationName: String,
        block: () -> T
    ): T {
        val startMark = timeSource.markNow()
        return try {
            block()
        } finally {
            val duration = startMark.elapsedNow()
            logPerformance(operationName, duration)
        }
    }

    /**
     * Logs performance metrics
     */
    fun logPerformance(operation: String, duration: Duration) {
        val milliseconds = duration.inWholeMilliseconds
        when {
            milliseconds > 1000 -> println("⚠️ SLOW: $operation took ${duration.inWholeSeconds}s")
            milliseconds > 100 -> println("ℹ️ INFO: $operation took ${milliseconds}ms")
            else -> println("✅ FAST: $operation took ${milliseconds}ms")
        }
    }

    /**
     * Creates a performance checkpoint for multi-stage operations
     */
    class Checkpoint(private val name: String) {
        private val startMark = timeSource.markNow()
        private var lastMark = startMark

        fun mark(stageName: String) {
            val elapsed = lastMark.elapsedNow()
            logPerformance("$name - $stageName", elapsed)
            lastMark = timeSource.markNow()
        }

        fun finish() {
            val total = startMark.elapsedNow()
            logPerformance("$name - Total", total)
        }
    }
}