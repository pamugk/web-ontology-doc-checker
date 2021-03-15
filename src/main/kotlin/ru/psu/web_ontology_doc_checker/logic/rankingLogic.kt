package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedItem
import ru.psu.web_ontology_doc_checker.model.documents.Sentence
import ru.psu.web_ontology_doc_checker.model.jont.Node
import ru.psu.web_ontology_doc_checker.model.jont.Onto
import kotlin.math.sqrt

fun rankDocuments(b: Int, K: Int, N: Int, strictRange: Boolean, docs: List<FilteredDocument>): List<RankedDocument> {
    return docs.map { filteredDoc ->
        val rankedItems = efreqRnum(b, K, N, strictRange, filteredDoc)
        val result = if (rankedItems.isNotEmpty()) rankedItems.sumOf { it.rankNorm } / rankedItems.size else 0.0
        return@map RankedDocument(filteredDoc.path, filteredDoc.name, rankedItems, result)
    }.sortedWith(compareBy({-it.result}, {it.name}))
}

private data class Concordance(val from: String, val to: String): Comparable<Concordance> {
    override fun compareTo(other: Concordance): Int {
        val comparedFrom = from.compareTo(other.from)
        return if (comparedFrom == 0) to.compareTo(other.to) else comparedFrom
    }
}

private infix fun String.with(that: String): Concordance = Concordance(this, that)

private fun efreqRnum(b: Int, K: Int, N: Int, strictRange: Boolean, doc: FilteredDocument): List<RankedItem> {
    val concordances = if (strictRange) findPairsOfStrictlyConnectedTerms(K, doc) else findPairsOfConnectedTerms(K, doc)
    val concordanceCountedPaths = concordances.distinct()
        .map { it to countPaths(it.to, doc.boundOntology, N, ArrayDeque(listOf(doc.boundOntology.getFirstNodeByName(it.from)!!))) }
        .filter { it.second > 0 }.toMap()
    val countedConcordances = concordances.filter { concordanceCountedPaths.containsKey(it) }.groupingBy { it }.eachCount()
    val weightyLinksCount = countedConcordances.entries.filter { (_, count) -> count > b }.groupingBy { it.key.from }.eachCount()
    val avgWeights = weightyLinksCount.map { (term, count) -> term to 1.0 * count / doc.terms.size }.toMap()
    val rankedPairs = countedConcordances.keys.filter { weightyLinksCount.containsKey(it.from) }.map { it to
            sqrt(concordanceCountedPaths[it]!!.toDouble()) * 2.0 * avgWeights[it.from]!! * countedConcordances[it]!! /
            (weightyLinksCount[it.from]!! + weightyLinksCount[it.to]!!)
    }.toMap()
    val ranks = rankedPairs.map { it.value }
    val minRank = ranks.minOrNull()?:0.0; val maxRank = ranks.maxOrNull()?:1.0

    return concordanceCountedPaths.keys.sortedBy { it }
        .filterOutDuplicates()
        .map { pair -> RankedItem(pair.from, pair.to,
            rankedPairs[pair]?:0.0, (rankedPairs[pair]?:0.0 - minRank) / maxRank,
            concordanceCountedPaths[pair]!!, avgWeights[pair.from]?:0.0,
        countedConcordances[pair]!!.toDouble(), weightyLinksCount[pair.from]?:0, weightyLinksCount[pair.to]?:0 ) }
}

private fun List<Concordance>.filterOutDuplicates(): List<Concordance> {
    val pairSet = mutableSetOf<Concordance>()
    for (pair in this) {
        if (!pairSet.contains(Concordance(pair.to, pair.from))) {
            pairSet.add(pair)
        }
    }
    return pairSet.toList()
}

private fun buildConcordances(sentenceFrom: Sentence, sentenceTo: Sentence): List<Concordance> {
    return sentenceFrom.terms.flatMap { term ->
        sentenceTo.terms.minus(term).flatMap { otherTerm -> listOf(term with otherTerm, otherTerm with term) }
    }
}

private fun findPairsOfConnectedTerms(K: Int, doc: FilteredDocument): List<Concordance> {
    val pairsOfTerms = mutableListOf<Concordance>()
    for (i in doc.sentences.sortedBy { it.number }.indices) {
        var j = i
        do {
            pairsOfTerms.addAll(buildConcordances(doc.sentences[i], doc.sentences[j]))
            j++
        } while(j < doc.sentences.size && doc.sentences[j].number - doc.sentences[i].number < K)
    }
    return pairsOfTerms
}

private fun findPairsOfStrictlyConnectedTerms(K: Int, doc: FilteredDocument): List<Concordance> {
    val pairsOfTerms = mutableListOf<Concordance>()
    for (i in doc.sentences.sortedBy { it.number }.indices) {
        var j = i
        while(j < doc.sentences.size && doc.sentences[j].number - doc.sentences[i].number != K) {
            j++
        }
        if (j != doc.sentences.size) {
            pairsOfTerms.addAll(buildConcordances(doc.sentences[i], doc.sentences[j]))
        }
    }
    return pairsOfTerms
}

private fun countPaths(to: String, ontology: Onto, N: Int, path: ArrayDeque<Node>): Int =
    if (path.size - 1 > N) 0
    else {
        if (to == path.first().name) 1
        else ontology
            .getNodesLinkedFrom(path.first())
            .plus(ontology.getNodesLinkedTo(path.first()))
            .filter { node -> !path.contains(node) }
            .map { node ->
                path.addFirst(node)
                val count = countPaths(to, ontology, N, path)
                path.removeFirst()
                return@map count
            }
            .sum()
    }