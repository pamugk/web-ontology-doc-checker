package ru.psu.web_ontology_doc_checker.model.documents

abstract class AbstractDocument(val path: String, val name: String) {
    override fun equals(other: Any?): Boolean {
        return other is Document && other.path == path;
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}