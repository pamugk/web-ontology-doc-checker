package ru.psu.web_ontology_doc_checker.components

import kotlinx.browser.document
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

fun openFileDialog(callback: (Event) -> Unit) {
    val input = document.createElement("input")
    input.setAttribute("type", "file")
    input.addEventListener("change", callback)
    input.dispatchEvent(MouseEvent("click"))
}