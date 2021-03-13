package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.input.mInputLabel
import com.ccfraser.muirwik.components.mNativeSelect
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import com.ccfraser.muirwik.components.targetValue
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.html.InputType
import react.RBuilder
import react.dom.option
import styled.css

fun RBuilder.settingsDialog(
    b: Int, K: Int, N: Int, showDialog: Boolean, onClose: ()->Unit,
    onbChange: (Int)->Unit, onNChange: (Int)->Unit, onKChange: (Int)->Unit) =
    mDialog(showDialog, onClose =  { _, _ -> onClose.invoke() }) {
        mDialogTitle("Настройки")
        mDialogContent {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            mDialogContentText(
                """
                        Основные настройки:
                        b - пороговое значение для веса дуг;
                        N - семантическое расстояние между вершинами онтологии;
                        K - количество предложений между понятиями в исходном тексте.
                    """.trimIndent()
            )
            mFormControl(variant = MFormControlVariant.outlined) {
                mInputLabel("b", htmlFor = "bNativeSelect")
                mNativeSelect(b.toString(), id = "bNativeSelect", onChange = { e, _ -> onbChange((e.targetValue as String).toInt()) }) {
                    option(content = "1")
                    option(content = "2")
                    option(content = "5")
                    option(content = "10")
                }
            }
            mFormControl {
                mTextField(
                    "N",
                    onChange = {
                        val value = it.targetInputValue.toIntOrNull() ?: N
                        if (value in 1..99 && value != N) onNChange.invoke(value)
                    },
                    type = InputType.number,
                    value = N.toString(),
                    variant = MFormControlVariant.outlined
                )
            }
            mFormControl {
                mTextField(
                    "K",
                    onChange = {
                        val value = it.targetInputValue.toIntOrNull() ?: K
                        if (value in 1..99 && value != K) onKChange.invoke(value)
                    },
                    type = InputType.number,
                    value = K.toString(),
                    variant = MFormControlVariant.outlined
                )
            }
        }
    }