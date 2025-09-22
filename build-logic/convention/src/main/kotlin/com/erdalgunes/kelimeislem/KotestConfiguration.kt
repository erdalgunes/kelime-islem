/*
 * Copyright 2025 Bir Kelime Bir İşlem Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erdalgunes.kelimeislem

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

/**
 * Configure Kotest for JVM tests
 */
internal fun Project.configureKotest() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        
        // Kotest configuration
        systemProperty("kotest.framework.parallelism", 4)
        systemProperty("kotest.framework.timeout", 10000)
        systemProperty("kotest.framework.invocation.timeout", 5000)
        
        // Test tags filtering
        val includeTags = providers.gradleProperty("kotest.tags.include").orNull
        val excludeTags = providers.gradleProperty("kotest.tags.exclude").orNull
        
        if (includeTags != null) {
            systemProperty("kotest.tags.include", includeTags)
        }
        if (excludeTags != null) {
            systemProperty("kotest.tags.exclude", excludeTags)
        }
        
        // Test reporting
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
        
        // Logging
        testLogging {
            events("passed", "skipped", "failed", "standardError")
            showExceptions = true
            showStackTraces = true
            showCauses = true
        }
        
        // Memory settings
        maxHeapSize = "2g"
        
        // Fail fast
        failFast = providers.gradleProperty("test.failFast").map { it.toBoolean() }.orElse(false).get()
    }
}