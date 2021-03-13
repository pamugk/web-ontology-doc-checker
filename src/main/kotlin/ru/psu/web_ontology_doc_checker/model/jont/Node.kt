package ru.psu.web_ontology_doc_checker.model.jont

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Node(
    /**
     * @return node unique identifier.
     */
    val id: Int = -1,
    /**
     * @return node name.
     */
    val name:String?,
    /**
     * @return attributes, stored exactly in this node.
     */
    val attributes: Map<String, String>,
    val namespace: String,
    val position_x: Int,
    val position_y: Int) {
    /**
     * @return internal storage of the node (as a dictionary), which can be used to assign arbitrary Java-objects to the node.
     * This may be useful during the reasoning, for example, to store some particular data in the instance of data source
     * (for future access via ontology) and so on.
     */
    @Transient
    val m_storage: MutableMap<String, Any> = mutableMapOf()

    override fun equals(other: Any?): Boolean {
        return other is Node && id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}