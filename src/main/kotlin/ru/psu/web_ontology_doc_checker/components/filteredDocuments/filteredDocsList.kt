package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCardActions
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mTooltip
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.utils.exportOntology
import ru.psu.web_ontology_doc_checker.utils.saveFileDialog
import styled.css

external interface FilteringPageProps: RProps {
    var filteredDocuments: List<FilteredDocument>
}

private fun onDownload(filteredDocs: List<FilteredDocument>) {
    filteredDocs.forEach { doc ->
        saveFileDialog(exportOntology(doc.boundOntology), "bound_${doc.name}.ont", "text/plain")
    }
}

private val filteredDocsList = functionalComponent<FilteringPageProps> { props ->
    var selectedDocument by useState<FilteredDocument?>(null)

    mCardActions {
        if (props.filteredDocuments.isNotEmpty()) {
            mTooltip("Сохранить \"онтологии привязки\" всех документов"){
                mIconButton("download", onClick = { onDownload(props.filteredDocuments) })
            }
        }
    }
    mCardContent {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        if (props.filteredDocuments.isEmpty()) {
            mTypography("Нет документов, связанных с ПрО, которую описывает выбранная онтология :( ") {
                css {
                    margin(LinearDimension.auto)
                }
            }
        } else {
            mList("Документы, близкие к ПрО, которую описывает выбранная онтология", true) {
                for (document in props.filteredDocuments)
                    filteredDocItem(document, { doc -> onDownload(listOf(doc))}) { newlySelectedDocument ->
                        selectedDocument = newlySelectedDocument
                    }
            }
        }
    }
    filteredDocDialog(selectedDocument, true) { selectedDocument = null}
}

fun RBuilder.filteredDocsList(filteredDocuments: List<FilteredDocument>) =
    child(filteredDocsList) {
        attrs.filteredDocuments = filteredDocuments
    }
