package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemAvatar
import com.ccfraser.muirwik.components.list.mListItemSecondaryAction
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.*
import kotlinx.css.properties.border
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.Document
import styled.css

fun RBuilder.documentItem(document: Document, onSelect: (Document) -> Unit, onDelete: (Document) -> Unit) {
    mListItem(onClick = { onSelect(document)}) {
        css {
            hover {
                border(1.pt, BorderStyle.solid, Color.orange)
            }
        }
        mListItemAvatar {
            mAvatar {
                mIcon("receipt_long")
            }
        }
        mListItemText(document.name) {
            css {
                textAlign = TextAlign.center
            }
        }
        mListItemSecondaryAction {
            mIconButton("delete_forever", onClick = { onDelete(document) })
        }
    }
}