/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author dc386
 */
public class Rule {
    String              actions;
    String              protocols;
    String              sourcesIp;
    String              sourcesPort;
    String              directions;
    String              destsIp;
    String              destsPort;
    Map<String, String> options;

    private Rule()
    {
    }

    public Rule(String actions, String protocols, String sourcesIp, String sourcesPort, String directions, String destsIp, String destsPort, Map<String, String> options) {
        this.actions = actions;
        this.protocols = protocols;
        this.sourcesIp = sourcesIp;
        this.sourcesPort = sourcesPort;
        this.directions = directions;
        this.destsIp = destsIp;
        this.destsPort = destsPort;
        this.options = options;
    }

    public void print()
    {
            System.out.println("[Rules]\n"
                    + "Action = [" + actions + "]\n"
                    + "Protocol = [" + protocols + "]\n"
                    + "Source Ip = [" + sourcesIp + "]\n"
                    + "Source Port = [" + sourcesPort + "]\n"
                    + "Direction = [" + directions + "]\n"
                    + "Destination IP = [" + destsIp + "]\n"
                    + "Destination Port = [" + destsPort + "]");
            for (Entry<String, String> option : options.entrySet())
            {
                System.out.print("Option = " + option.getKey() + ":" + option.getValue() + "\n");
            }
            System.out.println("=========\n");

    }
    
    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getDestsIp() {
        return destsIp;
    }

    public void setDestsIp(String destsIp) {
        this.destsIp = destsIp;
    }

    public String getDestsPort() {
        return destsPort;
    }

    public void setDestsPort(String destsPort) {
        this.destsPort = destsPort;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    public String getSourcesIp() {
        return sourcesIp;
    }

    public void setSourcesIp(String sourcesIp) {
        this.sourcesIp = sourcesIp;
    }

    public String getSourcesPort() {
        return sourcesPort;
    }

    public void setSourcesPort(String sourcesPort) {
        this.sourcesPort = sourcesPort;
    }
}
