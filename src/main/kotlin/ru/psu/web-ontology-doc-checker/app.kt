package ru.psu.web_ontology_doc_checker

import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mToolbar
import com.ccfraser.muirwik.components.mTypography
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

class App(props: RProps) : RComponent<RProps, RState>(props) {

    override fun RBuilder.render() {
        mAppBar {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов")
            }
        }
        mCard { }
    }
}
