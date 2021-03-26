package ru.psu.web_ontology_doc_checker.components

import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.card.mCardActions
import kotlinx.css.*
import react.RBuilder
import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.resources.doc1
import ru.psu.web_ontology_doc_checker.resources.doc2
import ru.psu.web_ontology_doc_checker.resources.doc3
import styled.css

fun RBuilder.documentCollectionPage(onSelected: (List<Document>) -> Unit) {
    mCardActions {
        css {
            display = Display.flex
            flex(1.0, 1.0)
            flexDirection = FlexDirection.column
        }
        mButton("Демонстрационный корпус документов", onClick = { onSelected(listOf(
            Document("elibrary_37756146_92967049.txt", "elibrary_37756146_92967049.txt", doc1),
            Document("ПОДХОДЫ К ОПИСАНИЮ ТРАНСФОРМАЦИЙ МЕТАМОДЕЛЕЙ.txt", "ПОДХОДЫ К ОПИСАНИЮ ТРАНСФОРМАЦИЙ МЕТАМОДЕЛЕЙ.txt", doc2),
            Document("Проектирование редактора моделей для DSM-платформы.txt", "Проектирование редактора моделей для DSM-платформы.txt", doc3)
        )) })
        mButton("Новый корпус документов", onClick = { onSelected(emptyList()) })
    }
}