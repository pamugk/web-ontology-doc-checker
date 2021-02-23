package ru.psu.web_ontology_doc_checker.model.jont

import kotlinx.serialization.Serializable

@Serializable
class Link(
    /**
     * @return relation unique identifier.
     */
    val id: Int = -1,
    /**
     * @return unique identifier of the relation source node.
     */
    val source_node_id: Int = -1,
    /**
     * @return unique identifier of the relation target node.
     */
    val destination_node_id: Int = -1,
    /**
     * @return relation name.
     */
    val name: String?,
    /**
     * @return attributes, stored exactly in this node.
     */
    val attributes: Map<String, String>,
    val namespace: String)