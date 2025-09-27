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

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover) apply false
    id("kelimeislem.test.coverage")
    id("org.sonarqube") version "5.1.0.4882"
}

// SonarCloud configuration - consolidated from sonar-project.properties
sonarqube {
    properties {
        // Project identification
        property("sonar.projectKey", "erdalgunes_kelime-islem")
        property("sonar.organization", "erdalgunes")
        property("sonar.projectName", "Kelime İşlem")
        property("sonar.projectVersion", "1.0")
        property("sonar.host.url", "https://sonarcloud.io")
        
        // Source code configuration - includes design-system module
        property("sonar.sources", "composeApp/src/commonMain,design-system/src/commonMain")
        property("sonar.tests", "composeApp/src/commonTest,composeApp/src/androidUnitTest")
        property("sonar.java.binaries", "composeApp/build/classes")
        
        // Language configuration
        property("sonar.language", "kotlin")
        property("sonar.kotlin.source.version", "1.9")
        property("sonar.kotlin.target.version", "17")

        // Detekt integration for code quality
        property("sonar.kotlin.detekt.reportPaths", "${projectDir}/build/reports/detekt/detekt.xml")
        
        // Coverage configuration
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/composeApp/build/reports/kover/report.xml")
        
        // Test results
        property("sonar.junit.reportPaths", "composeApp/build/test-results/testDebugUnitTest")
        
        // Exclusions - UI files, generated code, and design-system from coverage
        property("sonar.exclusions", "**/*Test.kt,**/*Spec.kt,**/test/**,**/androidTest/**,**/commonTest/**,**/androidUnitTest/**,**/ui/**,**/*Screen.kt,**/App.kt,**/build/**,**/*.gradle.kts,**/buildSrc/**,**/build-logic/**,**/design-system/**")
        
        // Coverage exclusions (UI components)
        property("sonar.coverage.exclusions", "**/ui/**,**/*Screen.kt,**/App.kt,**/*Activity.kt,**/Platform.*.kt")
        
        // Quality gates and additional settings
        property("sonar.qualitygate.wait", "true")
        property("sonar.sourceEncoding", "UTF-8")
        
        // Project links
        property("sonar.links.homepage", "https://github.com/erdalgunes/kelime-islem")
        property("sonar.links.scm", "https://github.com/erdalgunes/kelime-islem")
        property("sonar.links.issue", "https://github.com/erdalgunes/kelime-islem/issues")
    }
}

// Ensure SonarQube runs after tests, coverage, and Detekt
tasks.named("sonar") {
    dependsOn("koverXmlReport", "detekt")
}

// Configure Detekt for Kotlin Multiplatform
detekt {
    // KMP source sets configuration
    source.setFrom(
        "composeApp/src/commonMain/kotlin",
        "composeApp/src/androidMain/kotlin", 
        "composeApp/src/jsMain/kotlin",
        "design-system/src/commonMain/kotlin"
    )
    
    // Configuration file
    config.setFrom("$projectDir/detekt.yml")
    
    // Baseline for gradual adoption
    baseline = file("$projectDir/detekt-baseline.xml")
    
    // Build upon default config
    buildUponDefaultConfig = true
    
    // Enable all rule sets by default
    allRules = false
    
    // Reports will be configured on individual tasks
}

// Add detekt-rules-compose dependency for Compose-specific rules
dependencies {
    detektPlugins("io.nlopez.compose.rules:detekt:0.4.15")
}

// Configure Detekt task reports
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        sarif.required.set(true)
        md.required.set(false)
        txt.required.set(false)
    }
}

// Configure Kover for multi-module project
dependencies {
    // Merge coverage from all modules
    kover(project(":composeApp"))
    // TODO: Add androidApp once it has tests
    // kover(project(":androidApp"))
}