package ru.psu.web_ontology_doc_checker.services

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.psu.web_ontology_doc_checker.model.TermInfo
import kotlin.js.Console

private val httpClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

fun getTerms(onLoaded: (List<TermInfo>) -> Unit) {
    GlobalScope.launch {
        onLoaded(httpClient.get(path = "/dictionary.json"))
    }
}