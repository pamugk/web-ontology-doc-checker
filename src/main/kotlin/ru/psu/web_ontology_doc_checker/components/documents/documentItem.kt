package ru.psu.web_ontology_doc_checker.components.documents

import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemAvatar
import com.ccfraser.muirwik.components.list.mListItemSecondaryAction
import com.ccfraser.muirwik.components.mAvatar
import com.ccfraser.muirwik.components.mIcon
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.Document

fun RBuilder.documentItem(document: Document, onSelect: (Document) -> Unit, onDelete: (Document) -> Unit) {
    mListItem(onClick = { onSelect(document)}) {
        mListItemAvatar {
            mAvatar {
                mIcon("receipt_long")
            }
        }
        mListItemSecondaryAction {
            mIconButton("delete_forever", onClick = { onDelete(document) })
        }
    }
}