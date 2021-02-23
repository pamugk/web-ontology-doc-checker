package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import kotlinx.html.InputType
import react.*

fun RBuilder.settingsDialog(
    K: Int, N: Int, showDialog: Boolean, onClose: ()->Unit, onNChange: (Int)->Unit, onKChange: (Int)->Unit) =
    mDialog(showDialog, onClose =  { _, _ -> onClose.invoke() }) {
        mDialogTitle("Настройки")
        mDialogContent {
            mDialogContentText("""
                        Основные настройки:
                        N - семантическое расстояние между вершинами онтологии;
                        K - количество предложений между понятиями в исходном тексте.
                    """.trimIndent())
            mTextField("N",
                onChange = {
                    val value = it.targetInputValue.toIntOrNull() ?: N
                    if (value in 1..99 && value != N) onNChange.invoke(value)
                },
                type = InputType.number,
                value = N.toString(),
                variant = MFormControlVariant.outlined)
            mTextField("K",
                onChange = {
                    val value = it.targetInputValue.toIntOrNull() ?: K
                    if (value in 1..99 && value != K) onKChange.invoke(value)
                },
                type = InputType.number,
                value = K.toString(),
                variant = MFormControlVariant.outlined)
        }
    }