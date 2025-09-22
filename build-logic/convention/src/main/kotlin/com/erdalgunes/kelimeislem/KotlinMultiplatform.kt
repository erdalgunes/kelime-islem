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
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

/**
 * Configure Kotlin Multiplatform for shared modules (Android + JS targets)
 */
@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    
    // Android target
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        // Enable instrumented tests
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
    
    // JavaScript target for web
    js(IR) {
        binaries.executable()
        browser {
            runTask {
                devServerProperty.set(
                    org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer(
                        open = true,
                        port = 8080
                    )
                )
            }
        }
        nodejs()
    }
    
    // Configure common compiler options
    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
                    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
            }
        }
    }
    
    // Common source sets
    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
            }
        }
        
        commonTest {
            dependencies {
                implementation(libs.findLibrary("kotlin.test").get())
                implementation(libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
        
        androidMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
        
        getByName("jsMain") {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
            }
        }
    }
}