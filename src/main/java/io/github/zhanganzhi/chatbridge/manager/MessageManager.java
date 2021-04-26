package io.github.zhanganzhi.chatbridge.manager;

import io.github.zhanganzhi.chatbridge.ChatBridge;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageManager {
    private final ChatBridge chatBridge;

    public MessageManager(ChatBridge chatBridge) {
        this.chatBridge = chatBridge;
    }

    private void sendMessage(String message) {
        for (ProxiedPlayer target : chatBridge.getProxy().getPlayers()) {
            target.sendMessage(new TextComponent(message));
        }
    }

    public void onJoin(String player) {
        sendMessage(chatBridge.getConfigManager().getMessage("join").replace("{player}", player));
    }

    public void onLeave(String player) {
        sendMessage(chatBridge.getConfigManager().getMessage("leave").replace("{player}", player));
    }

    public void onSwitch(String player, String serverFrom, String serverTo) {
        String message = chatBridge.getConfigManager().getMessage("switch").replace("{player}", player);
        message = message.replace("{serverFrom}", chatBridge.getConfigManager().getServerName(serverFrom));
        message = message.replace("{serverTo}", chatBridge.getConfigManager().getServerName(serverTo));
        sendMessage(message);
    }

    public void onChat(String player, String serverOn, String message) {
        String msg = chatBridge.getConfigManager().getMessage("chat").replace("{player}", player);
        msg = msg.replace("{serverOn}", chatBridge.getConfigManager().getServerName(serverOn));
        msg = msg.replace("{message}", message);
        sendMessage(msg);
    }
}
