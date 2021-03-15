package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import kotlinx.browser.window
import kotlinx.css.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.get
import react.*
import ru.psu.web_ontology_doc_checker.components.documents.documentList
import ru.psu.web_ontology_doc_checker.components.filteredDocuments.filteringPage
import ru.psu.web_ontology_doc_checker.components.rangedDocuments.rankingPage
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import ru.psu.web_ontology_doc_checker.resources.defaultOntology
import ru.psu.web_ontology_doc_checker.utils.exportOntology
import ru.psu.web_ontology_doc_checker.utils.importOntology
import ru.psu.web_ontology_doc_checker.utils.openFileDialog
import ru.psu.web_ontology_doc_checker.utils.saveFileDialog
import styled.css

class AppState(
    var b: Int,
    var K: Int,
    var N: Int,
    var strict: Boolean,
    var showDialog: Boolean,
    var selectedTab: Any,

    var documents: Set<Document>,
    var documentsChanged: Boolean,

    var filteredDocuments: List<FilteredDocument>,
    var filteredDocsChanged: Boolean,
    var settingsChanged: Boolean,

    var rankedDocuments: List<RankedDocument>,

    var ontology: Onto
): RState

external interface AppProps: RProps {
    var ontology: String
}

class App(props: AppProps) : RComponent<AppProps, AppState>(props) {

    init {
        state = AppState(
            b = 1,
            K = 1,
            N = 10,
            strict = false,
            showDialog = false,
            selectedTab = Tabs.DOCUMENTS,

            documents = emptySet(),
            documentsChanged = false,

            filteredDocuments = emptyList(),
            filteredDocsChanged = false,
            settingsChanged = false,

            rankedDocuments = emptyList(),

            ontology = importOntology(props.ontology)
        )
    }

    override fun RBuilder.render() {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов")
                mIconButton("history", onClick = { changeOntology(defaultOntology) }) {
                    css {
                        marginLeft = LinearDimension.auto
                    }
                }
                mIconButton("upload", onClick = { openFileDialog(false) { e -> handleFileInput(e) } })
                mIconButton("settings", onClick = { setState { showDialog = true } })
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
                        state.ontology, state.N,
                        state.documents, state.documentsChanged, state.filteredDocuments,
                        ::onFilterDocuments, ::clearFilteredDocuments, ::downloadFilteredDocsOntologies)
                    Tabs.RANKING -> rankingPage(
                        state.ontology, state.filteredDocuments, state.filteredDocsChanged, state.settingsChanged,
                        state.b, state.K, state.N, state.strict, state.rankedDocuments, ::onRankDocuments)
                }
            }
        }
        settingsDialog(state.b, state.K, state.N, state.strict, state.showDialog,
            onClose = { setState { showDialog = false}},
            onbChange = { value -> setState { b = value; settingsChanged = state.rankedDocuments.isNotEmpty() }},
            onNChange = { value -> setState { N = value; settingsChanged = state.rankedDocuments.isNotEmpty() }},
            onKChange = { value -> setState { K = value; settingsChanged = state.rankedDocuments.isNotEmpty() }},
            onStrictChange = { value -> setState { strict = value; settingsChanged = state.rankedDocuments.isNotEmpty()}})
    }

    private fun handleFileInput(event: Event) {
        val input = event.target as? HTMLInputElement ?: return
        val file = input.files!!.get(0)!!
        val fReader = FileReader()
        fReader.onloadend = { _ -> window.alert("Онтология загружена"); changeOntology(fReader.result as String) }
        fReader.onerror = { _ -> window.alert("Загрузка онтологии не удалась, попробуйте ещё раз") }
        fReader.readAsText(file)
    }

    private fun changeOntology(ontologyText: String) {
        setState { ontology = importOntology(ontologyText); filteredDocuments = emptyList(); rankedDocuments = emptyList() }
    }

    private fun onAddDocument(newDocument: Document) {
        val updated = state.documents.contains(newDocument)
        setState { documents = documents.plus(newDocument); documentsChanged = updated && filteredDocuments.isNotEmpty() }
    }

    private fun onRemoveDocument(removedDocument: Document) {
        setState { documents = documents.minus(removedDocument); documentsChanged = filteredDocuments.isNotEmpty() }
    }

    private fun onFilterDocuments(newFilteredDocuments: List<FilteredDocument>) {
        setState { documentsChanged = false; filteredDocuments = newFilteredDocuments; filteredDocsChanged = rankedDocuments.isNotEmpty() }
    }

    private fun clearFilteredDocuments() {
        setState { documentsChanged = false; filteredDocuments = emptyList(); filteredDocsChanged = rankedDocuments.isNotEmpty() }
    }

    private fun downloadFilteredDocsOntologies(filteredDocs: List<FilteredDocument>) {
        filteredDocs.forEach { doc -> saveFileDialog(exportOntology(doc.boundOntology), "bound_${doc.name}.ont", "text/plain") }
    }

    private fun onRankDocuments(newRankedDocuments: List<RankedDocument>) {
        setState { filteredDocsChanged = false; rankedDocuments = newRankedDocuments }
    }
}
