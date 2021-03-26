package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.flex
import react.*
import styled.css

private enum class Tabs(val label: String) {
    DOCUMENTS_PROCESSING("Обработка документов"),
    DICTIONARY("Справочник")
}

private val app = functionalComponent<RProps> { props ->
    var selectedTab by useState<Any>(Tabs.DOCUMENTS_PROCESSING)

    mAppBar(position = MAppBarPosition.sticky) {
        mTabs(selectedTab, variant = MTabVariant.scrollable, onChange = {_, newTab -> selectedTab = newTab }) {
            mTab(Tabs.DOCUMENTS_PROCESSING.label, value = Tabs.DOCUMENTS_PROCESSING)
            mTab(Tabs.DICTIONARY.label, value = Tabs.DICTIONARY)
        }
    }
    mContainer {
        css {
            display = Display.flex
            flex(1.0, 1.0)
        }
        when (selectedTab) {
            Tabs.DOCUMENTS_PROCESSING -> mainPage()
            Tabs.DICTIONARY -> dictionaryPage()
        }
    }
}

fun RBuilder.app() = child(app) {}
