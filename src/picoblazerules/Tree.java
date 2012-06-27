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
    private ArrayList<Byte> table;
    private static final String NAME_ROOT = "";
    private static final int OPERATION = 0;
    private static final int CHARACTER = 1;
    private static final int ADDR1 = 2;
    private static final int ADDR2 = 3;
    private static final int OPT_FIRST = 0; // first row of a state, which may indicate a string match
    private static final int OPT_COMPARE = 1; // compare current character
    private static final int OPT_GOTO_AND_DROP = 2; // goto next1:next0 and drop current character
    private static final int OPT_GOTO_AND_RETRY = 3; // goto next1:next0 and retry with current character

    /**
     * 
     * @param words
     */
    public Tree(List<String> words) {
        this.words = words;
        this.nodes = new HashMap<String, Node>();
        this.table = new ArrayList<Byte>();
        
        Collections.sort(this.words);
        
        this.createNodes();
        this.setNodesDictionary();
        this.setNodesNext();
        this.setNodesSuffix();
        
        this.buildTable();
    }
    
    /**
     * 
     */
    private void createNodes()
    {
        for (String word : words)
        {
            for (int i = 0; i <= word.length();i++)
                nodes.put(word.substring(0, i), new Node(word.substring(0, i)));
        }
    }
    
    /**
     * 
     */
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
    
    /**
     * 
     */
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
    
    /**
     * 
     */
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
    
    /**
     * 
     */
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
    
    /**
     * 
     * @return
     */
    public String getTable()
    {
        String result = "";
        String suffix = "";
        String hexaAddr1 = "";
        String hexaAddr2 = "";
        Node node;
        int nRow = 0;

        for (String key : this.getSortedKeyNodes()) {
            node = this.nodes.get(key);
            for (int i = node.getStartAdress(); i < node.getEndAddress(); i += 4) {
                if (nRow != 0) {
                    result += ", ";
                }
                result += (int) table.get(i + OPERATION);

                if (table.get(i + OPERATION) == OPT_COMPARE) {
                    result += ", " + (char)table.get(i + CHARACTER).intValue();
                } else {
                    result += ", " + table.get(i + CHARACTER);
                }

                if (table.get(i + OPERATION) == OPT_FIRST) {
                    suffix = "";
                } else {
                    suffix = "0x";
                }

                result += ", " + (table.get(i + ADDR1) != null ? suffix + Integer.toString(table.get(i + ADDR1), 16) : "NULL");
                result += ", " + (table.get(i + ADDR2) != null ? suffix + Integer.toString(table.get(i + ADDR2), 16) : "NULL");
                
                if (suffix.equals("")) {
                    hexaAddr1 = Integer.toString(table.get(i + ADDR1), 16);
                    hexaAddr2 = Integer.toString(table.get(i + ADDR2), 16);
                } else {
                    hexaAddr1 = Integer.toString(table.get(i + ADDR1) >= 0 ? table.get(i + ADDR1) : 256 + table.get(i + ADDR1), 16);
                    hexaAddr2 = Integer.toString(table.get(i + ADDR2) >= 0 ? table.get(i + ADDR2) : 256 + table.get(i + ADDR2), 16);
                }
                
                nRow++;
            }
        }
        return result;
    }
    
    public void printBinaryTable()
    {
        for (Byte ch : this.table) {
            System.out.write(ch);
        }
        System.out.flush();
    }
    
    private void buildTable() {
        Node currentNode;
        int nNode;
        int startAddress;
        int endAddress;

        nNode = 0;
        for (String key : this.getSortedKeyNodes()) {
            startAddress = nNode;
            currentNode = this.nodes.get(key);
            this.table.add(nNode + OPERATION, (byte)OPT_FIRST);
            this.table.add(nNode + CHARACTER, (byte)0);
            this.table.add(nNode + ADDR1, (byte)0);
            this.table.add(nNode + ADDR2, currentNode.getId().byteValue());
            nNode += 4;
            nNode = addNextToTable(currentNode, this.table, nNode);
            nNode = addSuffixToTable(currentNode, this.table, nNode);
            endAddress = nNode;

            this.nodes.get(key).setStartAdress(startAddress);
            this.nodes.get(key).setEndAddress(endAddress);
        }

        // We complete missing addresses
        this.completeMissingAddresses();
    }
    
    private void completeMissingAddresses(){
        Node currentNode;
        Byte c;
        int address;

        for (String key : this.getSortedKeyNodes()) {
            currentNode = this.nodes.get(key);
            for (int i = currentNode.getStartAdress(); i < currentNode.getEndAddress(); i++) {
                if (this.table.get(i) == null) {
                    c = this.table.get(i - 1);
                    if (c == null) {
                        address = -1;
                    }
                    else if (this.nodes.get(currentNode.getName() + (char)c.intValue()) != null) {
                        address = this.nodes.get(currentNode.getName() + (char)c.intValue()).getStartAdress();
                    } else if (this.nodes.get(currentNode.getName()) != null && this.nodes.get(currentNode.getName()).getSuffix() != null) {
                        address = this.nodes.get(currentNode.getName()).getSuffix().getStartAdress();
                    } else {
                        address = -1;
                    }

                    if (address > 0) {
                        if (address < 255) {
                            this.table.set(i, (byte) 0);
                            this.table.set(i + 1, (byte) address);
                        } else {
                            this.table.set(i, (byte) (address >> 8));
                            this.table.set(i + 1, (byte) (address & 255));
                        }
                    }
                }
            }
        }
    }

    private int addNextToTable(Node currentNode, ArrayList<Byte> table, int nNode)
    {
        Node next;
        
        for(String key : this.getSortedKeyNextNodes(currentNode))
        {
            next = currentNode.getNextNode(key);
            table.add(nNode + OPERATION, (byte) OPT_COMPARE);
            table.add(nNode + CHARACTER, (byte)next.getName().charAt(next.getName().length() - 1));
            table.add(nNode + ADDR1, null);
            table.add(nNode + ADDR2, null);
            nNode += 4;
        }
        return nNode;
    }
    
    private int addSuffixToTable(Node currentNode, ArrayList<Byte> table, int nNode)
    {
        Node suffix;
        
        suffix = currentNode.getSuffix();
        if (currentNode.getName().equals(NAME_ROOT))
            table.add(nNode + OPERATION, (byte) OPT_GOTO_AND_DROP);
        else
            table.add(nNode + OPERATION, (byte) OPT_GOTO_AND_RETRY);
        table.add(nNode + CHARACTER, (byte) 0);
        if (suffix != null && !suffix.getName().equals(NAME_ROOT))
        {
            table.add(nNode + ADDR1, null);
            table.add(nNode + ADDR2, null);
        }
        else
        {
            table.add(nNode + ADDR1, (byte)0);
            table.add(nNode + ADDR2, (byte)0);
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
    
    
    public String getFormattedTable() {
        Node currentNode;
        String result = "";
        
        for (String key : this.getSortedKeyNodes()) {
            currentNode = this.nodes.get(key);
            result += this.getSectionTable(currentNode);
        }
        return result;
    }
    
    public String getInstructionTable()
    {
        String result = "";
        String suffix = "$";
        String hexaAddr1 = "";
        String hexaAddr2 = "";
        Node node;
        int nRow = 0;
        int currentAdress = 0;

        for (String key : this.getSortedKeyNodes()) {
            node = this.nodes.get(key);
            for (currentAdress = node.getStartAdress(); currentAdress < node.getEndAddress(); currentAdress += 4) {
                if (nRow != 0 && nRow % 4 != 0) {
                    result += ", ";
                }
                else
                    result += "rules_rom" + currentAdress + " DSROM " + suffix + Integer.toString(currentAdress, 16) + ", ";
                result += suffix + Integer.toString(table.get(currentAdress + OPERATION), 16) + ", ";

                result += suffix + Integer.toString(table.get(currentAdress + CHARACTER), 16) + ", ";

                hexaAddr1 = Integer.toString(table.get(currentAdress + ADDR1) >= 0 ? table.get(currentAdress + ADDR1) : 256 + table.get(currentAdress + ADDR1), 16);
                hexaAddr2 = Integer.toString(table.get(currentAdress + ADDR2) >= 0 ? table.get(currentAdress + ADDR2) : 256 + table.get(currentAdress + ADDR2), 16);

                result += (table.get(currentAdress + ADDR1) != null ? suffix + hexaAddr1 : "NULL") + ", ";
                result += (table.get(currentAdress + ADDR2) != null ? suffix + hexaAddr2 : "NULL");
                nRow++;
                if (nRow % 4 == 0)
                    result += "\n";
            }
        }
        for (int i = 0; i < (currentAdress % 16); i++)
            result += ", " + suffix + "0";
        return result;
    }
    
    private String getSectionTable(Node node){
        String result = "";
        String suffix = "";
        String hexaAddr1 = "";
        String hexaAddr2 = "";

        if (node.getName().equals("")) {
            result += "// Idle state\n";
        } else {
            result += "// state \"" + node.getName() + "\"\n";
        }

        for (int currentAddress = node.getStartAdress();
                currentAddress < node.getEndAddress(); currentAddress += 4) {
            result += "/* 0x" + Integer.toString(currentAddress, 16) + " */ ";
            result += (int) table.get(currentAddress + OPERATION);

            if (table.get(currentAddress + OPERATION) == OPT_COMPARE) {
                result += ", " + (char)table.get(currentAddress + CHARACTER).intValue();
            } else {
                result += ", " + (int) table.get(currentAddress + CHARACTER);
            }

            if (table.get(currentAddress + OPERATION) == OPT_FIRST) {
                suffix = "";
            } else {
                suffix = "0x";
            }

         
            if (suffix.equals("")) {
                hexaAddr1 = Integer.toString(table.get(currentAddress + ADDR1), 16);
                hexaAddr2 = Integer.toString(table.get(currentAddress + ADDR2), 16);
            } else {
                hexaAddr1 = Integer.toString(table.get(currentAddress + ADDR1) >= 0 ? table.get(currentAddress + ADDR1) : 256 + table.get(currentAddress + ADDR1), 16);
                hexaAddr2 = Integer.toString(table.get(currentAddress + ADDR2) >= 0 ? table.get(currentAddress + ADDR2) : 256 + table.get(currentAddress + ADDR2), 16);
            }

            result += ", " + (table.get(currentAddress + ADDR1) != null ? suffix + hexaAddr1 : "NULL");
            result += ", " + (table.get(currentAddress + ADDR2) != null ? suffix + hexaAddr2 : "NULL") + "\n";
        }
        return result + "\n";
    }
}
