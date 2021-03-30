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
import ru.psu.web_ontology_doc_checker.model.TermInfo
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import ru.psu.web_ontology_doc_checker.resources.defaultOntology
import ru.psu.web_ontology_doc_checker.utils.importOntology
import ru.psu.web_ontology_doc_checker.utils.openFileDialog
import styled.css

enum class Steps(val description: String) {
    DOCUMENT_COLLECTION("Выбор корпуса документов"),
    DOCUMENTS("Просмотр документов"),
    ONTOLOGY("Выбор управляющей онтологии"),
    SETTINGS("Предварительная настройка"),
    FILTER("Классификация документов"),
    FILTERED_DOCUMENTS("Просмотр результатов классификации"),
    RANK("Ранжирование"),
    RANKED_DOCUMENTS("Просмотр ранжированных документов")
}

private val orderOfSteps = listOf(
    Steps.DOCUMENT_COLLECTION, Steps.DOCUMENTS,
    Steps.ONTOLOGY, Steps.SETTINGS,
    Steps.FILTER, Steps.FILTERED_DOCUMENTS,
    Steps.RANK, Steps.RANKED_DOCUMENTS
)

private fun handleFileInput(event: Event, terms: List<TermInfo>, onChangeOntology: (Onto) -> Unit) {
    val input = event.target as? HTMLInputElement ?: return
    val file = input.files!![0]!!
    val fReader = FileReader()
    fReader.onloadend = { _ ->
        val newOntology = importOntology(fReader.result as String)
        val wrongOntology = newOntology.nodes.none { node -> terms.any { term -> term.term == node.name } }
        window.alert("Онтология загружена${if (wrongOntology) ", но она не содержит ни одного понятия, занесённого во встроенный словарь понятий" else ""}")
        onChangeOntology(newOntology)
    }
    fReader.onerror = { _ -> window.alert("Загрузка онтологии не удалась, попробуйте ещё раз") }
    fReader.readAsText(file)
}

class MainPageState(
    var currentStep: Steps, var processing: Boolean,

    var b: Int, var N: Int, var K: Int, var strict: Boolean,

    var ontology: Onto?,

    var documents: Set<Document>,
    var filteredDocuments: List<FilteredDocument>,
    var rankedDocuments: List<RankedDocument>
): RState

external interface MainProps: RProps {
    var terms: List<TermInfo>
}

class MainPage: RComponent<MainProps, MainPageState>() {
    init {
        state = MainPageState(
            Steps.DOCUMENT_COLLECTION, false,
            1, 10, 1, false,
            null, emptySet(), emptyList(), emptyList()
        )
    }

    override fun RBuilder.render() {
        mCard {
            css {
                display = Display.flex
                flex(1.0, 1.0)
                flexDirection = FlexDirection.column
            }
            mCardContent {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }
                if (state.currentStep != orderOfSteps[0]) {
                    mTypography("Предыдущий шаг: \"${orderOfSteps[orderOfSteps.indexOf(state.currentStep) - 1].description}\"")
                }
                mTypography("Текущий шаг: \"${state.currentStep.description}\"") {
                    css { fontWeight = FontWeight.bold }
                }
                if (state.currentStep != orderOfSteps[orderOfSteps.lastIndex]) {
                    mTypography("Следующий шаг: \"${orderOfSteps[orderOfSteps.indexOf(state.currentStep) + 1].description}\"")
                }
            }
            when(state.currentStep) {
                Steps.DOCUMENT_COLLECTION -> documentCollectionPage { documentCollection ->
                    setState{ documents = documentCollection.toSet(); currentStep = Steps.DOCUMENTS }
                }
                Steps.DOCUMENTS -> documentList(state.documents,
                    { newDocument -> setState { documents = documents.plus(newDocument) } },
                    { removedDocument -> setState { documents = documents.minus(removedDocument) } }
                )
                Steps.ONTOLOGY -> {
                    if (state.processing) loader(true, "Идёт загрузка онтологии...") else {
                        mCardActions {
                            css {
                                flex(1.0, 1.0, FlexBasis.auto)
                                margin(LinearDimension.auto)
                            }
                            mButton("Выбрать онтологию по умолчанию", onClick = {
                                setState { ontology = importOntology(defaultOntology) }
                            })
                            mButton("Загрузить онтологию...", onClick = {
                                setState { processing = true }
                                openFileDialog(false) { e -> handleFileInput(e, props.terms) { newOntology ->
                                        setState { ontology = newOntology; processing = false }
                                    }
                                }
                            })
                        }
                    }
                }
                Steps.SETTINGS -> settingsPanel(state.b, state.N, state.K, state.strict,
                    { newB -> setState { b = newB } }, { newN -> setState { N = newN } },
                    { newK -> setState { K = newK } }, { newStrict -> setState { strict = newStrict } })
                Steps.FILTER -> if (state.processing) loader(state.processing, "Идёт классификация документов...") else
                    mCardActions {
                        css {
                            flex(1.0, 1.0, FlexBasis.auto)
                            margin(LinearDimension.auto)
                        }
                        mButton("Провести классификацию документов", onClick = {
                            setState { processing = true }
                            setState { filteredDocuments = filterDocuments(documents, ontology!!, N, props.terms); processing = false }
                        })
                    }
                Steps.FILTERED_DOCUMENTS -> filteredDocsList(state.filteredDocuments.filter { doc -> doc.terms.isNotEmpty() && doc.sentences.isNotEmpty() })
                Steps.RANK -> if (state.processing) loader(true, "Идёт ранжирование документов...") else {
                    mCardActions {
                        css {
                            flex(1.0, 1.0, FlexBasis.auto)
                            margin(LinearDimension.auto)
                        }
                        mButton("Провести ранжирование документов", onClick = {
                            setState { processing = true }
                            setState { rankedDocuments = rankDocuments(b, N, K, strict, filteredDocuments); processing = false }
                        })
                    }
                }
                Steps.RANKED_DOCUMENTS -> rankedDocsTable(state.rankedDocuments, state.filteredDocuments)
            }
            if (state.currentStep != Steps.DOCUMENT_COLLECTION) {
                mCardActions {
                    mButton("Назад",
                        disabled = state.processing,
                        onClick = {
                            when(state.currentStep) {
                                Steps.DOCUMENT_COLLECTION -> TODO()
                                Steps.DOCUMENTS -> setState { currentStep = Steps.DOCUMENT_COLLECTION; documents = emptySet() }
                                Steps.ONTOLOGY -> setState { currentStep = Steps.DOCUMENTS; ontology = null }
                                Steps.SETTINGS -> setState { currentStep = Steps.ONTOLOGY }
                                Steps.FILTER -> setState { currentStep = Steps.SETTINGS; filteredDocuments = emptyList() }
                                Steps.FILTERED_DOCUMENTS -> setState { currentStep = Steps.FILTER; filteredDocuments = emptyList() }
                                Steps.RANK -> setState{ currentStep = Steps.FILTERED_DOCUMENTS; rankedDocuments = emptyList() }
                                Steps.RANKED_DOCUMENTS -> setState{ currentStep = Steps.RANK; rankedDocuments = emptyList() }
                            }
                        })
                    mButton("Далее",
                        disabled = state.processing || state.currentStep == orderOfSteps[orderOfSteps.lastIndex]
                                || state.currentStep == Steps.DOCUMENTS && state.documents.isEmpty()
                                || state.currentStep == Steps.ONTOLOGY && state.ontology == null
                                || state.currentStep == Steps.FILTER && state.filteredDocuments.isEmpty()
                                || state.currentStep == Steps.FILTERED_DOCUMENTS && state.filteredDocuments.none { doc -> doc.terms.isNotEmpty() && doc.sentences.isNotEmpty() }
                                || state.currentStep == Steps.RANK && state.rankedDocuments.isEmpty(),
                        onClick = {
                            setState { currentStep = orderOfSteps[orderOfSteps.indexOf(currentStep) + 1] }
                        })
                }
            }
        }
    }
}

fun RBuilder.mainPage(terms: List<TermInfo>) {
    child(MainPage::class) {
        attrs.terms = terms
    }
}