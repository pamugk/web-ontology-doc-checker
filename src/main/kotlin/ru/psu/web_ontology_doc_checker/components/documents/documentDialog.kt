package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.dialog.*
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.Document

fun RBuilder.documentDialog(document: Document?, fullscreen: Boolean, onClose: () -> Unit) {
    mDialog(document != null, fullscreen, onClose = { _, _ -> onClose() }, scroll = DialogScroll.body) {
        mDialogTitle("Документ: ${document?.name}")
        mDialogContent {
            mDialogContentText(document?.text ?: "")
        }
    }
}