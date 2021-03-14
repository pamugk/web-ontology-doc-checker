package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.Sentence
import ru.psu.web_ontology_doc_checker.model.jont.Link
import ru.psu.web_ontology_doc_checker.model.jont.Node
import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun filterDocuments(documents: Collection<Document>, ontology: Onto): List<FilteredDocument> {
    return documents.map { doc ->
        val sentences = tokenize(doc.text)
            .withIndex()
            .map { (i, sentence) -> Sentence(i, sentence, bindToOntology(sentence)) }
            .filter { sentence -> sentence.terms.isNotEmpty() }
        val boundTerms = sentences.flatMap { sentence -> sentence.terms }.toSet()
        return@map FilteredDocument(doc.path, doc.name, boundTerms, sentences, buildBoundOntology(boundTerms, ontology))
    }
}

private val regExps = terms.associateBy({it.term}, {it.forms.map { form -> "(^|\\W+)$form(\\W+|$)".toRegex(RegexOption.IGNORE_CASE) }})

private fun bindToOntology(sentence: String): List<String> {
    return terms.map{ termInfo -> termInfo.term }.filter{ term -> regExps[term]!!.any { regex -> regex.matches(sentence)} }
}

private fun buildBoundOntology(boundTerms: Collection<String>, sourceOntology: Onto):Onto {
    val basicNodes = boundTerms.mapNotNull { term -> sourceOntology.getFirstNodeByName(term) }
    val traversedLinks = mutableSetOf<Link>()
    val usedLinks = mutableSetOf<Link>()
    val usedNodes = basicNodes.toMutableSet()
    val path = ArrayDeque<Node>()
    val pathLinks = ArrayDeque<Link>()
    for (node in basicNodes) {
        val otherNodes = basicNodes.minus(node).toSet()
        path.addFirst(node)
        do {
            if (otherNodes.contains(path.first())) {
                usedNodes.addAll(path); usedLinks.addAll(pathLinks)
                path.removeFirst(); pathLinks.removeFirst()
                continue
            }
            val nextLink = sourceOntology.getNodeLinks(path.first()).firstOrNull { link -> !traversedLinks.contains(link) }
            if (nextLink == null) {
                path.removeFirst()
                pathLinks.removeFirstOrNull()
                continue
            }
            traversedLinks.add(nextLink)
            path.addFirst(sourceOntology.getNodeByID(nextLink.destination_node_id)!!); pathLinks.addFirst(nextLink)
        } while (path.isNotEmpty())
    }
    return Onto(null, sourceOntology.namespaces, usedNodes.toList(), usedLinks.toList(),null)
}