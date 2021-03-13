package ru.psu.web_ontology_doc_checker.utils

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

fun openFileDialog(callback: (Event) -> Unit) {
    val input = document.createElement("input")
    input.setAttribute("type", "file")
    input.setAttribute("multiple", "multiple")
    input.addEventListener("change", callback)
    input.dispatchEvent(MouseEvent("click"))
}

fun saveFileDialog(contents: dynamic, filename: String, filetype: String) {
    val data = Blob(arrayOf(contents), BlobPropertyBag(filetype))
    val downloadURL = URL.Companion.createObjectURL(data)

    val downloadLink = document.createElement("a")
    downloadLink.setAttribute("href", downloadURL)
    downloadLink.setAttribute("download", filename)
    downloadLink.dispatchEvent(MouseEvent("click"))

    URL.revokeObjectURL(downloadURL)
}