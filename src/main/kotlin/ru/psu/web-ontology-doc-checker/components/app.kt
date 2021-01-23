package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.MAppBarPosition
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mToolbar
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.LinearDimension
import kotlinx.css.flex
import kotlinx.css.marginLeft
import react.*
import styled.css

class AppState(
    var K: Int,
    var N: Int,
    var showDialog: Boolean
): RState

class App(props: RProps) : RComponent<RProps, AppState>(props) {

    init {
        state = AppState(
            K = 1,
            N = 10,
            showDialog = false
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
                flex(1.0, 1.0)
            }
        }
        settingsDialog(state.K, state.N, state.showDialog,
            onClose = { setState { showDialog = false}},
            onNChange = { value -> setState { N = value }},
            onKChange = { value -> setState { K = value }})
    }
}
