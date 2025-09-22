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

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kelimeislem.compose.multiplatform)
    id("kelimeislem.kmp.test")
    id("kelimeislem.test.coverage")
}

kotlin {
    sourceSets {
        // Additional dependencies can be added here if needed
        commonMain.dependencies {
            // All compose dependencies are handled by the convention plugin
        }
    }
}

android {
    namespace = "com.erdalgunes.kelimeislem.composeapp"
}