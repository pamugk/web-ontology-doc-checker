package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedItem

fun rankDocuments(b: Int, K: Int, N: Int, docs: List<FilteredDocument>): List<RankedDocument> {
    return docs.map { filteredDoc ->
        val rankedItems = efreqRnum(b, K, N, filteredDoc)
        val result = rankedItems.sumOf { it.sNorm } / rankedItems.size
        return@map RankedDocument(filteredDoc.path, filteredDoc.name, rankedItems, result)
    }.sortedWith(compareBy({-it.result}, {it.name}))
}

private fun efreqRnum(b: Int, K: Int, N: Int, doc: FilteredDocument): List<RankedItem> {
    for (i in doc.sentences.indices) {
        doc.sentences[i].terms
    }
    return emptyList()
}