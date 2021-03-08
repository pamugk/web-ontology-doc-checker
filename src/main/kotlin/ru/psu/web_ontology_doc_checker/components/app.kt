package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.components.documents.documentList
import ru.psu.web_ontology_doc_checker.components.filteredDocuments.filteringPage
import ru.psu.web_ontology_doc_checker.components.processors.rankingPage
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import ru.psu.web_ontology_doc_checker.utils.importOntology
import styled.css

class AppState(
    var K: Int,
    var N: Int,
    var showDialog: Boolean,
    var selectedTab: Any,

    var documents: Set<Document>,
    var documentsChanged: Boolean,

    var filteredDocuments: List<FilteredDocument>,
    var filteredDocsChanged: Boolean,

    var ontology: Onto
): RState

external interface AppProps: RProps {
    var ontology: String
}

class App(props: AppProps) : RComponent<AppProps, AppState>(props) {

    init {
        state = AppState(
            K = 1,
            N = 10,
            showDialog = false,
            selectedTab = Tabs.DOCUMENTS,

            documents = emptySet(),
            documentsChanged = false,

            filteredDocuments = emptyList(),
            filteredDocsChanged = false,

            ontology = importOntology(props.ontology)
        )
    }

    override fun RBuilder.render() {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов") {}
                mIconButton("settings", onClick = { setState { showDialog = true } }) {
                    css {
                        marginLeft = LinearDimension.auto
                    }
                }
            }
        }
        mCard {
            css {
                display = Display.flex
                flex(1.0, 1.0)
            }
            mCardContent {
                css {
                    display = Display.flex
                    flex(1.0, 1.0)
                }
                mTabs(state.selectedTab, variant = MTabVariant.scrollable, orientation = MTabOrientation.vertical,
                    onChange = {_, newTab -> setState { selectedTab = newTab }}) {
                    mTab("Документы", value = Tabs.DOCUMENTS)
                    mTab("Предобработка", value = Tabs.FILTERING)
                    mTab("Ранжирование", value = Tabs.RANKING)
                }
                when (state.selectedTab) {
                    Tabs.DOCUMENTS -> documentList(state.documents, ::onAddDocument, ::onRemoveDocument)
                    Tabs.FILTERING -> filteringPage(
                        state.ontology,
                        state.documents, state.documentsChanged, state.filteredDocuments,
                        ::onFilterDocuments, ::clearFilteredDocuments)
                    Tabs.RANKING -> rankingPage(state.filteredDocuments, state.filteredDocsChanged, emptyList(), {})
                }
            }
        }
        settingsDialog(state.K, state.N, state.showDialog,
            onClose = { setState { showDialog = false}},
            onNChange = { value -> setState { N = value }},
            onKChange = { value -> setState { K = value }})
    }

    private fun onAddDocument(newDocument: Document) {
        setState { documents = documents.plus(newDocument); documentsChanged = filteredDocuments.isNotEmpty() }
    }

    private fun onRemoveDocument(removedDocument: Document) {
        setState { documents = documents.minus(removedDocument); documentsChanged = filteredDocuments.isNotEmpty() }
    }

    private fun onFilterDocuments(newFilteredDocuments: List<FilteredDocument>) {
        setState { documentsChanged = false; filteredDocuments = newFilteredDocuments }
    }

    private fun clearFilteredDocuments() {
        setState { documentsChanged = false; filteredDocuments = emptyList() }
    }
}
