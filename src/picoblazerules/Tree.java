/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author dc386
 */
public class Tree {
    
    private List<String> words;
    private Map<String, Node> nodes;
    private static final String NAME_ROOT = "";
    private static final int OPERATION = 0;
    private static final int CHARACTER = 1;
    private static final int ADDR1 = 2;
    private static final int ADDR2 = 3;

    public Tree(List<String> words) {
        this.words = words;
        this.nodes = new HashMap();
        
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
        for (Entry<String, Node> node : nodes.entrySet())
        {
            if (words.contains(node.getKey()))
            {
                nodes.get(node.getKey()).setInDictionary(Boolean.TRUE);
                nodes.get(node.getKey()).setId(i++);
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
        for(Entry<String, Node> node : nodes.entrySet())
        {
            System.out.print("("+node.getKey()+")" + " ["+node.getValue().getId()+"]");
            for (Entry<String, Node> next : node.getValue().getNext().entrySet())
            {
                System.out.print(" {" + next.getKey() + "}");     
            }
            if (node.getValue().getSuffix() != null)
                System.out.print("|" + node.getValue().getSuffix().getName() + "|");
            System.out.println();
        }
    }
    
    public String getTable()
    {
        ArrayList<Character> table = new ArrayList<Character>();
        Node currentNode;
        int nNode;
        
        nNode = 0;
        for(Entry<String, Node> node : nodes.entrySet())
        {
            currentNode = node.getValue();
            table.add(nNode + OPERATION, '0');
            table.add(nNode + CHARACTER,'0');
            table.add(nNode + ADDR1, '0');
            table.add(nNode + ADDR2, currentNode.getId().toString().charAt(0));
            nNode += 4;
            nNode = addNextToTable(currentNode, table, nNode);
            nNode = addSuffixToTable(currentNode, table, nNode);
        }
        for (int i = 0; i < table.size();i+=4)
        {
            System.out.print("[" + table.get(i + OPERATION));
            System.out.print(" " + table.get(i + CHARACTER));
            System.out.print(" " + table.get(i + ADDR1));
            System.out.println(" " + table.get(i + ADDR2) + "]");
        }
        return table.toString();
    }
    
    private int addNextToTable(Node currentNode, ArrayList<Character> table, int nNode)
    {
        Map<String, Node> nexts;
        
        nexts = currentNode.getNext();
        for(Entry<String, Node> next : nexts.entrySet())
        {
            table.add(nNode + OPERATION, '1');  
            table.add(nNode + CHARACTER, next.getValue().getName().charAt(next.getValue().getName().length() - 1));
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
}
