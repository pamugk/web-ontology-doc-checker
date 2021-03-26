package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.input.mInputLabel
import kotlinx.css.*
import kotlinx.html.InputType
import react.RBuilder
import react.dom.option
import styled.css

fun RBuilder.settingsPanel(
    b: Int, N: Int, K: Int, strict: Boolean,
    onbChange: (Int)->Unit, onNChange: (Int)->Unit, onKChange: (Int)->Unit, onStrictChange: (Boolean)->Unit) =
    mContainer {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        mTypography(
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
                    if (value in 0..99 && value != K) onKChange.invoke(value)
                },
                type = InputType.number,
                value = K.toString(),
                variant = MFormControlVariant.outlined
            )
        }
        mFormControl {
            mInputLabel("Строгое применение \"K\"", htmlFor = "strictSwitch")
            mSwitch(strict, id = "strictSwitch", onChange = {e, _ -> onStrictChange(e.targetChecked)})
        }
    }