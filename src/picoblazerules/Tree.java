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
    private ArrayList<Rule> rules;
    private static final String NAME_ROOT = "";
    static final String CONTENT = "content";
    private static final int OPERATION = 0;
    private static final int CHARACTER = 1;
    private static final int ADDR1 = 2;
    private static final int ADDR2 = 3;
    private static final int OPT_FIRST = 0; // first row of a state, which may indicate a string match
    private static final int OPT_COMPARE = 1; // compare current character
    private static final int OPT_GOTO_AND_DROP = 2; // goto next1:next0 and drop current character
    private static final int OPT_GOTO_AND_RETRY = 3; // goto next1:next0 and retry with current character
    private static final int OPT_NETWORK = 4; // compare Protocol where 'protocol' is 0 = any, 1 = TCP, 2 = UDP, following by the IP FROM Range (8 Bytes), PORT FROM Range (4 Bytes), IP TO Range(8 Bytes), PORT TO Range(4 Bytes)

    /**
     * Tree constructor
     * @param rules The list of rules in order to build the tree
     */
    public Tree(ArrayList<Rule> rules) {
        
        String word;
        this.nodes = new HashMap<String, Node>();
        this.table = new ArrayList<Byte>();
        this.rules = new ArrayList<Rule>();
        this.words = new ArrayList<String>();
        
        this.rules = rules;
        
        for (Rule rule : this.rules)
        {
            if (rule.getOptions() != null && (word = rule.getOptions().get(CONTENT)) != null)
                this.words.add(word);
        }
        
        Collections.sort(this.words);
        
        this.createNodes();
        this.setNodesDictionary();
        this.setNodesNext();
        this.setNodesSuffix();
        
        this.buildTable();
    }
    
    /**
     * Create tree nodes
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
     * Set the node names are in dictionary or not
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
     * Set the next nodes in the tree
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
     * Set the suffix nodes in the tree
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
     * Print the tree
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
     * Get table
     * @return a string that represents the tree
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
    
    /**
     * Print binary table
     */
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
            if (currentNode.getId() > 0)
            {
                for (Rule rule : rules) {
                    if (rule.getOptions().get(CONTENT).equals(currentNode.getName())) {
                        nNode = this.addNetworkConfig(nNode, rule);
                    }
                }
            }
            endAddress = nNode;

            this.nodes.get(key).setStartAdress(startAddress);
            this.nodes.get(key).setEndAddress(endAddress);
        }

        // We complete missing addresses
        this.completeMissingAddresses();
    }
    
    private int addNetworkConfig(int nNode, Rule rule)
    {
        nNode = addProtocol(nNode, rule);
        nNode = addIpFrom(nNode, rule);
        nNode = addPortFrom(nNode, rule);
        nNode = addIpTo(nNode, rule);
        nNode = addPortTo(nNode, rule);
        return nNode;
    }
    
    private int addProtocol(int nNode, Rule rule)
    {
        this.table.add(nNode + OPERATION, (byte)OPT_NETWORK);
        this.table.add(nNode + CHARACTER, (byte)0);
        this.table.add(nNode + ADDR1, (byte)0);
        this.table.add(nNode + ADDR2, (byte)rule.getProtocolNumber());
        nNode +=4;
        return (nNode);
    }
    
    private int addIpFrom(int nNode, Rule rule)
    {
        byte[] ipRange = new byte[8];
        ipRange = rule.getIpFromRange();
        this.table.add(nNode + OPERATION, ipRange[0]);
        this.table.add(nNode + CHARACTER, ipRange[1]);
        this.table.add(nNode + ADDR1, ipRange[2]);
        this.table.add(nNode + ADDR2, ipRange[3]);
        nNode +=4;
        this.table.add(nNode + OPERATION, ipRange[4]);
        this.table.add(nNode + CHARACTER, ipRange[5]);
        this.table.add(nNode + ADDR1, ipRange[6]);
        this.table.add(nNode + ADDR2, ipRange[7]);
        nNode +=4;
        return (nNode);
    }

        private int addIpTo(int nNode, Rule rule)
    {
        byte[] ipRange = new byte[8];
        ipRange = rule.getIpToRange();
        this.table.add(nNode + OPERATION, ipRange[0]);
        this.table.add(nNode + CHARACTER, ipRange[1]);
        this.table.add(nNode + ADDR1, ipRange[2]);
        this.table.add(nNode + ADDR2, ipRange[3]);
        nNode +=4;
        this.table.add(nNode + OPERATION, ipRange[4]);
        this.table.add(nNode + CHARACTER, ipRange[5]);
        this.table.add(nNode + ADDR1, ipRange[6]);
        this.table.add(nNode + ADDR2, ipRange[7]);
        nNode +=4;
        return (nNode);
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
    
    
    /**
     * Get formatted table
     * @return a string that represents the table in a formatted way
     */
    public String getFormattedTable() {
        Node currentNode;
        String result = "";
        
        for (String key : this.getSortedKeyNodes()) {
            currentNode = this.nodes.get(key);
            result += this.getSectionTable(currentNode);
        }
        return result;
    }
    
    /**
     * Get instruction table
     * @return a string that represents the instructions of the table
     */
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
    
    
    /**
     * Get VHDL table
     * @return a string that represents the table as VHDL
     */
    public String getVhdlTable() {
        String result = "";
        String row = "";
        Node node;
        int nRow = 0;
        int currentAdress = 0;

        for (String key : this.getSortedKeyNodes()) {
            node = this.nodes.get(key);
            for (currentAdress = node.getStartAdress(); currentAdress < node.getEndAddress(); currentAdress += 4) {
                if (!(nRow != 0 && nRow % 8 != 0)) {
                    result += "INIT_" + String.format("%02x", currentAdress / 32).toUpperCase() + " => X\"";
                }
                row += String.format("%02x", table.get(currentAdress + OPERATION)).toUpperCase();
                row += String.format("%02x", table.get(currentAdress + CHARACTER)).toUpperCase();
                row += String.format("%02x", table.get(currentAdress + ADDR1)).toUpperCase();
                row += String.format("%02x", table.get(currentAdress + ADDR2)).toUpperCase();
                nRow++;
                if (nRow % 8 == 0) {
                    result += this.invertRow(row) + "\",\n";
                    row = "";
                }
            }
        }

        while (currentAdress % 32 != 0) {
            row += "00000000";
            currentAdress += 4;
        }
        result += this.invertRow(row) + "\",\n";

        // We fill the rest of the RAM until 3F (63)
        /*
        while (currentAdress <= 63 * 32) {
        row = "";
        result += "INIT_" + String.format("%02x", currentAdress / 32).toUpperCase() + " => X\"";
        row = String.format("%064x", 0);
        result += row + "\",\n";
        currentAdress += 32;
        }
         */

        return result.substring(0, result.length() - 2);
    }
    
    private String invertRow(String row) {
        String result = "";
        int i = 0;
        while ((row.length() - 2 - i) >= 0) {
            result += row.substring(row.length() - 2 - i, row.length() - i);
            i += 2;
        }
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
           //result += String.format("0x%02x", table.get(currentAddress + OPERATION));

            if (table.get(currentAddress + OPERATION) == OPT_COMPARE) {
                result += ", " + (char)table.get(currentAddress + CHARACTER).intValue();
            } else {
                result += ", " + (int) table.get(currentAddress + CHARACTER);
            }
            
            //result += ", " + String.format("0x%02x", table.get(currentAddress + CHARACTER));

            if (table.get(currentAddress + OPERATION) == OPT_FIRST || table.get(currentAddress + OPERATION) == OPT_NETWORK) {
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
    
    /**
     * Get hexadecimal formatted table
     * @return a string that represents the table as hexadecimal
     */
    public String getHexaFormattedTable() {
        Node currentNode;
        String result = "";

        for (String key : this.getSortedKeyNodes()) {
            currentNode = this.nodes.get(key);
            result += this.getHexaSectionTable(currentNode);
        }
        return result;
    }
  
    private String getHexaSectionTable(Node node) {
        String result = "";

        if (node.getName().equals("")) {
            result += "// Idle state\n";
        } else {
            result += "// state \"" + node.getName() + "\"\n";
        }
        for (int currentAddress = node.getStartAdress();
                currentAddress < node.getEndAddress(); currentAddress += 4) {
            result += "/* 0x" + Integer.toString(currentAddress, 16) + " */ ";
            result += String.format("0x%02x", table.get(currentAddress + OPERATION));
            result += ", " + String.format("0x%02x", table.get(currentAddress + CHARACTER));
            result += ", " + String.format("0x%02x", table.get(currentAddress + ADDR1));
            result += ", " + String.format("0x%02x", table.get(currentAddress + ADDR2)) + "\n";
        }
        return result + "\n";
    }

    private int addPortFrom(int nNode, Rule rule) {
        byte[] portRange = new byte[4];
        portRange = rule.getPortFromRange();
        this.table.add(nNode + OPERATION, portRange[0]);
        this.table.add(nNode + CHARACTER, portRange[1]);
        this.table.add(nNode + ADDR1, portRange[2]);
        this.table.add(nNode + ADDR2, portRange[3]);
        nNode +=4;
        return (nNode);
    }

    private int addPortTo(int nNode, Rule rule) {
        byte[] portRange = new byte[4];
        portRange = rule.getPortToRange();
        this.table.add(nNode + OPERATION, portRange[0]);
        this.table.add(nNode + CHARACTER, portRange[1]);
        this.table.add(nNode + ADDR1, portRange[2]);
        this.table.add(nNode + ADDR2, portRange[3]);
        nNode +=4;
        return (nNode);
    }
}
