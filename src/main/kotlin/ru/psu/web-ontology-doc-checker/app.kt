package ru.psu.web_ontology_doc_checker

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.card.mCard
import kotlinx.css.FlexBasis
import kotlinx.css.LinearDimension
import kotlinx.css.flex
import kotlinx.css.marginLeft
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css

class App(props: RProps) : RComponent<RProps, RState>(props) {

    override fun RBuilder.render() {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar {
                mTypography("Онтологический упорядочиватель документов")
                mIconButton("settings") {
                    css {
                        marginLeft = LinearDimension.auto
                    }
                }
            }
        }
        mCard {
            css {
                flex(1.0, 1.0)
            }
        }
    }
}
