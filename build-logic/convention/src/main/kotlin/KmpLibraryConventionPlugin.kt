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

import com.android.build.gradle.LibraryExtension
import com.erdalgunes.kelimeislem.configureKotlinMultiplatform
import com.erdalgunes.kelimeislem.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("androidCompileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("androidMinSdk").get().toString().toInt()
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }

            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(this)
                
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("kotlinx.serialization.json").get())
                        }
                    }
                }
            }
        }
    }
}