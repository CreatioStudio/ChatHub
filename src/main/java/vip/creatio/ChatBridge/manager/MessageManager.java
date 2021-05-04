package vip.creatio.ChatBridge.manager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageManager {
    private static final MessageManager instance = new MessageManager();

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return instance;
    }

    private void sendMessage(String message) {
        for (ProxiedPlayer target : ProxyServer.getInstance().getPlayers()) {
            target.sendMessage(new TextComponent(message));
        }
    }

    public void onJoin(String player) {
        sendMessage(ConfigManager.getInstance().getMessage("join").replace("{player}", player));
    }

    public void onLeave(String player) {
        sendMessage(ConfigManager.getInstance().getMessage("leave").replace("{player}", player));
    }

    public void onSwitch(String player, String serverFrom, String serverTo) {
        String message = ConfigManager.getInstance().getMessage("switch").replace("{player}", player);
        message = message.replace("{serverFrom}", ConfigManager.getInstance().getServerName(serverFrom));
        message = message.replace("{serverTo}", ConfigManager.getInstance().getServerName(serverTo));
        sendMessage(message);
    }

    public void onChat(String player, String serverOn, String message) {
        String msg = ConfigManager.getInstance().getMessage("chat").replace("{player}", player);
        msg = msg.replace("{serverOn}", ConfigManager.getInstance().getServerName(serverOn));
        msg = msg.replace("{message}", message);
        sendMessage(msg);
    }
}