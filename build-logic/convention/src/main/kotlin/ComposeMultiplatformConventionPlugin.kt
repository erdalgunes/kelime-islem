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
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
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

            // Apply Compose BOM at project level for all configurations
            dependencies {
                val bom = libs.findLibrary("compose-bom")
                if (bom.isPresent) {
                    add("implementation", platform(bom.get()))
                }
            }

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
                            // Note: material-icons-extended is Android/JVM only, added to specific platforms below
                        }
                    }

                    getByName("androidMain") {
                        dependencies {
                            implementation(compose.dependencies.preview)
                            implementation(libs.findLibrary("androidx.activity.compose").get())
                            implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
                            implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                            implementation(libs.findLibrary("androidx.navigation.compose").get())
                            implementation(libs.findLibrary("compose-material-icons-extended").get())
                        }
                    }
                    
                    // Add desktop-specific Compose HTML dependencies
                    findByName("desktopMain")?.apply {
                        dependencies {
                            implementation(libs.findLibrary("compose-material-icons-extended").get())
                        }
                    }
                    
                    // Add Android-specific UI testing support only
                    findByName("androidUnitTest")?.apply {
                        dependencies {
                            implementation(libs.findLibrary("compose.ui.test").get())
                        }
                    }
                    
                    // Add JS-specific Compose HTML dependencies
                    findByName("jsMain")?.apply {
                        dependencies {
                            implementation(compose.dependencies.html.core)
                        }
                    }
                }
            }
        }
    }
}