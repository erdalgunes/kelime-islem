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

import com.erdalgunes.kelimeislem.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kelimeislem.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }
            
            val compose = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    getByName("commonMain") {
                        dependencies {
                            implementation(compose.dependencies.runtime)
                            implementation(compose.dependencies.foundation)
                            implementation(compose.dependencies.material3)
                            implementation(compose.dependencies.ui)
                            implementation(compose.dependencies.components.resources)
                            implementation(compose.dependencies.components.uiToolingPreview)
                            implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
                            implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                            implementation(libs.findLibrary("androidx.navigation.compose").get())
                        }
                    }
                    
                    getByName("androidMain") {
                        dependencies {
                            implementation(compose.dependencies.preview)
                            implementation(libs.findLibrary("androidx.activity.compose").get())
                        }
                    }
                    
                    // Add UI testing support for common tests
                    findByName("commonTest")?.apply {
                        dependencies {
                            @OptIn(ExperimentalComposeLibrary::class)
                            implementation(compose.dependencies.uiTestJUnit4)
                        }
                    }
                    
                    // Add Android-specific UI testing support
                    findByName("androidUnitTest")?.apply {
                        dependencies {
                            implementation(libs.findLibrary("compose.ui.test").get())
                        }
                    }
                }
            }
        }
    }
}