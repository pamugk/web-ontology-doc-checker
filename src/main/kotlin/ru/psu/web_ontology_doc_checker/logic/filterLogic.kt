package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument

fun filterDocuments(documents: Collection<Document>): List<FilteredDocument> {
    return documents.map { doc ->
        val boundTerms = mutableSetOf<String>()
        val sentences = tokenize(doc.text).filter { sentence ->
            val boundToSentenceTerms = bindToOntology(sentence)
            boundTerms.addAll(boundToSentenceTerms)
            return@filter boundToSentenceTerms.isNotEmpty()
        }
        return@map FilteredDocument(doc.path, doc.name, boundTerms, sentences)
    }
}