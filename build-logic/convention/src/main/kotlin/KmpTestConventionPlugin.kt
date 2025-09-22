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

import com.erdalgunes.kelimeislem.configureKotest
import com.erdalgunes.kelimeislem.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin.test").get())
                        implementation(libs.findBundle("kotest.common").get())
                        implementation(libs.findLibrary("kotlinx.coroutines.test").get())
                        implementation(libs.findLibrary("turbine").get())
                    }
                    
                    // Android test dependencies will be added when android target exists
                    findByName("androidUnitTest")?.dependencies {
                        implementation(libs.findBundle("kotest.jvm").get())
                        implementation(libs.findLibrary("mockk.android").get())
                        implementation(libs.findLibrary("robolectric").get())
                    }
                    
                    jvmTest {
                        dependencies {
                            implementation(libs.findBundle("kotest.jvm").get())
                            implementation(libs.findLibrary("kotest.runner.junit5").get())
                            implementation(libs.findLibrary("kotest.framework.datatest").get())
                        }
                    }
                }
            }
            
            // Configure Kotest for JVM tests
            configureKotest()
        }
    }
}