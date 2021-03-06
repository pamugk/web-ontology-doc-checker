package ru.psu.web_ontology_doc_checker.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.psu.web_ontology_doc_checker.model.TermInfo

import ru.psu.web_ontology_doc_checker.model.jont.Onto

fun importOntology(ontoText: String): Onto {
    return Json.decodeFromString(serializer(), ontoText)
}

fun exportOntology(ontology: Onto): String {
    return Json.encodeToString(serializer(), ontology)
}