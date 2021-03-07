package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.components.documents.documentList
import ru.psu.web_ontology_doc_checker.model.Document
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import ru.psu.web_ontology_doc_checker.utils.importOntology
import styled.css

class AppState(
    var K: Int,
    var N: Int,
    var showDialog: Boolean,
    var selectedTab: Any,
    var documents: List<Document>,
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
            selectedTab = 0,
            documents = emptyList(),
            ontology = importOntology(props.ontology)
        )
    }

    override fun RBuilder.render() {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов")
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
                    mTab("Документы", value = 0)
                    mTab("Обработка", value = 1)
                    mTab("Итог", value = 2)
                }
                when (state.selectedTab) {
                    0 -> documentList(state.documents, ::onAddDocument, ::onRemoveDocument)
                    1 -> {

                    }
                    2 -> {

                    }
                }
            }
        }
        settingsDialog(state.K, state.N, state.showDialog,
            onClose = { setState { showDialog = false}},
            onNChange = { value -> setState { N = value }},
            onKChange = { value -> setState { K = value }})
    }

    private fun onAddDocument(newDocument: Document) {
        setState { documents = documents.plus(newDocument) }
    }

    private fun onRemoveDocument(removedDocument: Document) {
        setState { documents = documents.minus(removedDocument) }
    }
}
