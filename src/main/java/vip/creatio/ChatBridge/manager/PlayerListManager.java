package vip.creatio.ChatBridge.manager;

import java.util.LinkedList;

public class PlayerListManager {
    private static final PlayerListManager instance = new PlayerListManager();
    private final LinkedList<String> playerList = new LinkedList<>();

    private PlayerListManager() {
    }

    public static PlayerListManager getInstance() {
        return instance;
    }


    public void join(String player) {
        if (!playerList.contains(player)) {
            playerList.add(player);
        }
    }

    public void leave(String player) {
        playerList.remove(player);
    }

    public LinkedList<String> getPlayerList() {
        return playerList;
    }

    public String getStringPlayerList() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < getPlayerList().toArray().length; i++) {
            list.append(getPlayerList().get(i));
            if (i != getPlayerList().toArray().length - 1) {
                list.append(", ");
            }
        }
        return list.toString();
    }
}
