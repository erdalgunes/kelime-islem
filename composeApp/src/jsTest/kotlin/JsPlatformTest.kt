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

import com.erdalgunes.kelimeislem.getPlatform
import currentTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsPlatformTest {
    
    @Test
    fun `platform should return JS platform information`() {
        val platform = getPlatform()
        assertEquals("Web with Kotlin/JS", platform.name)
    }
    
    @Test
    fun `currentTimeMillis should return valid timestamp`() {
        val time1 = currentTimeMillis()
        val time2 = currentTimeMillis()
        
        // Should return positive values
        assertTrue(time1 > 0, "Time should be positive")
        assertTrue(time2 > 0, "Time should be positive")
        
        // Time should progress (or stay the same if calls are very fast)
        assertTrue(time2 >= time1, "Time should not go backwards")
    }
    
    @Test
    fun `currentTimeMillis should be consistent with system time`() {
        val time = currentTimeMillis()
        
        // Should be a reasonable timestamp (after year 2020)
        val year2020 = 1577836800000L // 2020-01-01 00:00:00 UTC
        assertTrue(time > year2020, "Time should be after 2020")
        
        // Should be before year 2100 (reasonable upper bound)
        val year2100 = 4102444800000L // 2100-01-01 00:00:00 UTC
        assertTrue(time < year2100, "Time should be before 2100")
    }
}