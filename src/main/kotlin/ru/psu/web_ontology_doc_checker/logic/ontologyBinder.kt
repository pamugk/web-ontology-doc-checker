package ru.psu.web_ontology_doc_checker.logic

private val regExps = terms.associateBy({it.term}, {it.forms.map { form -> "(^|\\W+)$form(\\W+|$)".toRegex(RegexOption.IGNORE_CASE) }})

fun bindToOntology(sentence: String): List<String> {
    return terms.map{ termInfo -> termInfo.term }.filter{ term -> regExps[term]!!.any { regex -> regex.matches(sentence)} }
}