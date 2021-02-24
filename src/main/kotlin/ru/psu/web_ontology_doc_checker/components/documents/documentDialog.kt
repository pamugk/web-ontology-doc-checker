package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.*
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.Document

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
                mTextFieldMultiLine("Содержимое", document.text, disabled = true)
            }
        }
    }
}