package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemAvatar
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import kotlinx.css.properties.border
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import styled.css

fun RBuilder.filteredDocItem(document: FilteredDocument, onSelect: (FilteredDocument) -> Unit) {
    mListItem(onClick = { onSelect(document)}) {
        css {
            hover {
                border(1.pt, BorderStyle.solid, Color.orange)
            }
        }
        mListItemAvatar {
            mAvatar {
                mIcon("summarize")
            }
        }
        mContainer {
            css {
                display = Display.flex
                justifyContent = JustifyContent.center
            }
            mTypography(document.name)
            mTypography("Привязалось понятий: ${document.terms.size}; выбрано предложений: ${document.sentences.size}")
        }
    }
}