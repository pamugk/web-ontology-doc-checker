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
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import styled.css

fun RBuilder.rankedDocDialog(document: RankedDocument, fullscreen: Boolean, onClose: () -> Unit) {
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
            mTableContainer {
                mTable {
                    mTableHead {
                        mTableRow("row") {
                            mTableCell("termi") { mTypography("Понятие i", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("termj") { mTypography("Понятие j", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("sij") { mTypography("Степень близости (Sij)", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("s'ij") { mTypography("Норм. степень близости (S'ij)", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("pij") { mTypography("Количество путей (Pij)", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("ui") { mTypography("Мощность понятия", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("ei") { mTypography("Вес дуги", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("bi*") { mTypography("Bi*", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("b*j") { mTypography("B*j", MTypographyVariant.body2, align = MTypographyAlign.center) }
                        }
                    }
                    mTableBody {
                        for ((i, pair) in document.rangedItems.withIndex()) {
                            mTableRow("row$i") {
                                mTableCell("termi$i") { mTypography(pair.termFrom, MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("termj$i") { mTypography(pair.termTo, MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("sij$i") { mTypography("${pair.rank}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("s'ij$i") { mTypography("${pair.rankNorm}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("pij$i") { mTypography("${pair.p}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("ui$i") { mTypography("${pair.u}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("ei$i") { mTypography("${pair.e}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("bi*$i") { mTypography("${pair.bi}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                                mTableCell("b*j$i") { mTypography("${pair.bj}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                            }
                        }
                    }
                }
            }
        }
    }
}