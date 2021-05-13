package vip.creatio.chathub.bungee.manager;

import vip.creatio.chathub.bungee.config.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlinePlayerListManager {
    private static final OnlinePlayerListManager instance = new OnlinePlayerListManager();
    private final HashMap<String, ArrayList<String>> serverOnlinePlayerMap = new HashMap<>();

    private OnlinePlayerListManager() {
    }

    public static OnlinePlayerListManager getInstance() {
        return instance;
    }

    public void init() {
        for (String serverId : ConfigManager.getInstance().getServerNameMap().keySet()) {
            if (serverId.equals("qq")) {
                continue;
            }
            serverOnlinePlayerMap.put(serverId, new ArrayList<>());
        }
    }

    public ArrayList<String> getOnlinePlayerList() {
        ArrayList<String> list = new ArrayList<>();
        for (ArrayList<String> serverList : serverOnlinePlayerMap.values()) {
            list.addAll(serverList);
        }
        return list;
    }

    public HashMap<String, ArrayList<String>> getServerOnlinePlayerMap() {
        return serverOnlinePlayerMap;
    }

    public void removePlayer(String player) {
        for (ArrayList<String> list : serverOnlinePlayerMap.values()) {
            list.remove(player);
        }
    }

    public void setServer(String player, String server) {
        removePlayer(player);
        serverOnlinePlayerMap.get(server).add(player);
    }

    public boolean isOnline(String player) {
        return getOnlinePlayerList().contains(player);
    }

    public String formatStringList(List<String> playerList) {
        StringBuilder stringList = new StringBuilder();
        for (int i = 0; i < playerList.toArray().length; i++) {
            stringList.append(playerList.get(i));
            if (i != playerList.toArray().length - 1) {
                stringList.append(", ");
            }
        }
        return stringList.toString();
    }

    public String getStringServerPlayerList() {
        StringBuilder stringList = new StringBuilder();
        HashMap<String, ArrayList<String>> playerMap = getServerOnlinePlayerMap();
        for (String serverId : playerMap.keySet()) {
            String serverName = ConfigManager.getInstance().getServerName(serverId);
            stringList.append(ConfigManager.getInstance().getListMessage(serverName, playerMap.get(serverId)));
            stringList.append("\n");
        }
        return stringList.toString().trim();
    }
}
