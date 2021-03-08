package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.*
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.flex
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.Document
import styled.css

fun RBuilder.documentDialog(document: Document?, fullscreen: Boolean, onClose: () -> Unit) {
    if (document != null) {
        mDialog(true, fullscreen, onClose = { _, _ -> onClose() }, scroll = DialogScroll.body) {
            mAppBar(position = MAppBarPosition.sticky) {
                mToolbar {
                    mIconButton("close", onClick = { onClose() })
                    mTypography("Документ: ${document.name}", variant = MTypographyVariant.h6)
                }
            }
            mDialogContent {
                css {
                    display = Display.flex
                }
                mTextFieldMultiLine("Содержимое", document.text, disabled = true) {
                    css {
                        flex(1.0, 1.0)
                    }
                }
            }
        }
    }
}