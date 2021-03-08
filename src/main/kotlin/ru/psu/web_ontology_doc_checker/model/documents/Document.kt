package ru.psu.web_ontology_doc_checker.model.documents

class Document(val path: String, val name: String, val text: String) {
    override fun equals(other: Any?): Boolean {
        return other is Document && other.path == path;
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}