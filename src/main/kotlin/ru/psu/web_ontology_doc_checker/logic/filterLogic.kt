package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.Document
import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.Sentence
import ru.psu.web_ontology_doc_checker.model.jont.Link
import ru.psu.web_ontology_doc_checker.model.jont.Node
import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun filterDocuments(documents: Collection<Document>, ontology: Onto, N: Int): List<FilteredDocument> {
    return documents.map { doc ->
        val sentences = tokenize(doc.text)
            .withIndex()
            .map { (i, sentence) -> Sentence(i, sentence, bindToOntology(sentence, ontology)) }
            .filter { sentence -> sentence.terms.isNotEmpty() }
        val boundTerms = sentences.flatMap { sentence -> sentence.terms }.toSet()
        return@map FilteredDocument(doc.path, doc.name, boundTerms, sentences, buildBoundOntology(boundTerms, ontology, N))
    }
}

private val regExps = terms.associateBy({it.term}, {it.forms.map { form -> "(^|\\W+)$form(\\W+|$)".toRegex(RegexOption.IGNORE_CASE) }})

private fun bindToOntology(sentence: String, ontology: Onto): List<String> {
    return terms.map{ termInfo -> termInfo.term }.filter{ term ->
        ontology.getFirstNodeByName(term) != null && regExps[term]!!.any { regex -> regex.matches(sentence)}
    }
}

private fun buildBoundOntology(boundTerms: Collection<String>, sourceOntology: Onto, N: Int):Onto {
    val basicNodes = boundTerms.mapNotNull { term -> sourceOntology.getFirstNodeByName(term) }
    val usedLinks = mutableSetOf<Link>()
    val usedNodes = basicNodes.toMutableSet()
    basicNodes.forEach { basicNode -> buildBoundOntologyInner(
        basicNodes.minus(basicNode).toSet(), sourceOntology, N,
        usedLinks, usedNodes, ArrayDeque(listOf(basicNode)), ArrayDeque()
    ) }
    return Onto(null, sourceOntology.namespaces, usedNodes.toList(), usedLinks.toList(),null)
}

private fun buildBoundOntologyInner(
    targetNodes: Set<Node>, sourceOntology: Onto, N: Int,
    usedLinks: MutableSet<Link>, usedNodes: MutableSet<Node>,
    path: ArrayDeque<Node>, pathLinks: ArrayDeque<Link>) {
    if (path.size > N) {
        return
    }
    if (targetNodes.contains(path.first())) {
        usedNodes.addAll(path); usedLinks.addAll(pathLinks)
        return
    }
    sourceOntology.getNodeLinks(path.first())
        .filter { link -> !pathLinks.contains(link) }
        .map { link -> link to
                if (link.source_node_id == path.first().id)
                    sourceOntology.getNodeByID(link.destination_node_id)!!
                else sourceOntology.getNodeByID(link.source_node_id)!!
        }
        .forEach { (nextLink, nextNode) ->
            path.addFirst(nextNode); pathLinks.addFirst(nextLink)
            buildBoundOntologyInner(targetNodes, sourceOntology, N, usedLinks, usedNodes, path, pathLinks)
            path.removeFirst(); pathLinks.removeFirstOrNull()
        }
}