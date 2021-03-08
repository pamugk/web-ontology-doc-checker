package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogContentText
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import react.*
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.utils.openFileDialog
import styled.css

external interface DocumentListProps: RProps {
    var documents: Collection<Document>
    var onDocumentAdded: (Document)->Unit
    var onDocumentRemoved: (Document)->Unit
}

private val documentList = functionalComponent<DocumentListProps> { props ->
    var selectedDocument by useState<Document?>(null)
    var showFileError by useState(false)
    mContainer {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
        }
        mIconButton("add_circle_outline", onClick = {
            openFileDialog { event -> handleFileInput(event, props.onDocumentAdded, { showFileError = true }) }
        })
        if (props.documents.isEmpty())
            mCard {
                css {
                    display = Display.flex
                    flex(1.0, 1.0)
                }
                mCardContent {
                    css {
                        margin(LinearDimension.auto)
                    }
                    mTypography("Список документов пуст")
                }
            }
        else {
            mList {
                for (document in props.documents)
                    documentItem(document, { newlySelectedDocument -> selectedDocument = newlySelectedDocument }, props.onDocumentRemoved)
            }
            documentDialog(selectedDocument, true) { selectedDocument = null }
        }
        if (showFileError) {
            mDialog(true, onClose = { _, _ -> showFileError = false }) {
                mDialogTitle("Ошибка чтения файла")
                mDialogContent {
                    mDialogContentText("Один из открываемых файлов не удалось открыть, увы-увы.")
                }
            }
        }
    }
}

fun RBuilder.documentList(documents: Collection<Document>, onDocumentAdded: (Document)->Unit, onDocumentRemoved: (Document)->Unit) =
    child(documentList) {
        attrs.documents = documents
        attrs.onDocumentAdded = onDocumentAdded
        attrs.onDocumentRemoved = onDocumentRemoved
    }

private fun handleFileInput(event: Event, onDocumentAdded: (Document)->Unit, onError: () -> Unit) {
    val input = event.target as? HTMLInputElement ?: return
    input.files!!.asList().forEach { file ->
        run {
            val fReader = FileReader()
            fReader.onloadend = { _ -> onDocumentAdded(Document(file.name, file.name, fReader.result as String)) }
            fReader.onerror = { _ -> onError() }
            fReader.readAsText(file)
        }
    }
}