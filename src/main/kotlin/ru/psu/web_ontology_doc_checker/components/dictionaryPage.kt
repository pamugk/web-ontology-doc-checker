package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.table.*
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.model.TermInfo
import styled.css
import kotlin.math.floor
import kotlin.math.min

private const val termsPerPage = 5

external interface DictionaryProps: RProps {
    var terms: List<TermInfo>
}

private val dictionaryPage = functionalComponent<DictionaryProps> { props ->
    var page by useState(0)

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
                    for (i in page * termsPerPage until min((page + 1) * termsPerPage, props.terms.size)) {
                        mTableRow("row$i") {
                            mTableCell("term$i") { mTypography(props.terms[i].term, MTypographyVariant.body2) }
                            mTableCell("forms$i") {
                                mTypography(props.terms[i].forms.joinToString("; "), MTypographyVariant.body2, align = MTypographyAlign.center)
                            }
                            mTableCell("def$i") { mTypography(props.terms[i].definition, MTypographyVariant.body2, align = MTypographyAlign.center) }
                        }
                    }
                }
                mTableFooter {
                    mTableRow {
                        mTableCell {
                            mContainer {
                                css {
                                    display = Display.flex
                                    minWidth = LinearDimension.maxContent
                                    alignItems = Align.center
                                }
                                mTypography("${page * termsPerPage + 1}-${min((page + 1) * termsPerPage, props.terms.size)} из ${props.terms.size}")
                                mIconButton("arrow_left", disabled = page == 0, onClick = { page-- })
                                mIconButton("arrow_right",
                                    disabled = page == floor(1.0 * props.terms.size / termsPerPage).toInt(), onClick = { page++ })
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.dictionaryPage(terms: List<TermInfo>) = child(dictionaryPage) {
    attrs.terms = terms
}