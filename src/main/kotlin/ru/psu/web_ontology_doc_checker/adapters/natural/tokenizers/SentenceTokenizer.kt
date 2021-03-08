package ru.psu.web_ontology_doc_checker.adapters.natural.tokenizers

@JsModule("tokenizers")
external class SentenceTokenizer {
    fun tokenize(text: String): List<String>
}