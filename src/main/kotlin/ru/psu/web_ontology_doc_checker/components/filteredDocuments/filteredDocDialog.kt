package ru.psu.web_ontology_doc_checker.components.filteredDocuments

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListSubheader
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import styled.css

fun RBuilder.filteredDocDialog(document: FilteredDocument?, fullscreen: Boolean, onClose: () -> Unit) {
    if (document != null) {
        mDialog(true, fullscreen, onClose = { _, _ -> onClose() }, scroll = DialogScroll.body) {
            mAppBar(position = MAppBarPosition.sticky) {
                mToolbar {
                    mIconButton("close", onClick = { onClose() })
                    mTypography("Документ: ${document.name}", variant = MTypographyVariant.h6)
                }
            }
            mDialogContent {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }
                mList {
                    mListSubheader("Привязавшиеся понятия")
                    for ((i, term) in document.terms.withIndex()) {
                        mListItem(primaryText = "${i+1}. $term", key = i.toString())
                    }
                }
                mDivider(MDividerVariant.fullWidth)
                mList {
                    mListSubheader("Предложения, содержащие привязавшиеся понятия")
                    for ((i, sentence) in document.sentences.withIndex()) {
                        mListItem(primaryText = "${i+1}. $sentence", key = i.toString())
                    }
                }
            }
        }
    }
}