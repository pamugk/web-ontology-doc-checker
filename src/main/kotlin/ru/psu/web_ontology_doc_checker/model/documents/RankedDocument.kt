package ru.psu.web_ontology_doc_checker.model.documents

class RankedItem(
    val termFrom: String, val termTo: String,
    val s: Double, val sNorm: Double, val p: Int,
    val u: Double, val e: Double, val b: Double
)

class RankedDocument(
    path: String, name: String,
    val rangedItems: List<RankedItem>, val result: Double): AbstractDocument(path, name)