package cs451;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Host {
    private static final String IP_START_REGEX = "/";
    private static HashMap<String, Host> hashToHost = new HashMap<String, Host>();
    private static HashMap<Integer, Host> idToHost = new HashMap<Integer, Host>();

    private int id;
    private String ip;
    private int port = -1;
    private String hash;

    public boolean populate(String idString, String ipString, String portString) {
        try {
            id = Integer.parseInt(idString);

            String ipTest = InetAddress.getByName(ipString).toString();
            if (ipTest.startsWith(IP_START_REGEX)) {
                ip = ipTest.substring(1);
            } else {
                ip = InetAddress.getByName(ipTest.split(IP_START_REGEX)[0]).getHostAddress();
            }

            port = Integer.parseInt(portString);
            if (port <= 0) {
                System.err.println("Port in the hosts file must be a positive number!");
                return false;
            }

            hash = InetAddress.getByName(ip).toString() + port;
        } catch (NumberFormatException e) {
            if (port == -1) {
                System.err.println("Id in the hosts file must be a number!");
            } else {
                System.err.println("Port in the hosts file must be a number!");
            }
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return true;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getHash() {
        return hash;
    }

    public static void initLookup(List<Host> hosts) {
        for (Host host : hosts) {
            hashToHost.put(host.getHash(), host);
            idToHost.put(host.getId(), host);
        }
    }

    public static Host hashLookup(String hash) {
        return hashToHost.get(hash);
    }

    public static Host idLookup(int id) {
        return idToHost.get(id);
    }

    public static Set<Integer> idSet() {
        return idToHost.keySet();
    }
}