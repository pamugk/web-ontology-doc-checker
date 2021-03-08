package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.mCircularProgress
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.display
import kotlinx.css.margin
import react.RBuilder
import styled.css

fun RBuilder.filteringNotification(processingDocuments: Boolean) {
    if(processingDocuments) {
        mDialog(true) {
            mDialogTitle("Идёт обработка документов...")
            mDialogContent {
                css {
                    display = Display.flex
                }
                mCircularProgress() {
                    css {
                        margin(LinearDimension.auto)
                    }
                }
            }
        }
    }
}