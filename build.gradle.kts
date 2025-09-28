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
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover) apply false
    id("kelimeislem.test.coverage")
    id("org.sonarqube") version "5.1.0.4882"
    // Supply chain security - SBOM generation
    id("org.cyclonedx.bom") version "1.9.0"
    // Dependency vulnerability scanning
    id("org.owasp.dependencycheck") version "11.1.0"
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
        property("sonar.sources", "composeApp/src/commonMain,composeApp/src/androidMain,design-system/src/commonMain")
        property("sonar.tests", "composeApp/src/commonTest,composeApp/src/androidUnitTest")
        property("sonar.java.binaries", "composeApp/build/classes")
        
        // Language configuration
        property("sonar.language", "kotlin")
        property("sonar.kotlin.source.version", "1.9")
        property("sonar.kotlin.target.version", "17")
        
        // Coverage configuration
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/composeApp/build/reports/kover/report.xml")
        
        // Test results
        property("sonar.junit.reportPaths", "composeApp/build/test-results/testDebugUnitTest")
        
        // Exclusions - UI files, generated code, and design-system from coverage
        property("sonar.exclusions", "**/*Test.kt,**/*Spec.kt,**/test/**,**/androidTest/**,**/commonTest/**,**/ui/**,**/*Screen.kt,**/App.kt,**/build/**,**/*.gradle.kts,**/buildSrc/**,**/build-logic/**,**/design-system/**")
        
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

// Ensure SonarQube runs after tests and coverage
tasks.named("sonar") {
    dependsOn("koverXmlReport")
}

// Configure Kover for multi-module project
dependencies {
    // Merge coverage from all modules
    kover(project(":composeApp"))
    // TODO: Add androidApp once it has tests
    // kover(project(":androidApp"))
}

// SBOM (Software Bill of Materials) Configuration
afterEvaluate {
    tasks.named("cyclonedxBom").configure {
        enabled = true
    }
}

// Dependency vulnerability scanning configuration
afterEvaluate {
    tasks.named("dependencyCheckAnalyze").configure {
        enabled = true
    }
}