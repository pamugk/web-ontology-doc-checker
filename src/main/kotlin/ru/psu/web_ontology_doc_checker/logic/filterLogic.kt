package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun filterDocuments(documents: Collection<Document>, ontology: Onto): List<FilteredDocument> {
    return documents.map { doc ->
        val sentences = tokenize(doc.text)
        return@map FilteredDocument(doc.path, doc.name, emptySet(), sentences)
    }
}

private val sentenceRegExp = Regex("(?<=\\s+|^)[\"'‘“'\"[({⟨](.*?[.?!])(\\s[.?!])*[\"'’”'\"\\])}⟩](?=\\s+|\$)|(?<=\\s+|^)\\S(.*?[.?!])(\\s[.?!])*(?=\\s+|\$)")

private fun tokenize(text: String): List<String> {
    return sentenceRegExp.findAll(text).iterator().asSequence().map { match -> match.groupValues[0].trim() }.toList()
}