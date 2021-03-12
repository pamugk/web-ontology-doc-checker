package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardActionArea
import com.ccfraser.muirwik.components.card.mCardActions
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.logic.filterDocuments
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import styled.css

external interface FilteringPageProps: RProps {
    var ontology: Onto
    var documents: Collection<Document>
    var documentsChanged: Boolean
    var filteredDocuments: List<FilteredDocument>
    var onFiltered: (List<FilteredDocument>) -> Unit
    var onClear: () -> Unit
}

private val filteringPage = functionalComponent<FilteringPageProps> { props ->
    var processingDocuments by useState(false)
    var selectedDocument by useState<FilteredDocument?>(null)

    mCard {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        if (props.documents.isEmpty()) {
            mCardContent {
                css {
                    display = Display.flex
                    flex(1.0, 1.0)
                }
                mTypography("Список документов пуст, фильтровать нечего :( ") {
                    css {
                        margin(LinearDimension.auto)
                    }
                }
            }
        } else {
            mCardActions {
                if (props.documentsChanged || props.filteredDocuments.isEmpty()) {
                    mButton("Провести фильтрацию предложений документов", onClick = {
                        processingDocuments = true
                        val filteredDocuments = filterDocuments(props.documents)
                        processingDocuments = false
                        props.onFiltered(filteredDocuments)
                    })
                }
                if (props.filteredDocuments.isNotEmpty()) {
                    mButton("Очистить фильтрованные документы", onClick = { props.onClear() })
                }
            }
            mCardContent {
                css {
                    display = Display.flex
                    flex(1.0, 1.0)
                    flexDirection = FlexDirection.column
                }
                if (props.filteredDocuments.isEmpty()) {
                    mTypography("Нет документов с фильтрованным содержимым :( ") {
                        css {
                            margin(LinearDimension.auto)
                        }
                    }
                } else {
                    if (props.documentsChanged) {
                        mTypography("С момента последней фильтрации произошли изменения в составе корпуса документов, поэтому рекомендуется произвести повторную фильтрацию")
                    }
                    mList("Фильтрованные документы", true) {
                        for (document in props.filteredDocuments)
                            filteredDocItem(document) { newlySelectedDocument ->
                                selectedDocument = newlySelectedDocument
                            }
                    }
                }
            }
            filteringNotification(processingDocuments)
            filteredDocDialog(selectedDocument, true) { selectedDocument = null}
        }
    }
}

fun RBuilder.filteringPage(
    ontology: Onto,
    documents: Collection<Document>, documentsChanged: Boolean,
    filteredDocuments: List<FilteredDocument>,
    onFiltered: (List<FilteredDocument>) -> Unit, onClear: () -> Unit) =
    child(filteringPage) {
        attrs.ontology = ontology
        attrs.documents = documents
        attrs.documentsChanged = documentsChanged
        attrs.filteredDocuments = filteredDocuments
        attrs.onFiltered = onFiltered
        attrs.onClear = onClear
    }
