/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 *
 * @author dc386
 */
public class Tree {
    
    private List<String> words;
    private Map<String, Node> nodes;
    private ArrayList<Character> table;
    private static final String NAME_ROOT = "";
    private static final int OPERATION = 0;
    private static final int CHARACTER = 1;
    private static final int ADDR1 = 2;
    private static final int ADDR2 = 3;

    public Tree(List<String> words) {
        this.words = words;
        this.nodes = new HashMap<String, Node>();
        this.table = new ArrayList<Character>();
        
        Collections.sort(this.words);
        
        this.createNodes();
        this.setNodesDictionary();
        this.setNodesNext();
        this.setNodesSuffix();
    }

    private void createNodes()
    {
        for (String word : words)
        {
            for (int i = 0; i <= word.length();i++)
                nodes.put(word.substring(0, i), new Node(word.substring(0, i)));
        }
    }
    
    private void setNodesDictionary()
    {
        int i = 1;
        for (String key : this.getSortedKeyNodes())
        {
            if (words.contains(key))
            {
                nodes.get(key).setInDictionary(Boolean.TRUE);
                nodes.get(key).setId(i++);
            }
        }
    }
    
    private void setNodesNext()
    {
        for (Entry<String, Node> node : nodes.entrySet())
        {
            if (!node.getKey().equals(NAME_ROOT))
            {
                String parentName = node.getKey().substring(0, node.getKey().length()-1);
                
                nodes.get(parentName).addNextNode(nodes.get(node.getKey()));
            }
        }
    }
    
    private void setNodesSuffix()
    {
        for (Entry<String, Node> node : nodes.entrySet())
        {
            if (!node.getKey().equals(NAME_ROOT))
            {
                String suffixName;
                for (int i=1; i <= node.getKey().length(); i++)
                {
                    suffixName = node.getKey().substring(i);
                    if (nodes.get(suffixName) != null)
                    {
                        nodes.get(node.getKey()).setSuffix(nodes.get(suffixName));
                        break;
                    }
                }
            }
        }
    }
    
    public void print()
    {
        Node node;
        
        for(String key : this.getSortedKeyNodes())
        {
            node = this.nodes.get(key);
            System.out.print("("+key+")" + " ["+node.getId()+"]");
            for(String nextKey : this.getSortedKeyNextNodes(node))
            {
                System.out.print(" {" + nextKey + "}");
            }
            if (node.getSuffix() != null)
                System.out.print("|" + node.getSuffix().getName() + "|");
            System.out.println();
        }
    }
    
    public String getTable()
    {
        Node currentNode;
        int nNode;
        int startAddress;
        int endAddress;
        
        nNode = 0;
        for(String key : this.getSortedKeyNodes())
        {
            startAddress = nNode;
            currentNode = this.nodes.get(key);
            this.table.add(nNode + OPERATION, '0');
            this.table.add(nNode + CHARACTER,'0');
            this.table.add(nNode + ADDR1, '0');
            this.table.add(nNode + ADDR2, currentNode.getId().toString().charAt(0));
            nNode += 4;
            nNode = addNextToTable(currentNode, this.table, nNode);
            nNode = addSuffixToTable(currentNode, this.table, nNode);
            endAddress = nNode;
            this.printRowTable(startAddress, endAddress, key);
        }
        return table.toString();
    }
    
    private int addNextToTable(Node currentNode, ArrayList<Character> table, int nNode)
    {
        Node next;
        
        for(String key : this.getSortedKeyNextNodes(currentNode))
        {
            next = currentNode.getNextNode(key);
            table.add(nNode + OPERATION, '1');
            table.add(nNode + CHARACTER, next.getName().charAt(next.getName().length() - 1));
            table.add(nNode + ADDR1, null);
            table.add(nNode + ADDR2, null);
            nNode += 4;
        }
        return nNode;
    }
    
    private int addSuffixToTable(Node currentNode, ArrayList<Character> table, int nNode)
    {
        Node suffix;
        
        suffix = currentNode.getSuffix();
        if (currentNode.getName().equals(NAME_ROOT))
            table.add(nNode + OPERATION, '2');
        else
            table.add(nNode + OPERATION, '3');
        table.add(nNode + CHARACTER,'0');
        if (suffix != null && !suffix.getName().equals(NAME_ROOT))
        {
            table.add(nNode + ADDR1, null);
            table.add(nNode + ADDR2, null);
        }
        else
        {
            table.add(nNode + ADDR1, '0');
            table.add(nNode + ADDR2, '0');
        }
        nNode += 4;
        return nNode;
    }

    private TreeSet<String> getSortedKeyNodes() {
        TreeSet<String> keys = new TreeSet<String>(this.nodes.keySet());
        return keys;
    }
    
    private TreeSet<String> getSortedKeyNextNodes(Node node) {
        TreeSet<String> keys = new TreeSet<String>(node.getNext().keySet());
        return keys;
    }
    
    void printRowTable(int startAddress, int endAddress, String state){
        if (state.equals("")) {
            System.out.println("// Idle State");
        }
        else {
            System.out.println("// state \""+state+"\"");
        }
        System.out.println("// addr \"0x"+Integer.toString(startAddress, 16)+"\"");
        for (int i = startAddress; i < endAddress;i+=4)
        {
            System.out.print("[" + table.get(i + OPERATION));
            System.out.print(" " + table.get(i + CHARACTER));
            System.out.print(" " + table.get(i + ADDR1));
            System.out.println(" " + table.get(i + ADDR2) + "]");
        }
        System.out.println();
    }
}
