package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemAvatar
import com.ccfraser.muirwik.components.list.mListItemSecondaryAction
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mIcon
import kotlinx.css.*
import kotlinx.css.properties.border
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import styled.css

fun RBuilder.filteredDocItem(document: FilteredDocument, onSelect: (FilteredDocument) -> Unit, onDownload: (FilteredDocument) -> Unit) {
    mListItem(onClick = { onDownload(document) }) {
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
        mListItemText(document.name, "Привязалось понятий: ${document.terms.size}; выбрано предложений: ${document.sentences.size}") {
            css {
                cursor = Cursor.default
                textAlign = TextAlign.center
            }
        }
        mListItemSecondaryAction {
            mIconButton("download", onClick = { onSelect(document)})
        }
    }
}