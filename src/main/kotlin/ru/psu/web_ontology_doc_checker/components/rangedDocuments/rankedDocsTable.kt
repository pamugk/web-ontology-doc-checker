package ru.psu.web_ontology_doc_checker.components.rangedDocuments

import com.ccfraser.muirwik.components.MTypographyAlign
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.table.*
import react.*
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument

external interface RankedDocsTableProps: RProps {
    var rankedDocuments: List<RankedDocument>
}

private val rankedDocsTable = functionalComponent<RankedDocsTableProps> { props ->
    var selectedDoc by useState<RankedDocument?>(null)

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
                    mTableRow("row$i") {
                        mTableCell("num$i") { mTypography("${i+1}", MTypographyVariant.body2) }
                        mTableCell("doc$i") { mTypography(doc.name, MTypographyVariant.body2, align = MTypographyAlign.center) }
                        mTableCell("res$i") { mTypography("${doc.result}", MTypographyVariant.body2, align = MTypographyAlign.center) }
                    }
                }
            }
        }
    }
    if (selectedDoc != null) {
        rankedDocDialog(selectedDoc!!, true) { selectedDoc = null }
    }
}

fun RBuilder.rankedDocsTable(rankedDocuments: List<RankedDocument>) =
    child(rankedDocsTable) {
        attrs.rankedDocuments = rankedDocuments
    }
