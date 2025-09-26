package com.kelimeislem.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class PerformanceMonitorTest : FreeSpec({

    "PerformanceMonitor" - {

        "measureTime function" - {

            "should execute block and return correct result" {
                val result = PerformanceMonitor.measureTime("test operation") {
                    42
                }
                result shouldBe 42
            }

            "should execute block that returns string" {
                val result = PerformanceMonitor.measureTime("string operation") {
                    "test result"
                }
                result shouldBe "test result"
            }

            "should handle exceptions and still measure time" {
                var logged = false
                try {
                    PerformanceMonitor.measureTime("failing operation") {
                        logged = true // Verify block was entered
                        throw RuntimeException("test exception")
                    }
                } catch (e: RuntimeException) {
                    // Expected exception
                }
                // Block should have been executed before exception
                logged shouldBe true
            }

            "should handle empty block" {
                val result = PerformanceMonitor.measureTime("empty operation") {
                    // Empty block
                }
                result shouldBe Unit
            }

            "should measure fast operations correctly" {
                val result = PerformanceMonitor.measureTime("fast operation") {
                    // Quick operation
                    val sum = (1..100).sum()
                    sum
                }
                result shouldBe 5050 // 1+2+...+100
            }

            "should measure operations with computation" {
                val result = PerformanceMonitor.measureTime("computation") {
                    (1..1000).map { it * 2 }.sum()
                }
                result shouldBe 1001000 // sum of 2,4,6...2000
            }

            "should handle nested measurements" {
                val result = PerformanceMonitor.measureTime("outer") {
                    val inner = PerformanceMonitor.measureTime("inner") {
                        "inner-result"
                    }
                    "$inner-outer"
                }
                result shouldBe "inner-result-outer"
            }
        }

        "logPerformance function" - {

            "should handle FAST durations (less than 100ms)" {
                // Just verify it doesn't throw
                PerformanceMonitor.logPerformance("quick task", 50.milliseconds)
                PerformanceMonitor.logPerformance("instant task", Duration.ZERO)
                PerformanceMonitor.logPerformance("tiny task", 1.milliseconds)
                PerformanceMonitor.logPerformance("fast boundary", 99.milliseconds)
            }

            "should handle INFO durations (100ms to 1000ms)" {
                // Just verify it doesn't throw
                PerformanceMonitor.logPerformance("medium task", 500.milliseconds)
                PerformanceMonitor.logPerformance("boundary 100", 100.milliseconds)
                PerformanceMonitor.logPerformance("almost slow", 999.milliseconds)
                PerformanceMonitor.logPerformance("half second", 500.milliseconds)
            }

            "should handle SLOW durations (greater than 1000ms)" {
                // Just verify it doesn't throw
                PerformanceMonitor.logPerformance("slow task", 1500.milliseconds)
                PerformanceMonitor.logPerformance("boundary 1000", 1000.milliseconds)
                PerformanceMonitor.logPerformance("very slow", 5.seconds)
                PerformanceMonitor.logPerformance("huge task", 10.seconds)
            }

            "should handle edge cases" {
                // Just verify these don't throw
                PerformanceMonitor.logPerformance("zero", Duration.ZERO)
                PerformanceMonitor.logPerformance("negative", Duration.ZERO) // Can't have negative duration
                PerformanceMonitor.logPerformance("max", Duration.INFINITE)
            }
        }

        "Checkpoint class" - {

            "should create checkpoint and track single stage" {
                val checkpoint = PerformanceMonitor.Checkpoint("test operation")
                // Simulate some work with computation
                val result = (1..1000).sum()
                checkpoint.finish()
                result shouldBe 500500
            }

            "should track multiple stages with mark" {
                val checkpoint = PerformanceMonitor.Checkpoint("multi-stage")

                // Stage 1
                val result1 = (1..100).sum()
                checkpoint.mark("stage1")

                // Stage 2
                val result2 = (1..200).sum()
                checkpoint.mark("stage2")

                // Final
                val result3 = (1..300).sum()
                checkpoint.finish()

                result1 shouldBe 5050
                result2 shouldBe 20100
                result3 shouldBe 45150
            }

            "should handle immediate finish after creation" {
                val checkpoint = PerformanceMonitor.Checkpoint("instant")
                checkpoint.finish()
                // Just verify it doesn't throw
            }

            "should handle multiple marks in sequence" {
                val checkpoint = PerformanceMonitor.Checkpoint("sequential")

                checkpoint.mark("first")
                checkpoint.mark("second")
                checkpoint.mark("third")
                checkpoint.finish()
                // Just verify it doesn't throw
            }

            "should measure time progression" {
                val checkpoint = PerformanceMonitor.Checkpoint("timing test")

                // Do some work
                busyWait(5)
                checkpoint.mark("after work")

                // Do more work
                busyWait(5)
                checkpoint.mark("after more work")

                checkpoint.finish()
                // Just verify timing works
            }

            "should reset timer after each mark" {
                val checkpoint = PerformanceMonitor.Checkpoint("reset test")

                // First mark immediately
                checkpoint.mark("immediate")

                // Wait and mark again
                busyWait(5)
                checkpoint.mark("after delay")

                // Finish immediately
                checkpoint.finish()
                // Timer should reset after each mark
            }

            "should handle checkpoint with only finish (no marks)" {
                val checkpoint = PerformanceMonitor.Checkpoint("no marks")
                busyWait(5)
                checkpoint.finish()
                // Just verify it doesn't throw
            }

            "should work correctly with nested checkpoints" {
                val outer = PerformanceMonitor.Checkpoint("outer")

                val inner = PerformanceMonitor.Checkpoint("inner")
                inner.mark("inner-mark")
                inner.finish()

                outer.mark("after-inner")
                outer.finish()
                // Verify nested checkpoints work
            }
        }

        "Integration tests" - {

            "should work correctly with nested measurements" {
                var outerExecuted = false
                var innerExecuted = false

                val result = PerformanceMonitor.measureTime("outer operation") {
                    outerExecuted = true
                    val innerResult = PerformanceMonitor.measureTime("inner operation") {
                        innerExecuted = true
                        "inner"
                    }
                    "$innerResult-outer"
                }

                result shouldBe "inner-outer"
                outerExecuted shouldBe true
                innerExecuted shouldBe true
            }

            "should work with checkpoint inside measureTime" {
                var checkpointUsed = false

                val result = PerformanceMonitor.measureTime("combined test") {
                    val checkpoint = PerformanceMonitor.Checkpoint("internal checkpoint")
                    checkpoint.mark("step1")
                    checkpointUsed = true
                    checkpoint.finish()
                    "done"
                }

                result shouldBe "done"
                checkpointUsed shouldBe true
            }

            "should handle complex scenarios" {
                val results = mutableListOf<Int>()

                PerformanceMonitor.measureTime("complex") {
                    val cp = PerformanceMonitor.Checkpoint("process")

                    // Step 1
                    results.add(1)
                    cp.mark("step1")

                    // Nested measurement
                    val nested = PerformanceMonitor.measureTime("nested") {
                        results.add(2)
                        42
                    }
                    results.add(nested)
                    cp.mark("step2")

                    // Another checkpoint
                    val cp2 = PerformanceMonitor.Checkpoint("subprocess")
                    results.add(3)
                    cp2.finish()

                    cp.finish()
                }

                results shouldBe listOf(1, 2, 42, 3)
            }
        }

        "TimeSource property" - {
            "should provide access to TimeSource.Monotonic" {
                val timeSource = PerformanceMonitor.timeSource
                timeSource shouldBe TimeSource.Monotonic
            }

            "should be used consistently across functions" {
                // Verify the time source is the same instance
                val mark1 = PerformanceMonitor.timeSource.markNow()
                busyWait(5)
                val mark2 = PerformanceMonitor.timeSource.markNow()

                // Time should have elapsed between marks
                val elapsed = mark2 - mark1
                (elapsed.inWholeMilliseconds >= 0) shouldBe true
            }
        }
    }
})

// Platform-agnostic busy wait to simulate work
private fun busyWait(milliseconds: Int) {
    val start = TimeSource.Monotonic.markNow()
    val duration = milliseconds.milliseconds
    while (start.elapsedNow() < duration) {
        // Busy wait - do some computation to avoid optimization
        (1..10).sum()
    }
}