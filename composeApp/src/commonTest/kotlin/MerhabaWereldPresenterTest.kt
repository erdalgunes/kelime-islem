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

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MerhabaWereldPresenterTest {
    
    @Test
    fun `initial state should have correct default values`() = runTest {
        val presenter = MerhabaWereldPresenter()
        val state = presenter.state.value
        
        assertEquals("Merhaba Wereld", state.title)
        assertEquals("Built with Kotlin Multiplatform", state.subtitle)
        assertEquals(0, state.clickCount)
    }
    
    @Test
    fun `clicking should increment count`() = runTest {
        val presenter = MerhabaWereldPresenter()
        
        presenter.handleEvent(MerhabaWereldEvent.Clicked)
        assertEquals(1, presenter.state.value.clickCount)
        
        presenter.handleEvent(MerhabaWereldEvent.Clicked)
        assertEquals(2, presenter.state.value.clickCount)
    }
    
    @Test
    fun `reset should set count to zero`() = runTest {
        val presenter = MerhabaWereldPresenter()
        
        // Click multiple times
        repeat(5) {
            presenter.handleEvent(MerhabaWereldEvent.Clicked)
        }
        assertEquals(5, presenter.state.value.clickCount)
        
        // Reset
        presenter.handleEvent(MerhabaWereldEvent.Reset)
        assertEquals(0, presenter.state.value.clickCount)
    }
    
    @Test
    fun `title and subtitle should remain constant during interactions`() = runTest {
        val presenter = MerhabaWereldPresenter()
        val initialTitle = presenter.state.value.title
        val initialSubtitle = presenter.state.value.subtitle
        
        presenter.handleEvent(MerhabaWereldEvent.Clicked)
        presenter.handleEvent(MerhabaWereldEvent.Reset)
        presenter.handleEvent(MerhabaWereldEvent.Clicked)
        
        assertEquals(initialTitle, presenter.state.value.title)
        assertEquals(initialSubtitle, presenter.state.value.subtitle)
    }
}