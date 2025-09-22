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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        MerhabaWereldWebApp()
    }
}

@Composable
fun MerhabaWereldWebApp() {
    val presenter = remember { MerhabaWereldPresenter() }
    val state = presenter.state.collectAsState()
    
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                minHeight(100.vh)
                fontFamily("system-ui", "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto", "sans-serif")
                background("linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                margin(0.px)
                padding(32.px)
            }
        }
    ) {
        H1(
            attrs = {
                style {
                    fontSize(48.px)
                    fontWeight("bold")
                    color(Color.white)
                    marginBottom(16.px)
                    textAlign("center")
                }
            }
        ) {
            Text(state.value.title)
        }
        
        P(
            attrs = {
                style {
                    fontSize(18.px)
                    color(Color("#f0f0f0"))
                    textAlign("center")
                    maxWidth(600.px)
                    marginBottom(24.px)
                }
            }
        ) {
            Text("${state.value.subtitle} & Compose HTML")
        }
        
        if (state.value.clickCount > 0) {
            P(
                attrs = {
                    style {
                        fontSize(16.px)
                        color(Color.white)
                        textAlign("center")
                        marginBottom(16.px)
                    }
                }
            ) {
                Text("Clicked ${state.value.clickCount} times!")
            }
        }
        
        Button(
            attrs = {
                style {
                    fontSize(16.px)
                    padding(12.px, 24.px)
                    backgroundColor(Color("#4CAF50"))
                    color(Color.white)
                    border("none")
                    borderRadius(8.px)
                    cursor("pointer")
                    marginRight(8.px)
                }
                onClick { presenter.handleEvent(MerhabaWereldEvent.Clicked) }
            }
        ) {
            Text("Click Me!")
        }
        
        if (state.value.clickCount > 0) {
            Button(
                attrs = {
                    style {
                        fontSize(16.px)
                        padding(12.px, 24.px)
                        backgroundColor(Color("#f44336"))
                        color(Color.white)
                        border("none")
                        borderRadius(8.px)
                        cursor("pointer")
                        marginLeft(8.px)
                    }
                    onClick { presenter.handleEvent(MerhabaWereldEvent.Reset) }
                }
            ) {
                Text("Reset")
            }
        }
    }
}