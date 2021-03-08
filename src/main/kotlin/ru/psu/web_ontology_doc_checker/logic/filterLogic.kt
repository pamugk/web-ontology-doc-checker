package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun filterDocuments(documents: Collection<Document>, ontology: Onto): List<FilteredDocument> {
    return documents.map { doc -> FilteredDocument(doc.path, doc.name, emptySet(), emptyList()) }
}