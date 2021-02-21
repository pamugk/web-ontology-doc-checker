package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.input.mInput
import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.html.InputType
import react.*
import styled.css

class AppState(
    var K: Int,
    var N: Int,
    var showDialog: Boolean,
    var selectedTab: Any
): RState

class App(props: RProps) : RComponent<RProps, AppState>(props) {

    init {
        state = AppState(
            K = 1,
            N = 10,
            showDialog = false,
            selectedTab = 0
        )
    }

    override fun RBuilder.render() {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов")
                mIconButton("settings", onClick = { setState { showDialog = true } }) {
                    css {
                        marginLeft = LinearDimension.auto
                    }
                }
            }
        }
        mCard {
            css {
                display = Display.flex
                flex(1.0, 1.0)
            }
            mTabs(state.selectedTab, variant = MTabVariant.scrollable, orientation = MTabOrientation.vertical,
                onChange = {_, newTab -> setState { selectedTab = newTab }}) {
                mTab("Документы", value = 0)
                mTab("Обработка", value = 1)
                mTab("Итог", value = 2)
            }
            when (state.selectedTab) {
                0 -> mContainer {
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.column
                    }
                    mIconButton("add_circle_outline", onClick = { openFileDialog({ event ->  }) })
                }
                1 -> {

                }
                2 -> {

                }
            }
        }
        settingsDialog(state.K, state.N, state.showDialog,
            onClose = { setState { showDialog = false}},
            onNChange = { value -> setState { N = value }},
            onKChange = { value -> setState { K = value }})
    }
}
