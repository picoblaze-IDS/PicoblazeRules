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
    String              protocol;
    String              sourcesIp;
    String              sourcesPort;
    String              directions;
    String              destsIp;
    String              destsPort;
    Map<String, String> options;
    private static final String TCP = "tcp";
    private static final String UDP = "udp";
    private static final String ICMP = "icmp";

    private Rule()
    {
    }

    /**
     * Rule constructor
     * @param actions rule actions
     * @param protocol rule protocol
     * @param sourcesIp rule source IP
     * @param sourcesPort rule source port
     * @param directions rule direction
     * @param destsIp rule destination IP
     * @param destsPort rule destination port
     * @param options rule options
     */
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

    /**
     * Rule debug print
     */
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
    
    /**
     * Get rule actions
     * @return the rule actions
     */
    public String getActions() {
        return actions;
    }

    /**
     * Set rule actions
     * @param actions the rule actions
     */
    public void setActions(String actions) {
        this.actions = actions;
    }

    /**
     * Get rule destination IP
     * @return the rule destination IP
     */
    public String getDestsIp() {
        return destsIp;
    }

    /**
     * Set rule destination IP
     * @param destsIp the rule destination IP
     */
    public void setDestsIp(String destsIp) {
        this.destsIp = destsIp;
    }

    /**
     * Get rule destination port
     * @return the rule destination port
     */
    public String getDestsPort() {
        return destsPort;
    }

    /**
     * Set rule destination port
     * @param destsPort the rule destination port
     */
    public void setDestsPort(String destsPort) {
        this.destsPort = destsPort;
    }

    /**
     * Get rule direction
     * @return the rule direction
     */
    public String getDirections() {
        return directions;
    }

    /**
     * Set rule direction
     * @param directions the rule direction
     */
    public void setDirections(String directions) {
        this.directions = directions;
    }

    /**
     * Get rule options
     * @return the rule options
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * Set rule options
     * @param options the rule options
     */
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    /**
     * Get rule protocol
     * @return the rule protocol
     */
    public String getProtocols() {
        return protocol;
    }

    /**
     * Set rule protocol
     * @param protocols the rule protocol
     */
    public void setProtocols(String protocols) {
        this.protocol = protocols;
    }

    /**
     * Get rule source IP
     * @return the rule source IP
     */
    public String getSourcesIp() {
        return sourcesIp;
    }

    /**
     * Set rule source IP
     * @param sourcesIp the rule source IP
     */
    public void setSourcesIp(String sourcesIp) {
        this.sourcesIp = sourcesIp;
    }

    /**
     * Get rule source port
     * @return the rule source port
     */
    public String getSourcesPort() {
        return sourcesPort;
    }

    /**
     * Set rule source port
     * @param sourcesPort the rule source port
     */
    public void setSourcesPort(String sourcesPort) {
        this.sourcesPort = sourcesPort;
    }
    
    /**
     * Get rule protocol number
     * @return the rule protocol number
     */
    public int getProtocolNumber()
    {
        //http://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
        if (this.protocol.equals(TCP)) {
            return 6;
        } else if (this.protocol.equals(UDP)) {
            return 17;
        } else if (this.protocol.equals(ICMP)) {
            return 1;
        }
        return 255;
    }
    
    /**
     * Get "IP from" range
     * @return the "IP from" range
     */
    public byte[] getIpFromRange() {
        return this.getIpRange(this.sourcesIp);
    }
    
    /**
     * Get "IP to" range
     * @return the "IP to" range
     */
    public byte[] getIpToRange() {
        return this.getIpRange(this.destsIp);
    }

    private byte[] getIpRange(String ipRange) {
        byte[] result = new byte[8];
        String ip;
        String ipBinary = "";
        String ipBinaryStart = "";
        String ipBinaryEnd = "";
        String subnet;

        if (ipRange.equals("any")) {
            for (int i = 0; i < 4; i++) {
                result[i] = (byte) 0;
            }
            for (int i = 4; i < 8; i++) {
                result[i] = (byte) 255;
            }
            return result;
        }
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

                result[0] = (byte) Integer.parseInt(ipBinaryStart.substring(0, 8), 2);
                result[1] = (byte) Integer.parseInt(ipBinaryStart.substring(8, 16), 2);
                result[2] = (byte) Integer.parseInt(ipBinaryStart.substring(16, 24), 2);
                result[3] = (byte) Integer.parseInt(ipBinaryStart.substring(24, 32), 2);

                result[4] = (byte) Integer.parseInt(ipBinaryEnd.substring(0, 8), 2);
                result[5] = (byte) Integer.parseInt(ipBinaryEnd.substring(8, 16), 2);
                result[6] = (byte) Integer.parseInt(ipBinaryEnd.substring(16, 24), 2);
                result[7] = (byte) Integer.parseInt(ipBinaryEnd.substring(24, 32), 2);

            }
            else {
                
                for (int i = 0; i < 4; i++) {
                    ipBinary += String.format("%8s", Integer.toBinaryString(Integer.valueOf(ipRange.split("\\.")[i]))).replace(" ", "0");
                }
                
                result[0] = (byte) Integer.parseInt(ipBinary.substring(0, 8), 2);
                result[1] = (byte) Integer.parseInt(ipBinary.substring(8, 16), 2);
                result[2] = (byte) Integer.parseInt(ipBinary.substring(16, 24), 2);
                result[3] = (byte) Integer.parseInt(ipBinary.substring(24, 32), 2);

                result[4] = (byte) Integer.parseInt(ipBinary.substring(0, 8), 2);
                result[5] = (byte) Integer.parseInt(ipBinary.substring(8, 16), 2);
                result[6] = (byte) Integer.parseInt(ipBinary.substring(16, 24), 2);
                result[7] = (byte) Integer.parseInt(ipBinary.substring(24, 32), 2);
                
            }
            
        } catch (Exception e) {
        } finally {
            return result;
        }
    }

    /**
     * Get "port from" range
     * @return the "port from" range
     */
    public byte[] getPortFromRange() {
        return this.getPortRange(this.sourcesPort);
    }

    /**
     * Set "port to" range
     * @return the "port to" range
     */
    public byte[] getPortToRange() {
        return this.getPortRange(this.destsPort);
    }

    private byte[] getPortRange(String portRange) {
        byte[] result = new byte[4];
        String portStart = "";
        String portEnd = "";
        
        if (portRange.equals("any")) {
            for (int i = 0; i < 2; i++) {
                result[i] = (byte) 0;
            }
            for (int i = 2; i < 4; i++) {
                result[i] = (byte) 255;
            }
            return result;
        }
        try {
            if (portRange.contains(":")) {
                portStart = portRange.split(":")[0];
                portEnd = portRange.split(":")[1];
                if (Integer.parseInt(portStart) > Integer.parseInt(portEnd))
                {
                    portEnd = portRange.split(":")[0];                    
                    portStart = portRange.split(":")[1];
                }
                result[0] = this.fillPort(0, Integer.parseInt(portStart));
                result[1] = this.fillPort(1, Integer.parseInt(portStart));
                result[2] = this.fillPort(0, Integer.parseInt(portEnd));
                result[3] = this.fillPort(1, Integer.parseInt(portEnd));
            }
            else {
                result[0] = this.fillPort(0, Integer.parseInt(portRange));
                result[1] = this.fillPort(1, Integer.parseInt(portRange));
                result[2] = this.fillPort(0, Integer.parseInt(portRange));
                result[3] = this.fillPort(1, Integer.parseInt(portRange));
            }
        } catch (Exception e) {
        } finally {
            return result;
        }
    }
    
    private byte fillPort(int i, int port) {
        byte[] result = new byte[2];
        if (port < 255) {
            result[0] = (byte) 0;
            result[1] = (byte) port;
        } else {
            result[0] = (byte) (port >> 8);
            result[1] = (byte) (port & 255);
        }
        return result[i];
    }
    
}
