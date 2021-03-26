package ru.psu.web_ontology_doc_checker

import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.render
import ru.psu.web_ontology_doc_checker.components.app

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            app()
        }
    }
}
