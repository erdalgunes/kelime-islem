package com.kelimeislem.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeLessThan
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
                val capturedOutput = captureOutput {
                    try {
                        PerformanceMonitor.measureTime("failing operation") {
                            throw RuntimeException("test exception")
                        }
                    } catch (e: RuntimeException) {
                        // Expected exception
                    }
                }
                // Should still log performance even if exception occurs
                capturedOutput shouldContain "failing operation"
            }

            "should handle empty block" {
                val result = PerformanceMonitor.measureTime("empty operation") {
                    // Empty block
                }
                result shouldBe Unit
            }

            "should measure fast operations correctly" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.measureTime("fast operation") {
                        // Quick operation
                        val sum = (1..100).sum()
                        sum
                    }
                }
                capturedOutput shouldContain "✅ FAST"
                capturedOutput shouldContain "fast operation"
            }

            "should measure operations with computation" {
                val result = PerformanceMonitor.measureTime("computation") {
                    (1..1000).map { it * 2 }.sum()
                }
                result shouldBe 1001000 // sum of 2,4,6...2000
            }
        }

        "logPerformance function" - {

            "should log FAST for durations less than 100ms" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("quick task", 50.milliseconds)
                }
                capturedOutput shouldContain "✅ FAST"
                capturedOutput shouldContain "quick task"
                capturedOutput shouldContain "50ms"
            }

            "should log INFO for durations between 100ms and 1000ms" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("medium task", 500.milliseconds)
                }
                capturedOutput shouldContain "ℹ️ INFO"
                capturedOutput shouldContain "medium task"
                capturedOutput shouldContain "500ms"
            }

            "should log SLOW for durations greater than 1000ms" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("slow task", 1500.milliseconds)
                }
                capturedOutput shouldContain "⚠️ SLOW"
                capturedOutput shouldContain "slow task"
                capturedOutput shouldContain "1s"
            }

            "should handle boundary value at exactly 100ms" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("boundary 100", 100.milliseconds)
                }
                capturedOutput shouldContain "✅ FAST"
                capturedOutput shouldContain "100ms"
            }

            "should handle boundary value at exactly 1000ms" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("boundary 1000", 1000.milliseconds)
                }
                capturedOutput shouldContain "ℹ️ INFO"
                capturedOutput shouldContain "1000ms"
            }

            "should handle very small durations" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("tiny task", 1.milliseconds)
                }
                capturedOutput shouldContain "✅ FAST"
                capturedOutput shouldContain "1ms"
            }

            "should handle zero duration" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("instant task", Duration.ZERO)
                }
                capturedOutput shouldContain "✅ FAST"
                capturedOutput shouldContain "0ms"
            }

            "should handle very large durations" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.logPerformance("huge task", 5000.milliseconds)
                }
                capturedOutput shouldContain "⚠️ SLOW"
                capturedOutput shouldContain "5s"
            }
        }

        "Checkpoint class" - {

            "should create checkpoint and track single stage" {
                val capturedOutput = captureOutput {
                    val checkpoint = PerformanceMonitor.Checkpoint("test operation")
                    // Simulate some work
                    Thread.sleep(10)
                    checkpoint.finish()
                }
                capturedOutput shouldContain "test operation - Total"
            }

            "should track multiple stages with mark" {
                val capturedOutput = captureOutput {
                    val checkpoint = PerformanceMonitor.Checkpoint("multi-stage")

                    // Stage 1
                    Thread.sleep(10)
                    checkpoint.mark("stage1")

                    // Stage 2
                    Thread.sleep(10)
                    checkpoint.mark("stage2")

                    // Final
                    Thread.sleep(10)
                    checkpoint.finish()
                }

                capturedOutput shouldContain "multi-stage - stage1"
                capturedOutput shouldContain "multi-stage - stage2"
                capturedOutput shouldContain "multi-stage - Total"
            }

            "should handle immediate finish after creation" {
                val capturedOutput = captureOutput {
                    val checkpoint = PerformanceMonitor.Checkpoint("instant")
                    checkpoint.finish()
                }
                capturedOutput shouldContain "instant - Total"
                capturedOutput shouldContain "✅ FAST"
            }

            "should handle multiple marks in sequence" {
                val capturedOutput = captureOutput {
                    val checkpoint = PerformanceMonitor.Checkpoint("sequential")

                    checkpoint.mark("first")
                    checkpoint.mark("second")
                    checkpoint.mark("third")
                    checkpoint.finish()
                }

                capturedOutput shouldContain "sequential - first"
                capturedOutput shouldContain "sequential - second"
                capturedOutput shouldContain "sequential - third"
                capturedOutput shouldContain "sequential - Total"
            }

            "should measure accurate time between marks" {
                val checkpoint = PerformanceMonitor.Checkpoint("timing test")

                val capturedFirst = captureOutput {
                    Thread.sleep(50)
                    checkpoint.mark("after 50ms")
                }

                // The logged time should be around 50ms
                capturedFirst shouldContain "✅ FAST"

                val capturedSecond = captureOutput {
                    Thread.sleep(150)
                    checkpoint.mark("after 150ms more")
                }

                // This stage took 150ms, should be INFO
                capturedSecond shouldContain "ℹ️ INFO"

                val capturedFinal = captureOutput {
                    checkpoint.finish()
                }

                // Total should be at least 200ms (50+150)
                capturedFinal shouldContain "timing test - Total"
            }

            "should reset timer after each mark" {
                val checkpoint = PerformanceMonitor.Checkpoint("reset test")

                // First mark after minimal time
                val captured1 = captureOutput {
                    checkpoint.mark("immediate")
                }
                captured1 shouldContain "✅ FAST"

                // Wait and mark again
                Thread.sleep(50)
                val captured2 = captureOutput {
                    checkpoint.mark("after delay")
                }
                captured2 shouldContain "✅ FAST"

                // Finish immediately
                val captured3 = captureOutput {
                    checkpoint.finish()
                }
                // Time since last mark should be minimal
                captured3 shouldContain "✅ FAST"
            }

            "should handle checkpoint with only finish (no marks)" {
                val capturedOutput = captureOutput {
                    val checkpoint = PerformanceMonitor.Checkpoint("no marks")
                    Thread.sleep(20)
                    checkpoint.finish()
                }
                capturedOutput shouldContain "no marks - Total"
                capturedOutput shouldContain "✅ FAST"
            }
        }

        "Integration tests" - {

            "should work correctly with nested measurements" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.measureTime("outer operation") {
                        val innerResult = PerformanceMonitor.measureTime("inner operation") {
                            Thread.sleep(10)
                            "inner"
                        }
                        Thread.sleep(10)
                        "$innerResult-outer"
                    }
                }

                capturedOutput shouldContain "inner operation"
                capturedOutput shouldContain "outer operation"
            }

            "should work with checkpoint inside measureTime" {
                val capturedOutput = captureOutput {
                    PerformanceMonitor.measureTime("combined test") {
                        val checkpoint = PerformanceMonitor.Checkpoint("internal checkpoint")
                        Thread.sleep(10)
                        checkpoint.mark("step1")
                        Thread.sleep(10)
                        checkpoint.finish()
                    }
                }

                capturedOutput shouldContain "internal checkpoint - step1"
                capturedOutput shouldContain "internal checkpoint - Total"
                capturedOutput shouldContain "combined test"
            }
        }
    }
})

// Helper function to capture console output
private fun captureOutput(block: () -> Unit): String {
    val originalOut = System.out
    val outputStream = java.io.ByteArrayOutputStream()
    val printStream = java.io.PrintStream(outputStream)

    try {
        System.setOut(printStream)
        block()
        printStream.flush()
        return outputStream.toString()
    } finally {
        System.setOut(originalOut)
    }
}