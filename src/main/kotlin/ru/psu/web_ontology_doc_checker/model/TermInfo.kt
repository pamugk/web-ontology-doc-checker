package ru.psu.web_ontology_doc_checker.model

import kotlinx.serialization.Serializable

@Serializable
class TermInfo(
    val term: String,
    val forms: List<String>,
    val definition: String
)