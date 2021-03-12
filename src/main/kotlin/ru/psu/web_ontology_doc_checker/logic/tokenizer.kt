package ru.psu.web_ontology_doc_checker.logic

private val sentenceRegExp = Regex("(?<=\\s+|^)[\"'‘“'\"[({⟨](.*?[.?!])(\\s[.?!])*[\"'’”'\"\\])}⟩](?=\\s+|\$)|(?<=\\s+|^)\\S(.*?[.?!])(\\s[.?!])*(?=\\s+|\$)")

fun tokenize(text: String): List<String> {
    return sentenceRegExp.findAll(text).iterator().asSequence().map { match -> match.groupValues[0].trim() }.toList()
}