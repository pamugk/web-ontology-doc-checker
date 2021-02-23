package ru.psu.web_ontology_doc_checker.utils

import kotlinx.browser.document
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

fun openFileDialog(callback: (Event) -> Unit) {
    val input = document.createElement("input")
    input.setAttribute("type", "file")
    input.setAttribute("multiple", "multiple")
    input.addEventListener("change", callback)
    input.dispatchEvent(MouseEvent("click"))
}