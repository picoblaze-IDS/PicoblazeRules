/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

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
        return ("");
    }
}
