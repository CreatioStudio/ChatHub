package vip.creatio.ChatBridge.tool;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message {
    public static void sendPublicMessage(String message) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(message));
        }
    }

    public static void sendPrivateMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(message));
    }
}
