package ru.psu.web_ontology_doc_checker.model.jont

import kotlinx.serialization.Serializable

@Serializable
class Onto(val last_id: Int,
           val namespaces:Map<String, String>,
           val nodes: List<Node>,
           val relations: List<Link>,
           val visualize_ont_path: String?) {

    /**
     * @param id - unique identifier of node.
     * @return node with given unique identifier. If no one node matches the given identifier, null is returned.
     */
    fun getNodeByID(id: Int): Node? = nodes.singleOrNull { node -> node.id == id }

    /**
     * @param id - unique identifier of relation.
     * @return relation with given unique identifier. If no one relation matches the given identifier, null is returned.
     */
    fun getLinkByID(id: Int): Link? = relations.singleOrNull { link -> link.id == id }

    /**
     * @param name - name of node.
     * @return array of nodes matching the given name.
     * There can be more than one, since the name is not necessary unique within the ontology.
     * If no one node matches the given name, array will be empty.
     */
    fun getNodesByName(name: String): List<Node> = nodes.filter { node -> node.name == name }

    /**
     * @param name - name of node.
     * @return first node matching the given name. If no one node matches the given name, null is returned.
     */
    fun getFirstNodeByName(name: String): Node? = nodes.singleOrNull { node -> node.name == name }

    /**
     * @param node - node to find relations from.
     * @param linkName - name of the relation.
     * @return array of nodes, which are connected with the given one by the relation (link) with given name.
     * The direction of relation (link) is from the given node to nodes returned.
     */
    fun getNodesLinkedFrom(node: Node, linkName: String): List<Node> =
        relations.filter { link -> link.source_node_id == node.id && link.name == linkName }
            .mapNotNull { link -> getNodeByID(link.destination_node_id) }

    /**
     * @param node - node to find relations to.
     * @param linkName - name of the relation.
     * @return array of nodes, which are connected with the given one by the relation (link) with given name.
     * The direction of relation (link) is from nodes returned to the given node.
     */
    fun getNodesLinkedTo(node: Node, linkName: String): List<Node> =
        relations.filter { link -> link.destination_node_id == node.id && link.name == linkName }
            .mapNotNull { link -> getNodeByID(link.source_node_id) }

    /**
     * @param node - node to find relations from.
     * @param linkName - name of the relation.
     * @param typeName - name of the type defining node.
     * @return array of nodes, which are connected with the given one by the relation (link) with given name
     * and are connected by is_a to the node with given name (say, have given type).
     * The direction of relation (link) is from the given node to nodes returned.
     */
    fun getTypedNodesLinkedFrom(node: Node, linkName: String, typeName: String): List<Node> =
        getNodesLinkedFrom(node, linkName).map { lNode -> getNodesLinkedFrom(lNode, "is_a") }
            .flatten().filter { proto -> proto.name == typeName }

    /**
     * @param node - node to find relations to.
     * @param linkName - name of the relation.
     * @param typeName - name of the type defining node.
     * @return array of nodes, which are connected with the given one by the relation (link) with given name
     * and are connected by is_a to the node with given name (say, have given type).
     * The direction of relation (link) is from nodes returned to the given node.
     */
    fun getTypedNodesLinkedTo(node: Node, linkName: String, typeName: String): List<Node> =
        getNodesLinkedTo(node, linkName).map { lNode -> getNodesLinkedFrom(lNode, "is_a") }
            .flatten().filter { proto -> proto.name == typeName }

    /**
     * @param node - node to check type of.
     * @param typeName - name of the type defining node.
     * @return true if node has direct is_a connection with node of given name (say, has given type), false otherwise.
     */
    fun isNodeOfType(node: Node, typeName: String): Boolean =
        getNodesLinkedFrom(node, "is_a").any { proto -> proto.name == typeName }

    /**
     * @param node - node to check type of.
     * @param typeName - name of the type defining node.
     * @return true if node has is_a connection with node of given name (say, has given type) at any inheritance level, false otherwise.
     */
    fun isNodeOfTypeRecursive(node: Node, typeName: String): Boolean =
        getNodesLinkedFrom(node, "is_a").any {
                proto -> proto.name == typeName || isNodeOfTypeRecursive(proto, typeName) }

    /**
     * @param node - node to check type of.
     * @param linkName - name of the relation.
     * @param typeName - name of the type defining node.
     * @return true if node has connection (link) with given name with another node,
     * which is connected by is_a to the node with given name (say, has given type) at any inheritance level, false otherwise.
     * The direction of relation (link) is from the given node to nodes returned.
     */
    fun isNodeOfTypeRecursiveFrom(node: Node, linkName: String, typeName: String): Boolean =
        getNodesLinkedFrom(node, linkName).any { lNode -> isNodeOfTypeRecursive(lNode, typeName) }

    /**
     * @param node - node to check type of.
     * @param linkName - name of the relation.
     * @param typeName - name of the type defining node.
     * @return true if node has connection (link) with given name with another node,
     * which is connected by is_a to the node with given name (say, has given type) at any inheritance level, false otherwise.
     * The direction of relation (link) is from nodes returned to the given node.
     */
    fun isNodeOfTypeRecursiveTo(node: Node, linkName: String, typeName: String): Boolean =
        getNodesLinkedTo(node, linkName).any { lNode -> isNodeOfTypeRecursive(lNode, typeName) }

    /**
     * @param node - node to get attribute's value of.
     * @param name - name of the attribute.
     * @return value of the requested attribute of the given node, taking into account the inheritance.
     * The inheritance means that if given node has no attribute requested, it is going to be searched upwards by the
     * is_a links. If the entire hierarchy of nodes contains no attribute with the given name, null is returned.
     */
    fun getInheritedAttributeOfNode(node: Node, name: String): String? {
        val result = node.attributes.getOrElse(name, { null })
        if (result != null) return result
        return getNodesLinkedFrom(node, "is_a")
            .mapNotNull { proto -> getInheritedAttributeOfNode(proto, name) }.firstOrNull()
    }
}