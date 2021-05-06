package vip.creatio.ChatBridge.config;

import vip.creatio.ChatBridge.manager.PlayerListManager;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager extends Config {
    private static final ConfigManager instance = new ConfigManager();

    private ConfigManager() {
        super();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public String getJoinMessage(String player) {
        HashMap<String, String> map = new HashMap<>();
        map.put("{player}", player);
        return getFormattedMessage("join", map);
    }

    public String getLeaveMessage(String player) {
        HashMap<String, String> map = new HashMap<>();
        map.put("{player}", player);
        return getFormattedMessage("leave", map);
    }

    public String getSwitchMessage(String player, String serverFrom, String serverTo) {
        HashMap<String, String> map = new HashMap<>();
        map.put("{player}", player);
        map.put("{serverFrom}", getServerName(serverFrom));
        map.put("{serverTo}", getServerName(serverTo));
        return getFormattedMessage("switch", map);
    }

    public String getChatMessage(String player, String serverOn, String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put("{player}", player);
        map.put("{serverOn}", getServerName(serverOn));
        map.put("{message}", message.replace('&', '§'));
        return getFormattedMessage("chat", map);
    }

    public String getBlockWordMessage(String message, String blockWord) {
        HashMap<String, String> map = new HashMap<>();
        map.put("{message}", message);
        map.put("{blockWord}", blockWord);
        return getFormattedMessage("block_word", map);
    }

    public String getListMessage() {
        HashMap<String, String> map = new HashMap<>();
        map.put("{playerCount}", PlayerListManager.getInstance().getPlayerList().toArray().length + "");
        map.put("{playerList}", PlayerListManager.getInstance().getStringPlayerList());
        return getFormattedMessage("list", map);
    }

    public String getFormattedMessage(String key, Map<String, String> map) {
        String message = getMessageMap().get(key);
        for (String name : map.keySet()) {
            message = message.replace(name, map.get(name));
        }
        return message;
    }

    public String getServerName(String serverId) {
        String serverName = getServerNameMap().get(serverId);
        return serverName == null ? serverId : serverName;
    }
}
