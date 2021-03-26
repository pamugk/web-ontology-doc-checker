package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.LinearDimension
import kotlinx.css.margin
import react.RBuilder
import styled.css

fun RBuilder.loader(processingDocuments: Boolean, text: String) {
    mCardContent {
        mCircularProgress {
            css {
                margin(LinearDimension.auto)
            }
        }
        mTypography(if (processingDocuments) text else "Готово")
    }
}