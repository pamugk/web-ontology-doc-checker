package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardActions
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.mTypography
import kotlinx.browser.window
import kotlinx.css.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.get
import react.*
import ru.psu.web_ontology_doc_checker.components.documents.documentList
import ru.psu.web_ontology_doc_checker.components.filteredDocuments.filteredDocsList
import ru.psu.web_ontology_doc_checker.components.rangedDocuments.rankedDocsTable
import ru.psu.web_ontology_doc_checker.logic.filterDocuments
import ru.psu.web_ontology_doc_checker.logic.rankDocuments
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import ru.psu.web_ontology_doc_checker.resources.defaultOntology
import ru.psu.web_ontology_doc_checker.utils.importOntology
import ru.psu.web_ontology_doc_checker.utils.openFileDialog
import styled.css

private enum class Steps(val description: String) {
    DOCUMENT_COLLECTION("Выбор корпуса документов"),
    DOCUMENTS("Просмотр документы"),
    ONTOLOGY("Выбор управляющей онтологии"),
    SETTINGS("Предварительная настройка"),
    FILTER("Классификация документов"),
    FILTERED_DOCUMENTS("Просмотр классифицированных документов"),
    RANK("Ранжирование"),
    RANKED_DOCUMENTS("Просмотр ранжированных документов")
}

private fun handleFileInput(event: Event, onChangeOntology: (Onto) -> Unit) {
    val input = event.target as? HTMLInputElement ?: return
    val file = input.files!![0]!!
    val fReader = FileReader()
    fReader.onloadend = { _ ->
        window.alert("Онтология загружена")
        onChangeOntology(importOntology(fReader.result as String))
    }
    fReader.onerror = { _ -> window.alert("Загрузка онтологии не удалась, попробуйте ещё раз") }
    fReader.readAsText(file)
}

private val mainPage = functionalComponent<RProps> {
    var currentStep by useState(Steps.DOCUMENT_COLLECTION)
    var processing by useState(false)

    var b by useState(1); var K by useState(1)
    var N by useState(10); var strict by useState(false)

    var ontology by useState<Onto?>(null)

    var documents by useState<Set<Document>>(emptySet())
    var filteredDocuments by useState<List<FilteredDocument>>(emptyList())
    var rankedDocuments by useState<List<RankedDocument>>(emptyList())

    mCard {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        mCardContent {
            mTypography("Текущий шаг: \"${currentStep.description}\"")
        }
        when(currentStep) {
            Steps.DOCUMENT_COLLECTION -> documentCollectionPage { documentCollection ->
                documents = documentCollection.toSet()
                currentStep = Steps.DOCUMENTS
            }
            Steps.DOCUMENTS -> documentList(documents,
                { newDocument -> documents = documents.plus(newDocument) },
                { removedDocument -> documents = documents.minus(removedDocument) }
            )
            Steps.ONTOLOGY -> {
                if (processing) loader(true, "Идёт загрузка онтологии...") else {
                    mCardActions {
                        mButton("Выбрать онтологию по умолчанию", onClick = {
                            ontology = importOntology(defaultOntology)
                        })
                        mButton("Загрузить онтологию...", onClick = {
                            processing = true
                            openFileDialog(false) { e -> handleFileInput(e) { newOntology ->
                                    ontology = newOntology
                                    processing = false
                                }
                            }
                        })
                    }
                }
            }
            Steps.SETTINGS -> settingsPanel(b, N, K, strict, { newB -> b = newB }, { newN -> N = newN }, { newK -> K = newK },
                { newStrict -> strict = newStrict })
            Steps.FILTER -> if (processing) loader(processing, "Идёт классификация документов...") else
                mCardActions {
                    mButton("Провести классификацию документов", onClick = {
                        processing = true
                        filteredDocuments = filterDocuments(documents, ontology!!, N)
                        processing = false
                    })
            }
            Steps.FILTERED_DOCUMENTS -> filteredDocsList(filteredDocuments)
            Steps.RANK -> if (processing) loader(true, "Идёт ранжирование документов...") else {
                mButton("Провести ранжирование документов", onClick = {
                    processing = true
                    rankedDocuments = rankDocuments(b, N, K, strict, filteredDocuments)
                    processing = false
                })
            }
            Steps.RANKED_DOCUMENTS -> rankedDocsTable(rankedDocuments)
        }
        if (currentStep != Steps.DOCUMENT_COLLECTION) {
            mCardActions {
                mButton("Назад",
                    disabled = processing,
                    onClick = {
                        when(currentStep) {
                            Steps.DOCUMENT_COLLECTION -> TODO()
                            Steps.DOCUMENTS -> {
                                currentStep = Steps.DOCUMENT_COLLECTION
                                documents = emptySet()
                            }
                            Steps.ONTOLOGY -> {
                                currentStep = Steps.DOCUMENTS
                                ontology = null
                            }
                            Steps.SETTINGS -> currentStep = Steps.ONTOLOGY
                            Steps.FILTER -> {
                                currentStep = Steps.SETTINGS
                                filteredDocuments = emptyList()
                            }
                            Steps.FILTERED_DOCUMENTS -> {
                                currentStep = Steps.FILTERED_DOCUMENTS
                                filteredDocuments = emptyList()
                            }
                            Steps.RANK -> {
                                currentStep = Steps.FILTERED_DOCUMENTS
                                rankedDocuments = emptyList()
                            }
                            Steps.RANKED_DOCUMENTS -> currentStep = Steps.RANK
                        }
                    })
                mButton("Далее",
                    disabled = processing || currentStep == Steps.RANKED_DOCUMENTS
                            || currentStep == Steps.DOCUMENTS && documents.isEmpty()
                            || currentStep == Steps.ONTOLOGY && ontology == null
                            || currentStep == Steps.FILTER && filteredDocuments.isEmpty()
                            || currentStep == Steps.RANK && rankedDocuments.isEmpty(),
                    onClick = {
                        currentStep = when(currentStep) {
                            Steps.DOCUMENT_COLLECTION -> Steps.DOCUMENTS
                            Steps.DOCUMENTS -> Steps.ONTOLOGY
                            Steps.ONTOLOGY -> Steps.SETTINGS
                            Steps.SETTINGS -> Steps.FILTER
                            Steps.FILTER -> Steps.FILTERED_DOCUMENTS
                            Steps.FILTERED_DOCUMENTS -> Steps.RANK
                            Steps.RANK -> Steps.RANKED_DOCUMENTS
                            Steps.RANKED_DOCUMENTS -> TODO()
                        }
                    })
            }
        }
    }
}

fun RBuilder.mainPage() {
    child(mainPage) {}
}