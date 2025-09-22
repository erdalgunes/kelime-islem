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

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class TestCoverageConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlinx.kover")
            
            configure<KoverProjectExtension> {
                // Configure coverage reports
                reports {
                    // Configure filters for all variants
                    filters {
                        excludes {
                            // Exclude Android generated code
                            classes(
                                "*.R",
                                "*.R$*",
                                "*.BuildConfig",
                                "*.Manifest*",
                                "*.BR",
                                "*Binding",
                                "*BindingImpl"
                            )
                            
                            // Exclude Compose generated code and UI
                            classes(
                                "*.ComposableKt*",
                                "*ComposableSingletons*",
                                "*AppKt*",  // Exclude App.kt and its generated classes
                                "*Kt$*"     // Exclude Kotlin file classes with composables
                            )
                            
                            // Exclude UI-only files
                            annotatedBy(
                                "*Preview",
                                "*Composable"
                            )
                            
                            // Exclude DI generated code
                            classes(
                                "*_Factory",
                                "*_Impl",
                                "*_HiltModules*",
                                "*_MembersInjector",
                                "hilt_aggregated_deps.*",
                                "dagger.hilt.*"
                            )
                            
                            // Exclude test code
                            packages(
                                "*.test",
                                "*.androidTest",
                                "*.commonTest",
                                "*.jvmTest",
                                "kelime_islem.composeapp.generated.*",
                                "*.ui.theme"  // Exclude theme-related UI code
                            )
                        }
                    }
                    
                    // Configure total variant (all classes)
                    total {
                        // HTML report configuration
                        html {
                            title.set("Kelime İşlem Coverage Report")
                            onCheck.set(true)
                        }
                        
                        // XML report configuration  
                        xml {
                            onCheck.set(true)
                        }
                        
                        // Verification rules - incrementally increasing
                        verify {
                            onCheck.set(true)
                            
                            // Increased to 50% after improving test coverage
                            rule("Minimum coverage") {
                                // Boy Scout Rule: Leave code better than you found it
                                // Current: 52.5%, Target: 80%
                                minBound(50)
                            }
                        }
                        
                        // Log coverage to console
                        log {
                            onCheck.set(true)
                            header.set("📊 Coverage Report:")
                            coverageUnits.set(CoverageUnit.LINE)
                            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
                        }
                    }
                }
            }
        }
    }
}