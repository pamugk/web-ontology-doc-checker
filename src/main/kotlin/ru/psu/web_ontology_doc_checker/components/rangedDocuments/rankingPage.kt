package ru.psu.web_ontology_doc_checker.components.rangedDocuments

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.components.loadingNotification
import ru.psu.web_ontology_doc_checker.logic.rankDocuments
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import styled.css

external interface RangingPageProps: RProps {
    var ontology: Onto
    var filteredDocuments: List<FilteredDocument>
    var filteredDocsChanged: Boolean; var settingsChanged: Boolean
    var b: Int ; var N: Int ; var K:Int
    var rankedDocuments: List<RankedDocument>
    var onRanked: (List<RankedDocument>) -> Unit
}

private val rankingPage = functionalComponent<RangingPageProps> { props ->
    var processingDocuments by useState(false)

    mCard {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        mCardContent {
            css {
                display = Display.flex
                flex(1.0, 1.0)
                flexDirection = FlexDirection.column
            }
            if (props.filteredDocuments.isNotEmpty()) {
                if (props.rankedDocuments.isEmpty() || props.settingsChanged || props.filteredDocsChanged) {
                    mButton("Провести реранжирование", onClick = { e ->
                        processingDocuments = true
                        val rankedDocuments = rankDocuments(props.b, props.K, props.N, props.filteredDocuments)
                        processingDocuments = false
                        props.onRanked(rankedDocuments)
                    })
                }
                if (props.filteredDocsChanged || props.settingsChanged) {
                    mTypography("С момента последнего ранжирования список " +
                            (if (props.filteredDocsChanged) "фильтрованных документов был обновлён" else "настройки были обновлены") +
                            ", рекомендуется провести повторное ранжирование") {
                        css {
                            margin(LinearDimension.auto)
                        }
                    }
                }
                if (props.rankedDocuments.isNotEmpty()) {
                    rankedDocsTable(props.rankedDocuments)
                } else {
                    mTypography("Ранжирование документов пока что не проводилось") {
                        css {
                            margin(LinearDimension.auto)
                        }
                    }
                }
                loadingNotification(processingDocuments, "Идёт ранжирование документов...")
            } else {
                mTypography("Список фильтрованных документов пуст, ранжировать нечего :( ") {
                    css {
                        margin(LinearDimension.auto)
                    }
                }
            }
        }
    }
}

fun RBuilder.rankingPage(
    ontology: Onto,
    filteredDocuments: List<FilteredDocument>, filteredDocsChanged: Boolean, settingsChanged: Boolean,
    b: Int, N: Int, K:Int,
    rankedDocuments: List<RankedDocument>, onRanked: (List<RankedDocument>) -> Unit) =
    child(rankingPage) {
        attrs.ontology = ontology
        attrs.filteredDocuments = filteredDocuments
        attrs.filteredDocsChanged = filteredDocsChanged; attrs.settingsChanged = settingsChanged
        attrs.b = b; attrs.N = N; attrs.K = K
        attrs.rankedDocuments = rankedDocuments; attrs.onRanked = onRanked
    }