package ru.psu.web_ontology_doc_checker

import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            child(App::class) {
                attrs { }
            }
        }
    }
}
