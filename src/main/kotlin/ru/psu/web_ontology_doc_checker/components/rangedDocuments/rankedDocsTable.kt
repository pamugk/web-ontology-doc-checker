package ru.psu.web_ontology_doc_checker.components.rangedDocuments

import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.table.*
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import styled.css

external interface RangingPageProps: RProps {
    var rankedDocuments: List<RankedDocument>
    var filteredDocuments: List<FilteredDocument>
}

private val rankedPage = functionalComponent<RangingPageProps> { props ->
    var selectedDoc by useState<RankedDocument?>(null)

    mCardContent {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        mTableContainer {
            mTable {
                mTableHead {
                    mTableRow("row") {
                        mTableCell("num") { mTypography("№", MTypographyVariant.body2) }
                        mTableCell("doc") { mTypography("Документ", MTypographyVariant.body2, align = MTypographyAlign.center) }
                        mTableCell("res") { mTypography("Оценка", MTypographyVariant.body2, align = MTypographyAlign.center) }
                    }
                }
                mTableBody {
                    for ((i, doc) in props.rankedDocuments.withIndex()) {
                        mTableRow("row$i", onClick = { selectedDoc = doc }) {
                            mTableCell("num$i") { mTypography("${i+1}", MTypographyVariant.body2) }
                            mTableCell("doc$i") { mTypography(doc.name, MTypographyVariant.body2, align = MTypographyAlign.center) }
                            mTableCell("res$i") { mTypography("${doc.result}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                        }
                    }
                }
            }
        }
        if (selectedDoc != null) {
            rankedDocDialog(selectedDoc!!, props.filteredDocuments.first { filteredDoc ->
                filteredDoc.name == selectedDoc!!.name && filteredDoc.path == selectedDoc!!.path },
                true) { selectedDoc = null }
        }
    }
}

fun RBuilder.rankedDocsTable(rankedDocuments: List<RankedDocument>, filteredDocuments: List<FilteredDocument>) =
    child(rankedPage) {
        attrs.rankedDocuments = rankedDocuments
        attrs.filteredDocuments = filteredDocuments
    }