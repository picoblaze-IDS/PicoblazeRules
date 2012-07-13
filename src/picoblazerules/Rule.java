/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author dc386
 */
public class Rule {
    String              actions;
    String              protocol;
    String              sourcesIp;
    String              sourcesPort;
    String              directions;
    String              destsIp;
    String              destsPort;
    Map<String, String> options;
    private static final String TCP = "tcp";
    private static final String UDP = "udp";

    private Rule()
    {
    }

    public Rule(String actions, String protocol, String sourcesIp, String sourcesPort, String directions, String destsIp, String destsPort, Map<String, String> options) {
        this.actions = actions;
        this.protocol = protocol;
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
                    + "Protocol = [" + protocol + "]\n"
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
        return protocol;
    }

    public void setProtocols(String protocols) {
        this.protocol = protocols;
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
    
    public int getProtocolNumber()
    {
        if (this.protocol.equals(TCP))
            return 1;
        else if (this.protocol.equals(UDP))
            return 2;
        return 0;
    }
    
    public byte[] getIpFromRange() {
        return this.getIpRange(this.sourcesIp);
    }
    
    public byte[] getIpToRange() {
        return this.getIpRange(this.destsIp);
    }
    
    private byte[] getIpRange(String ipRange) {
        byte[] ipFromRange = new byte[8];
        String ip;
        String ipBinary = "";
        String ipBinaryStart = "";
        String ipBinaryEnd = "";
        String subnet;

        try {
            if (ipRange.contains("/")) {
                ip = ipRange.split("/")[0];
                subnet = ipRange.split("/")[1];

                for (int i = 0; i < 4; i++) {
                    ipBinary += String.format("%8s", Integer.toBinaryString(Integer.valueOf(ip.split("\\.")[i]))).replace(" ", "0");
                }

                for (int i = 0; i < ipBinary.length(); i++) {
                    if (i < Integer.valueOf(subnet)) {
                        ipBinaryStart += ipBinary.charAt(i);
                        ipBinaryEnd += ipBinary.charAt(i);
                    } else {
                        ipBinaryStart += "0";
                        ipBinaryEnd += "1";
                    }
                }

                ipFromRange[0] = (byte) Integer.parseInt(ipBinaryStart.substring(0, 8), 2);
                ipFromRange[1] = (byte) Integer.parseInt(ipBinaryStart.substring(8, 16), 2);
                ipFromRange[2] = (byte) Integer.parseInt(ipBinaryStart.substring(16, 24), 2);
                ipFromRange[3] = (byte) Integer.parseInt(ipBinaryStart.substring(24, 32), 2);

                ipFromRange[4] = (byte) Integer.parseInt(ipBinaryEnd.substring(0, 8), 2);
                ipFromRange[5] = (byte) Integer.parseInt(ipBinaryEnd.substring(8, 16), 2);
                ipFromRange[6] = (byte) Integer.parseInt(ipBinaryEnd.substring(16, 24), 2);
                ipFromRange[7] = (byte) Integer.parseInt(ipBinaryEnd.substring(24, 32), 2);

            }
        } catch (Exception e) {
        } finally {
            return ipFromRange;
        }
    }
}
