package ru.psu.web_ontology_doc_checker.logic

import ru.psu.web_ontology_doc_checker.model.documents.FilteredDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedDocument
import ru.psu.web_ontology_doc_checker.model.documents.RankedItem
import ru.psu.web_ontology_doc_checker.model.documents.Sentence
import ru.psu.web_ontology_doc_checker.model.jont.Link
import ru.psu.web_ontology_doc_checker.model.jont.Node
import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun rankDocuments(b: Int, K: Int, N: Int, strictRange: Boolean, docs: List<FilteredDocument>): List<RankedDocument> {
    return docs.map { filteredDoc ->
        val rankedItems = efreqRnum(b, K, N, strictRange, filteredDoc)
        val result = rankedItems.sumOf { it.rankNorm } / rankedItems.size
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
    val concordanceCountedPaths = concordances.distinct().map { it to countPaths(it.from, it.to, doc.boundOntology, N) }
        .filter { it.second > 0 }.toMap()
    val countedConcordances = concordances.filter { concordanceCountedPaths.containsKey(it) }.groupingBy { it }.eachCount()
    val weightyLinksCount = countedConcordances.entries.filter { (_, count) -> count > b }.groupingBy { it.key.from }.eachCount()
    val avgWeights = weightyLinksCount.map { (term, count) -> term to 1.0 * count / doc.terms.size }.toMap()
    val rankedPairs = countedConcordances.keys.map { it to
            concordanceCountedPaths[it]!! * 2.0 * avgWeights[it.from]!! * countedConcordances[it]!! /
            (weightyLinksCount[it.from]!! + weightyLinksCount[it.to]!!)
    }
    val ranks = rankedPairs.map { it.second }
    val minRank = ranks.minOrNull()!!; val maxRank = ranks.maxOrNull()!!
    return rankedPairs.sortedBy { it.first }
        .filterOutDuplicates()
        .map { (pair, rank) -> RankedItem(pair.from, pair.to, rank, (rank - minRank) / maxRank, 0, avgWeights[pair.from]!!,
        countedConcordances[pair]!!.toDouble(), weightyLinksCount[pair.from]!!, weightyLinksCount[pair.to]!! ) }
}

private fun List<Pair<Concordance, Double>>.filterOutDuplicates(): Map<Concordance, Double> {
    val map = mutableMapOf<Concordance, Double>()
    for ((pair, rank) in this) {
        if (!map.contains(Concordance(pair.to, pair.from))) {
            map[pair] = rank
        }
    }
    return map
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

private fun countPaths(from: String, to: String, ontology: Onto, N: Int): Int {
    val traversedLinks = mutableSetOf<Link>()
    val path = ArrayDeque<Node>()
    path.addFirst(ontology.getFirstNodeByName(from)!!)
    var pathsCount = 0
    do {
        if (path.size - 1 > N) {
            path.removeFirst()
            continue
        }
        if (to == path.first().name) {
            pathsCount++
            path.removeFirst()
            continue
        }
        val nextLink = ontology.getLinksFrom(path.first()).firstOrNull { link -> !traversedLinks.contains(link) }
        if (nextLink == null) {
            path.removeFirst()
            continue
        }
        traversedLinks.add(nextLink)
        path.addFirst(ontology.getNodeByID(nextLink.destination_node_id)!!)
    } while (path.isNotEmpty())
    return pathsCount
}