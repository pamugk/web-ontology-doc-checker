package ru.psu.web_ontology_doc_checker

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.dialog.*
import com.ccfraser.muirwik.components.form.MFormControlVariant
import kotlinx.css.LinearDimension
import kotlinx.css.flex
import kotlinx.css.marginLeft
import kotlinx.html.InputType
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
        fun handleCloseDialog() {
            setState { showDialog = false}
        }

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
        mDialog(state.showDialog, onClose =  { _, _ -> handleCloseDialog() }) {
            mDialogTitle("Настройки")
            mDialogContent {
                mDialogContentText("""
                    Основные настройки:
                    N - семантическое расстояние между вершинами онтологии;
                    K - количество предложений между понятиями в исходном тексте.
                """.trimIndent())
                mTextField("N",
                    onChange = {
                        val value = it.targetInputValue.toIntOrNull() ?: state.N
                        if (value in 1..99) setState { N = value }
                    },
                    type = InputType.number,
                    value = state.N.toString(),
                    variant = MFormControlVariant.outlined)
                mTextField("K",
                    onChange = {
                        val value = it.targetInputValue.toIntOrNull() ?: state.K
                        if (value in 1..99) setState { K = value }
                    },
                    type = InputType.number,
                    value = state.K.toString(),
                    variant = MFormControlVariant.outlined)
            }
        }
    }
}
