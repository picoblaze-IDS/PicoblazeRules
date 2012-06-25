/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    List<Map>       options;
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
        
    public Parser(String _fileName) throws IOException
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
            options = new ArrayList<Map>();

            buffer = new BufferedReader(new FileReader(_fileName));
            while ((line = buffer.readLine()) != null)
            {
                int section = -1;
                for (String retval: line.split(" ", 8))
                {
                    section++;
                    switch (section){
                        case ACTION: actions.add(retval);
                            break;
                        case PROTOCOL: protocols.add(retval);
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
        catch(FileNotFoundException exc)
        {
            System.out.println("Error: File not found.");
        }
    }
    
    public Map<String, String> parseOption(String options)
    {
        Map<String, String> optionMap = new HashMap();
        
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
                    optionMap.put(key, value);
                i++;
            }
        }
        return (optionMap);
    }
    
    public void print()
    {
        Map<String, String> optionsMap;
        for (int i = 0; i < getnRules(); i++)
        {
            System.out.println("[Rules " + i + "]\n"
                    + "Action = [" + actions.get(i) + "]\n"
                    + "Protocol = [" + protocols.get(i) + "]\n"
                    + "Source Ip = [" + sourcesIp.get(i) + "]\n"
                    + "Source Port = [" + sourcesPort.get(i) + "]\n"
                    + "Direction = [" + directions.get(i) + "]\n"
                    + "Destination IP = [" + destsIp.get(i) + "]\n"
                    + "Destination Port = [" + destsPort.get(i) + "]");
            optionsMap = options.get(i);
            for (Entry<String, String> option : optionsMap.entrySet())
            {
                System.out.print("Option = " + option.getKey() + ":" + option.getValue() + "\n");
            }
            System.out.println("=========\n");
        }
    }
    
    public List<String> getAction() {
        return actions;
    }

    public void setAction(List<String> actions) {
        this.actions = actions;
    }

    public BufferedReader getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferedReader buffer) {
        this.buffer = buffer;
    }

    public List<String> getDestIp() {
        return destsIp;
    }

    public void setDestIp(List<String> destsIp) {
        this.destsIp = destsIp;
    }

    public List<String> getDestPort() {
        return destsPort;
    }

    public void setDestPort(List<String> destsPort) {
        this.destsPort = destsPort;
    }

    public String getFileLine() {
        return fileLine;
    }

    public void setFileLine(String fileLine) {
        this.fileLine = fileLine;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public List<Map> getOption() {
        return options;
    }

    public void setOption(List<Map> options) {
        this.options = options;
    }

    public List<String> getProtocol() {
        return protocols;
    }

    public void setProtocol(List<String> protocols) {
        this.protocols = protocols;
    }

    public List<String> getSourceIp() {
        return sourcesIp;
    }

    public void setSourceIp(List<String> sourcesIp) {
        this.sourcesIp = sourcesIp;
    }

    public List<String> getSourcePort() {
        return sourcesPort;
    }

    public void setSourcePort(List<String> sourcesPort) {
        this.sourcesPort = sourcesPort;
    }

    public int getnRules() {
        return nRules;
    }

    public void setnRules(int nRules) {
        this.nRules = nRules;
    }
}
