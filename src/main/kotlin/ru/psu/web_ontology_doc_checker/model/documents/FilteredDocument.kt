package ru.psu.web_ontology_doc_checker.model.documents

class FilteredDocument(
    path: String, name: String,
    val terms: Set<String>, val sentences: List<String>): AbstractDocument(path, name)