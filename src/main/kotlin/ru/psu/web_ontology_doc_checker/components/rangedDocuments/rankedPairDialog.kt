package ru.psu.web_ontology_doc_checker.components.rangedDocuments

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.table.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedItem
import styled.css

fun RBuilder.rankedPairDialog(termPair: RankedItem, document: FilteredDocument, fullscreen: Boolean, onClose: () -> Unit) {
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
                flexDirection = FlexDirection.column
            }
            mTypography("Предложения, содержащие понятия \"${termPair.termFrom}\" и \"${termPair.termTo}\"")
            mTableContainer {
                mTable {
                    mTableHead {
                        mTableRow("row") {
                            mTableCell("num") { mTypography("№", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("numOrig") { mTypography("№ в исходном документе", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("sent") { mTypography("Предложение", MTypographyVariant.body2, align = MTypographyAlign.center) }
                        }
                    }
                    mTableBody {
                        for ((i, sentence) in document.sentences.filter { sentence ->
                            sentence.terms.contains(termPair.termFrom) ||
                                    sentence.terms.contains(termPair.termTo) } .withIndex()) {
                            mTableRow("row$i") {
                                mTableCell("num$i") { mTypography("${i + 1}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("numOrig$i") { mTypography("${sentence.number + 1}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("sent$i") { mTypography(sentence.text, MTypographyVariant.body2, align = MTypographyAlign.center) }
                            }
                        }
                    }
                }
            }
        }
    }
}