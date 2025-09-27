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

package com.erdalgunes.kelimeislem.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Update checker that follows Obtanium's approach for version detection
 * and automatic updates from GitHub releases
 */
class UpdateChecker(
    private val currentVersion: String = BuildConfig.VERSION_NAME,
    private val currentVersionCode: Int = BuildConfig.VERSION_CODE
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: Flow<UpdateState> = _updateState.asStateFlow()

    // API endpoints
    companion object {
        const val GITHUB_API = "https://api.github.com/repos/erdalgunes/kelime-islem"
        const val UPDATE_API = "https://erdalgunes.github.io/kelime-islem/update.json"
        const val VERSION_API = "https://erdalgunes.github.io/kelime-islem/version.json"
        const val RELEASES_URL = "https://github.com/erdalgunes/kelime-islem/releases"
    }

    /**
     * Check for updates using Obtanium-style detection
     */
    suspend fun checkForUpdates() = withContext(Dispatchers.IO) {
        _updateState.value = UpdateState.Checking

        try {
            // First try the GitHub Pages API (faster, cached)
            val updateInfo = fetchUpdateInfo()

            if (updateInfo != null) {
                val hasUpdate = isNewerVersion(
                    currentVersion = currentVersion,
                    latestVersion = updateInfo.latestVersion.version
                )

                _updateState.value = if (hasUpdate) {
                    UpdateState.UpdateAvailable(updateInfo)
                } else {
                    UpdateState.NoUpdate
                }
            } else {
                // Fallback to GitHub API
                checkViaGitHubApi()
            }
        } catch (e: Exception) {
            _updateState.value = UpdateState.Error(e.message ?: "Unknown error")
        }
    }

    /**
     * Check for updates directly via GitHub API (fallback)
     */
    private suspend fun checkViaGitHubApi() {
        try {
            val release = fetchLatestRelease()

            if (release != null) {
                val hasUpdate = isNewerVersion(
                    currentVersion = currentVersion,
                    latestVersion = release.tagName.removePrefix("v")
                )

                val updateInfo = ObtaniumUpdateInfo(
                    app = AppInfo(
                        id = "com.erdalgunes.kelimeislem",
                        name = "Kelime İşlem",
                        author = "Erdal Güneş",
                        source = "GitHub"
                    ),
                    latestVersion = VersionInfo(
                        version = release.tagName.removePrefix("v"),
                        versionCode = 0, // Will be extracted from assets
                        releaseDate = release.publishedAt,
                        apkUrl = release.assets.firstOrNull {
                            it.name.endsWith(".apk")
                        }?.browserDownloadUrl ?: "",
                        sha256 = "",
                        fileSize = release.assets.firstOrNull {
                            it.name.endsWith(".apk")
                        }?.size ?: 0,
                        minSdkVersion = 24,
                        targetSdkVersion = 36
                    ),
                    updateInfo = UpdateInfo(
                        mandatory = false,
                        changelog = release.body,
                        releaseUrl = release.htmlUrl
                    ),
                    obtanium = ObtaniumConfig(
                        appId = "com.erdalgunes.kelimeislem",
                        sourceUrl = "https://github.com/erdalgunes/kelime-islem",
                        preferredApkIndex = 0,
                        useLatestTag = true,
                        fallbackToOlderReleases = true,
                        filterAssets = true,
                        filterRegex = ".*\\.apk$",
                        versionDetection = "standardVersioning",
                        apkFilterRegex = ".*release.*\\.apk$",
                        autoApkFilterByArch = true,
                        appName = "Kelime İşlem",
                        exemptFromBackgroundUpdates = false,
                        skipUpdateNotifications = false,
                        about = "Turkish word game - Bir Kelime Bir İşlem"
                    )
                )

                _updateState.value = if (hasUpdate) {
                    UpdateState.UpdateAvailable(updateInfo)
                } else {
                    UpdateState.NoUpdate
                }
            } else {
                _updateState.value = UpdateState.NoUpdate
            }
        } catch (e: Exception) {
            _updateState.value = UpdateState.Error(e.message ?: "Failed to check GitHub API")
        }
    }

    /**
     * Fetch update information from GitHub Pages API
     */
    private suspend fun fetchUpdateInfo(): ObtaniumUpdateInfo? {
        // This would be implemented with actual HTTP client
        // For now, returning null to trigger GitHub API fallback
        return null
    }

    /**
     * Fetch latest release from GitHub API
     */
    private suspend fun fetchLatestRelease(): GitHubRelease? {
        // This would be implemented with actual HTTP client
        // Placeholder implementation
        return null
    }

    /**
     * Compare version strings (semantic versioning)
     */
    private fun isNewerVersion(currentVersion: String, latestVersion: String): Boolean {
        val current = parseVersion(currentVersion)
        val latest = parseVersion(latestVersion)

        return when {
            latest.major > current.major -> true
            latest.major < current.major -> false
            latest.minor > current.minor -> true
            latest.minor < current.minor -> false
            latest.patch > current.patch -> true
            else -> false
        }
    }

    /**
     * Parse semantic version string
     */
    private fun parseVersion(version: String): SemanticVersion {
        val parts = version.split(".")
        return SemanticVersion(
            major = parts.getOrNull(0)?.toIntOrNull() ?: 0,
            minor = parts.getOrNull(1)?.toIntOrNull() ?: 0,
            patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
        )
    }

    /**
     * Download and install update (Android only)
     */
    suspend fun downloadAndInstallUpdate(updateInfo: ObtaniumUpdateInfo) {
        _updateState.value = UpdateState.Downloading(0)

        // Platform-specific implementation would go here
        // This would download the APK and trigger installation
    }

    /**
     * Get Obtanium configuration URL for easy app addition
     */
    fun getObtaniumUrl(): String {
        return "obtanium://add/${GITHUB_API.replace("https://", "")}"
    }
}

/**
 * Update state
 */
sealed class UpdateState {
    object Idle : UpdateState()
    object Checking : UpdateState()
    object NoUpdate : UpdateState()
    data class UpdateAvailable(val info: ObtaniumUpdateInfo) : UpdateState()
    data class Downloading(val progress: Int) : UpdateState()
    data class Error(val message: String) : UpdateState()
}

/**
 * Semantic version representation
 */
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int
)

/**
 * Obtanium-compatible update information
 */
@Serializable
data class ObtaniumUpdateInfo(
    val app: AppInfo,
    val latestVersion: VersionInfo,
    val updateInfo: UpdateInfo,
    val obtanium: ObtaniumConfig
)

@Serializable
data class AppInfo(
    val id: String,
    val name: String,
    val author: String,
    val source: String
)

@Serializable
data class VersionInfo(
    val version: String,
    val versionCode: Int,
    val releaseDate: String,
    val apkUrl: String,
    val sha256: String,
    val fileSize: Long,
    val minSdkVersion: Int,
    val targetSdkVersion: Int
)

@Serializable
data class UpdateInfo(
    val mandatory: Boolean,
    val changelog: String,
    val releaseUrl: String
)

@Serializable
data class ObtaniumConfig(
    val appId: String,
    val sourceUrl: String,
    val preferredApkIndex: Int,
    val useLatestTag: Boolean,
    val fallbackToOlderReleases: Boolean,
    val filterAssets: Boolean,
    val filterRegex: String,
    val versionDetection: String,
    val apkFilterRegex: String,
    val autoApkFilterByArch: Boolean,
    val appName: String,
    val exemptFromBackgroundUpdates: Boolean,
    val skipUpdateNotifications: Boolean,
    val about: String
)

/**
 * GitHub Release model
 */
@Serializable
data class GitHubRelease(
    val tagName: String,
    val name: String,
    val body: String,
    val publishedAt: String,
    val htmlUrl: String,
    val assets: List<GitHubAsset>
)

@Serializable
data class GitHubAsset(
    val name: String,
    val size: Long,
    val browserDownloadUrl: String
)

/**
 * BuildConfig placeholder (will be generated by build system)
 */
object BuildConfig {
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1
}