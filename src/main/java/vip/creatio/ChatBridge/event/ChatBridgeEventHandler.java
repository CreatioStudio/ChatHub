package vip.creatio.ChatBridge.event;

import com.alibaba.fastjson.JSONObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import vip.creatio.ChatBridge.config.ConfigManager;
import vip.creatio.ChatBridge.manager.PlayerListManager;
import vip.creatio.ChatBridge.qq.Bot;
import vip.creatio.ChatBridge.qq.Info;
import vip.creatio.ChatBridge.tool.AdvancedBan;
import vip.creatio.ChatBridge.tool.Message;
import vip.creatio.ChatBridge.tool.Net;

import java.util.regex.Pattern;

public class ChatBridgeEventHandler {
    private static final ChatBridgeEventHandler instance = new ChatBridgeEventHandler();

    private ChatBridgeEventHandler() {

    }

    public static ChatBridgeEventHandler getInstance() {
        return instance;
    }

    public void onJoin(PostLoginEvent event) {
        String player = event.getPlayer().getName();
        PlayerListManager.getInstance().join(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getJoinMessage(player));
        Net.broadcastEvent("/join", player, "", "", "", "");
    }

    public void onLeave(PlayerDisconnectEvent event) {
        String player = event.getPlayer().getName();
        PlayerListManager.getInstance().leave(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getLeaveMessage(player));
        Net.broadcastEvent("/leave", player, "", "", "", "");
    }

    public void onSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        String serverFrom = event.getFrom() == null ? "" : event.getFrom().getName();
        String serverTo = player.getServer().getInfo().getName();
        Message.sendPublicMessage(ConfigManager.getInstance().getSwitchMessage(playerName, serverFrom, serverTo));
        Net.broadcastEvent("/switch", playerName, serverFrom, serverTo, "", "");
    }

    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String playerName = player.getName();
        String serverOn = player.getServer().getInfo().getName();
        String message = event.getMessage();

        // Command and mute
        boolean isCommand = event.isCommand() || event.isProxyCommand();
        boolean isMuted = ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") != null && AdvancedBan.isMuted(player);
        if (isCommand || isMuted) {
            return;
        }

        // Ignore
        for (String ignoreRule : ConfigManager.getInstance().getIgnoreRules()) {
            if (Pattern.matches(ignoreRule, message)) {
                return;
            }
        }

        // Block
        for (String blockWord : ConfigManager.getInstance().getBlockWords()) {
            if (message.contains(blockWord)) {
                Message.sendPrivateMessage(player, ConfigManager.getInstance().getBlockWordMessage(message, blockWord));
                event.setCancelled(true);
                return;
            }
        }

        event.setCancelled(true);
        Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(playerName, serverOn, message));
        Net.broadcastEvent("/chat", playerName, "", "", serverOn, message);
        if (ConfigManager.getInstance().isQqEnable() && ConfigManager.getInstance().isQqMessage()) {
            String msg = ConfigManager.getInstance().getChatMessage(playerName, serverOn, message);
            msg = msg.replaceAll("[&|§].", "");
            Bot.getInstance().sendGroupMessageAll(ConfigManager.getInstance().getQqGroupId(), msg);
        }
    }

    public void onReceiveBroadcastJoin(JSONObject data) {
        String player = data.getString("player");
        PlayerListManager.getInstance().join(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getJoinMessage(player));
    }

    public void onReceiveBroadcastLeave(JSONObject data) {
        String player = data.getString("player");
        PlayerListManager.getInstance().leave(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getLeaveMessage(player));
    }

    public void onReceiveBroadcastSwitch(JSONObject data) {
        String player = data.getString("player");
        String serverFrom = data.getString("serverFrom");
        String serverTo = data.getString("serverTo");
        Message.sendPublicMessage(ConfigManager.getInstance().getSwitchMessage(player, serverFrom, serverTo));
    }

    public void onReceiveBroadcastChat(JSONObject data) {
        String player = data.getString("player");
        String serverOn = data.getString("serverOn");
        String message = data.getString("message");
        Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(player, serverOn, message));
    }

    public void onReceiveQQPost(JSONObject data) {
        if (ConfigManager.getInstance().isQqEnable()) {
            if (data.getString("post_type").equals("message")) {
                onQQMessage(new Info(data));
            }
        }
    }

    public void onQQMessage(Info info) {
        if (!ConfigManager.getInstance().getQqGroupId().contains(info.source_id)) {
            return;
        }
        if (ConfigManager.getInstance().isQqMessage()) {
            Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(info.sender_name, "qq", info.content));
            Net.broadcastEvent("/chat", info.sender_name, "", "", "qq", info.content);
        }
        if (info.content.startsWith("/")) {
            onQQCommand(info);
        }
    }

    public void onQQCommand(Info info) {
        String[] command = info.content.split("\\s+");
        if (command[0].equals("/list")) {
            Bot.getInstance().reply(info, ConfigManager.getInstance().getListMessage());
        } else if (command[0].equals("/mc")) {
            String message = info.content.replace("/mc ", "");
            Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(info.sender_name, "qq", message));
            Net.broadcastEvent("/chat", info.sender_name, "", "", "qq", message);
        }
    }
}
