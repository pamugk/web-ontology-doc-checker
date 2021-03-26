package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.table.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import ru.psu.web_ontology_doc_checker.logic.terms
import styled.css

fun RBuilder.dictionaryPage() {
    mDialogContent {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
        }
        mTableContainer {
            mTable {
                mTableHead {
                    mTableRow("row") {
                        mTableCell("term") { mTypography("Понятие", MTypographyVariant.body2) }
                        mTableCell("form") { mTypography("Формы", MTypographyVariant.body2, align = MTypographyAlign.center) }
                        mTableCell("def") { mTypography("Определение", MTypographyVariant.body2, align = MTypographyAlign.center) }
                    }
                }
                mTableBody {
                    for ((i, term) in terms.withIndex()) {
                        mTableRow("row$i") {
                            mTableCell("term$i") { mTypography(term.term, MTypographyVariant.body2) }
                            mTableCell("forms$i") {
                                for (form in term.forms) {
                                    mTypography(form, MTypographyVariant.body2, align = MTypographyAlign.center)
                                }
                            }
                            mTableCell("def$i") { mTypography(term.definition, MTypographyVariant.body2, align = MTypographyAlign.center) }
                        }
                    }
                }
            }
        }
    }
}