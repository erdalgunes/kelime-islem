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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MerhabaWereldState(
    val title: String = "Merhaba Wereld",
    val subtitle: String = "Built with Kotlin Multiplatform",
    val clickCount: Int = 0
)

sealed interface MerhabaWereldEvent {
    data object Clicked : MerhabaWereldEvent
    data object Reset : MerhabaWereldEvent
}

class MerhabaWereldPresenter {
    private val _state = MutableStateFlow(MerhabaWereldState())
    val state: StateFlow<MerhabaWereldState> = _state.asStateFlow()
    
    fun handleEvent(event: MerhabaWereldEvent) {
        when (event) {
            is MerhabaWereldEvent.Clicked -> {
                _state.value = _state.value.copy(
                    clickCount = _state.value.clickCount + 1
                )
            }
            is MerhabaWereldEvent.Reset -> {
                _state.value = _state.value.copy(clickCount = 0)
            }
        }
    }
}