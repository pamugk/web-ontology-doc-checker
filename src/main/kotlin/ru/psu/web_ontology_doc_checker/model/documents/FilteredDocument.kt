package ru.psu.web_ontology_doc_checker.model.documents

class FilteredDocument(
    val path: String, val name: String,
    val terms: Set<String>, val sentences: List<Sentence>)