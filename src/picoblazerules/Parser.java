/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dc386
 */
public final class Parser {
    
    List<String>    actions;
    List<String>    protocols;
    List<String>    sourcesIp;
    List<String>    sourcesPort;
    List<String>    directions;
    List<String>    destsIp;
    List<String>    destsPort;
    List<Map<String, String>>       options;
    ArrayList<Rule>      rules;
    int             nRules;
    String          fileName;
    String          fileLine = "";
    BufferedReader  buffer = null;
    private static final int ACTION = 0;
    private static final int PROTOCOL = 1;
    private static final int SRCIP = 2;
    private static final int SRCPORT = 3;
    private static final int DIRECTION = 4;
    private static final int DSTIP = 5;
    private static final int DSTPORT = 6;
    private static final int OPTION = 7;
    
    private Parser()
    {
    }
        
    /**
     * Parser constructor
     * @param _fileName file rule path
     */
    public Parser(String _fileName)
    {
        try
        {
            String line;

            actions = new ArrayList<String>();
            protocols = new ArrayList<String>();
            sourcesIp = new ArrayList<String>();
            sourcesPort = new ArrayList<String>();
            directions = new ArrayList<String>();
            destsIp = new ArrayList<String>();
            destsPort = new ArrayList<String>();
            options = new ArrayList<Map<String, String>>();
            rules = new ArrayList<Rule>();

            buffer = new BufferedReader(new FileReader(_fileName));
            while ((line = buffer.readLine()) != null)
            {
                if (line.trim().isEmpty())
                    continue;
                int section = -1;
                for (String retval: line.split(" ", 8))
                {
                    section++;
                    switch (section){
                        case ACTION: actions.add(retval.toLowerCase());
                            break;
                        case PROTOCOL: protocols.add(retval.toLowerCase());
                            break;
                        case SRCIP: sourcesIp.add(retval);
                            break;
                        case SRCPORT: sourcesPort.add(retval);
                            break;
                        case DIRECTION: directions.add(retval);
                            break;
                        case DSTIP: destsIp.add(retval);
                            break;
                        case DSTPORT: destsPort.add(retval);
                            break;
                        case OPTION: options.add(parseOption(retval));
                            break;
                        default:
                            break;
                    }
                }
                nRules++;
            }
            buffer.close();
        }
        catch (IOException ex) {
            System.out.println("Error: File not found.");
            System.exit(0);
        }
        for (int i = 0; i < nRules; i++)
            rules.add(new Rule(actions.get(i), protocols.get(i), sourcesIp.get(i), sourcesPort.get(i), directions.get(i), destsIp.get(i), destsPort.get(i), options.get(i)));
    }

    /**
     * Parse the rule option part
     * @param options the option string
     * @return a map of option with option key as key and option value as value
     */
    public Map<String, String> parseOption(String options)
    {
        Map<String, String> optionMap = new HashMap<String, String>();
        
        if (options.isEmpty())
            return (null);
        options = options.substring(1, options.length()-1);
        for (String option: options.split(";"))
        {
            int i = 0;
            String key = "";
            for (String value: option.split(":"))
            {
                value = value.trim();
                value = value.replaceAll("\"", "");
                if (i % 2 == 0)
                    key = value;
                else
                    optionMap.put(key.toLowerCase(), value.toLowerCase());
                i++;
            }
        }
        return (optionMap);
    }

    /**
     * Get actions
     * @return a list of actions
     */
    public List<String> getAction() {
        return actions;
    }

    /**
     * Set actions
     * @param actions the list of actions
     */
    public void setAction(List<String> actions) {
        this.actions = actions;
    }

    /**
     * Get buffer
     * @return the buffer
     */
    public BufferedReader getBuffer() {
        return buffer;
    }

    /**
     * Set buffer
     * @param buffer the buffer
     */
    public void setBuffer(BufferedReader buffer) {
        this.buffer = buffer;
    }

    /**
     * Get destination IPs
     * @return a list of destination IPs
     */
    public List<String> getDestIp() {
        return destsIp;
    }

    /**
     * Set destination IPs
     * @param destsIp a list of destination IPs
     */
    public void setDestIp(List<String> destsIp) {
        this.destsIp = destsIp;
    }

    /**
     * Get destination ports
     * @return a list of destination ports
     */
    public List<String> getDestPort() {
        return destsPort;
    }

    /**
     * Set destination ports
     * @param destsPort a list of destination ports
     */
    public void setDestPort(List<String> destsPort) {
        this.destsPort = destsPort;
    }

    /**
     * Get fileline
     * @return the fileline
     */
    public String getFileLine() {
        return fileLine;
    }

    /**
     * Set fileline
     * @param fileLine the fileline
     */
    public void setFileLine(String fileLine) {
        this.fileLine = fileLine;
    }

    /**
     * Get file name
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set file name
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Get options
     * @return a list of options
     */
    public List<Map<String, String>> getOption() {
        return options;
    }

    /**
     * Set options
     * @param options a list of options
     */
    public void setOption(List<Map<String, String>> options) {
        this.options = options;
    }

    /**
     * Get protocols
     * @return a list of protocols
     */
    public List<String> getProtocol() {
        return protocols;
    }

    /**
     * Set protocols
     * @param protocols a list of protocols
     */
    public void setProtocol(List<String> protocols) {
        this.protocols = protocols;
    }

    /**
     * Get source IPs
     * @return a list of source IPs
     */
    public List<String> getSourceIp() {
        return sourcesIp;
    }

    /**
     * Set source IPs
     * @param sourcesIp a list of source IPs
     */
    public void setSourceIp(List<String> sourcesIp) {
        this.sourcesIp = sourcesIp;
    }

    /**
     * Get source ports
     * @return a list of source ports
     */
    public List<String> getSourcePort() {
        return sourcesPort;
    }

    /**
     * Set source ports
     * @param sourcesPort a list of source ports
     */
    public void setSourcePort(List<String> sourcesPort) {
        this.sourcesPort = sourcesPort;
    }

    /**
     * Get the number of rules
     * @return the number of rules
     */
    public int getnRules() {
        return nRules;
    }

    /**
     * Set the number of rules
     * @param nRules the number of rules
     */
    public void setnRules(int nRules) {
        this.nRules = nRules;
    }

    /**
     * Get the rules
     * @return the rules
     */
    public ArrayList<Rule> getRules() {
        return rules;
    }

    /**
     * Set the rules
     * @param rules the rules
     */
    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }
}
