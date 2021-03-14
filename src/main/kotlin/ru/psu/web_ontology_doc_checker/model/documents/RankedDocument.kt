package ru.psu.web_ontology_doc_checker.model.documents

class RankedItem(
    val termFrom: String, val termTo: String,
    val rank: Double, val rankNorm: Double, val p: Int,
    val u: Double, val e: Double, val bi: Int, val bj: Int
)

class RankedDocument(
    path: String, name: String,
    val rangedItems: List<RankedItem>, val result: Double): AbstractDocument(path, name)