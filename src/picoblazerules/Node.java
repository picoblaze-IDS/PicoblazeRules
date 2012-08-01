/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dc386
 */
public class Node {

    private String name;
    private Map<String, Node> next;
    private Node suffix;
    private Boolean inDictionary;
    private Integer id;
    private Integer startAdress;
    private Integer endAddress;

    /**
     * Node constructor
     * @param name
     */
    public Node(String name) {
        this.name = name;
        this.next = new HashMap<String, Node>();
        this.suffix = null;
        this.inDictionary = false;
        this.id = 0;
        this.startAdress = 0;
        this.endAddress = 0;
    }

    /**
     * Get the end address of a section (state)
     * @return the end address of a section (state)
     */
    public Integer getEndAddress() {
        return endAddress;
    }

    /**
     * Set the end address of a section (state)
     * @param endAddress
     */
    public void setEndAddress(Integer endAddress) {
        this.endAddress = endAddress;
    }

    /**
     * Get the start address of a section (state)
     * @return the start address of a section (state)
     */
    public Integer getStartAdress() {
        return startAdress;
    }

    /**
     * Set the start address of a section (state)
     * @param startAdress
     */
    public void setStartAdress(Integer startAdress) {
        this.startAdress = startAdress;
    }

    /**
     * Add a node to the current node
     * @param _node the node to add
     */
    public void addNextNode(Node _node)
    {
        this.next.put(_node.getName(), _node);
    }

    /**
     * Get the next node of the current node by its name
     * @param _nextNodeName the next node name
     * @return the next node
     */
    public Node getNextNode(String _nextNodeName)
    {
        return (this.next.get(_nextNodeName));
    }
    
    /**
     * Get node id
     * @return the node id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set node id
     * @param id the node id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get inDictionary (if the node name is in the dictionary)
     * @return true if the node name is in the dictionary, false otherwise
     */
    public Boolean getInDictionary() {
        return inDictionary;
    }

    /**
     * Set inDictionary (if the node name is in the dictionary)
     * @param inDictionary true if the node name is in the dictionary, false otherwise
     */
    public void setInDictionary(Boolean inDictionary) {
        this.inDictionary = inDictionary;
    }

    /**
     * Get node name
     * @return the node name
     */
    public String getName() {
        return name;
    }

    /**
     * Set node name
     * @param name the node name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the all next nodes of the current node
     * @return a map with the next node name as key and the next node as value
     */
    public Map<String, Node> getNext() {
        return next;
    }

    /**
     * Set the all next nodes of the current node
     * @param next a map with the next node name as key and the next node as value
     */
    public void setNext(Map<String, Node> next) {
        this.next = next;
    }

    /**
     * Get the suffix node of the current node
     * @return the suffix node
     */
    public Node getSuffix() {
        return suffix;
    }

    /**
     * Set the suffix node of the current node
     * @param suffix the suffix node
     */
    public void setSuffix(Node suffix) {
        this.suffix = suffix;
    }
}
