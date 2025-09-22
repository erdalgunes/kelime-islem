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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.erdalgunes.kelimeislem.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    
    // Add compose multiplatform gradle plugin for convention plugins
    compileOnly("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.compose.get()}")
    
    // Add Kover plugin for test coverage
    compileOnly("org.jetbrains.kotlinx:kover-gradle-plugin:${libs.versions.kover.get()}")
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "kelimeislem.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "kelimeislem.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("kmpApplication") {
            id = "kelimeislem.kmp.application"
            implementationClass = "KmpApplicationConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "kelimeislem.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("androidApplication") {
            id = "kelimeislem.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "kelimeislem.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "kelimeislem.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kmpTest") {
            id = "kelimeislem.kmp.test"
            implementationClass = "KmpTestConventionPlugin"
        }
        register("androidUnitTest") {
            id = "kelimeislem.android.test"
            implementationClass = "AndroidUnitTestConventionPlugin"
        }
        register("testCoverage") {
            id = "kelimeislem.test.coverage"
            implementationClass = "TestCoverageConventionPlugin"
        }
    }
}