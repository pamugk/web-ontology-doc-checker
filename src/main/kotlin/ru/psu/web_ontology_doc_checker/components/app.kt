package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.*
import kotlinx.css.*
import react.*
import ru.psu.web_ontology_doc_checker.model.TermInfo
import ru.psu.web_ontology_doc_checker.services.getTerms
import styled.css

private enum class Tabs(val label: String) {
    DOCUMENTS_PROCESSING("Обработка документов"),
    DICTIONARY("Словарь")
}

private val app = functionalComponent<RProps> { props ->
    var selectedTab by useState<Any>(Tabs.DOCUMENTS_PROCESSING)
    var terms by useState<List<TermInfo>?>(null)

    useEffect {
        if (terms == null) {
            getTerms { loadedTerms -> terms = loadedTerms }
        }
    }

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
            flexDirection = FlexDirection.column
        }
        if (terms == null) {
            mCircularProgress {
                css {
                    margin(LinearDimension.auto)
                }
            }
            mTypography("Идёт загрузка словаря...")
        } else {
            when (selectedTab) {
                Tabs.DOCUMENTS_PROCESSING -> mainPage(terms!!)
                Tabs.DICTIONARY -> dictionaryPage(terms!!)
            }
        }
    }

}

fun RBuilder.app() = child(app) {}
