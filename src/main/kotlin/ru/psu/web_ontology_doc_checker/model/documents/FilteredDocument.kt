package ru.psu.web_ontology_doc_checker.model.documents

import ru.psu.web_ontology_doc_checker.model.jont.Onto

class FilteredDocument(
    path: String, name: String,
    val terms: Set<String>, val sentences: List<Sentence>,
    val boundOntology: Onto): AbstractDocument(path, name)