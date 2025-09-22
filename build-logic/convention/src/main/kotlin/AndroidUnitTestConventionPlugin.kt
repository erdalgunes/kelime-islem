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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.erdalgunes.kelimeislem.configureKotest
import com.erdalgunes.kelimeislem.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidUnitTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure for both library and application modules
            pluginManager.withPlugin("com.android.library") {
                configureAndroidTests<LibraryExtension>()
            }
            
            pluginManager.withPlugin("com.android.application") {
                configureAndroidTests<ApplicationExtension>()
            }
        }
    }
    
    private inline fun <reified T> Project.configureAndroidTests() where T : com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *> {
        extensions.configure<T> {
            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
            }
        }
        
        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("kotlin.test").get())
            add("testImplementation", libs.findBundle("kotest.common").get())
            add("testImplementation", libs.findBundle("kotest.jvm").get())
            add("testImplementation", libs.findLibrary("kotest.runner.junit5").get())
            add("testImplementation", libs.findLibrary("mockk.android").get())
            add("testImplementation", libs.findLibrary("robolectric").get())
            add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            add("testImplementation", libs.findLibrary("turbine").get())
            
            // Android Test dependencies
            add("androidTestImplementation", libs.findBundle("android.test").get())
            add("androidTestImplementation", libs.findLibrary("espresso.core").get())
            add("androidTestImplementation", libs.findLibrary("compose.ui.test").get())
        }
        
        // Configure Kotest for unit tests
        configureKotest()
    }
}