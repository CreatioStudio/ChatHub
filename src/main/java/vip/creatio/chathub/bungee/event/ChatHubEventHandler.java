package vip.creatio.chathub.bungee.event;

import com.alibaba.fastjson.JSONObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import vip.creatio.chathub.bungee.config.ConfigManager;
import vip.creatio.chathub.bungee.manager.MessageManager;
import vip.creatio.chathub.bungee.manager.OnlinePlayerListManager;
import vip.creatio.chathub.bungee.qq.Bot;
import vip.creatio.chathub.bungee.qq.Info;
import vip.creatio.chathub.tool.AdvancedBan;
import vip.creatio.chathub.tool.Message;
import vip.creatio.chathub.tool.Net;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ChatHubEventHandler {
    private static final ChatHubEventHandler instance = new ChatHubEventHandler();

    private ChatHubEventHandler() {
    }

    public static ChatHubEventHandler getInstance() {
        return instance;
    }

    public void onJoin(PostLoginEvent event) {
        String player = event.getPlayer().getName();
        Message.sendPublicMessage(ConfigManager.getInstance().getJoinMessage(player));
        Net.broadcastJoin(player);
    }

    public void onLeave(PlayerDisconnectEvent event) {
        String player = event.getPlayer().getName();
        OnlinePlayerListManager.getInstance().removePlayer(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getLeaveMessage(player));
        Net.broadcastLeave(player);
    }

    public void onSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        String serverFrom = event.getFrom() == null ? "" : event.getFrom().getName();
        String serverTo = player.getServer().getInfo().getName();
        OnlinePlayerListManager.getInstance().setServer(playerName, serverTo);
        Message.sendPublicMessage(ConfigManager.getInstance().getSwitchMessage(playerName, serverFrom, serverTo));
        Net.broadcastSwitch(playerName, serverFrom, serverTo);
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

        event.setCancelled(true);

        // Block
        for (String blockWord : ConfigManager.getInstance().getBlockWords()) {
            if (message.contains(blockWord)) {
                Message.sendPrivateMessage(player, ConfigManager.getInstance().getBlockWordMessage(message, blockWord));
                return;
            }
        }

        new Thread(() -> {
            int messageId = MessageManager.getInstance().onChat();
            Net.broadcastChatTry(playerName, serverOn, message, messageId);
            try {
                Thread.sleep(ConfigManager.getInstance().getBroadcastCancelTimeout());
            } catch (InterruptedException ignored) {
            }
            if (!MessageManager.getInstance().isCanceled(messageId)) {
                Net.broadcastChat(playerName, serverOn, message);
                String msg = ConfigManager.getInstance().getChatMessage(playerName, serverOn, message);
                Message.sendPublicMessage(msg);
                if (ConfigManager.getInstance().isQqEnable() && ConfigManager.getInstance().isQqMessage()) {
                    msg = msg.replaceAll("[&|§].", "");
                    Bot.getInstance().sendGroupMessageAll(ConfigManager.getInstance().getQqGroupId(), msg);
                }
            }
        }).start();
    }

    public void onTabComplete(TabCompleteEvent event) {
        String[] args = event.getCursor().split(" ");
        if (args[0].equals("/chat")) {
            if (args.length == 1) {
                event.getSuggestions().addAll(Arrays.asList("list", "msg", "qq"));
            } else if (args[1].equals("msg")) {
                if (args.length == 2 && event.getCursor().startsWith("/chat msg ")) {
                    event.getSuggestions().addAll(OnlinePlayerListManager.getInstance().getOnlinePlayerList());
                } else if (args.length >= 3) {
                    event.getSuggestions().add("<msg>");
                }
            } else if (args[1].equals("qq")) {
                if (args.length == 2 && event.getCursor().startsWith("/chat qq ")) {
                    event.getSuggestions().add("<msg>");
                }
            }
        }
    }

    public void onReceiveBroadcastJoin(JSONObject data) {
        String player = data.getString("player");
        Message.sendPublicMessage(ConfigManager.getInstance().getJoinMessage(player));
    }

    public void onReceiveBroadcastLeave(JSONObject data) {
        String player = data.getString("player");
        OnlinePlayerListManager.getInstance().removePlayer(player);
        Message.sendPublicMessage(ConfigManager.getInstance().getLeaveMessage(player));
    }

    public void onReceiveBroadcastSwitch(JSONObject data) {
        String player = data.getString("player");
        String serverFrom = data.getString("serverFrom");
        String serverTo = data.getString("serverTo");
        OnlinePlayerListManager.getInstance().setServer(player, serverTo);
        Message.sendPublicMessage(ConfigManager.getInstance().getSwitchMessage(player, serverFrom, serverTo));
    }

    public void onReceiveBroadcastChat(JSONObject data) {
        String player = data.getString("player");
        String serverOn = data.getString("serverOn");
        String message = data.getString("message");
        Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(player, serverOn, message));
    }

    public void onReceiveBroadcastMsg(JSONObject data) {
        String player = data.getString("player");
        String message = data.getString("message");
        String targetName = data.getString("target");
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
        if (target != null) {
            target.sendMessage(new TextComponent(ConfigManager.getInstance().getMsgTargetMessage(player, message)));
        }
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
            Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(info.sender_name, "qq", info.content.replace("mp;", "")));
            Net.broadcastChat(info.sender_name, "qq", info.content);
        }
        if (info.content.startsWith("/")) {
            onQQCommand(info);
        }
    }

    public void onQQCommand(Info info) {
        String[] command = info.content.split("\\s+");
        if (command[0].equals("/list")) {
            Bot.getInstance().reply(info, OnlinePlayerListManager.getInstance().getStringServerPlayerList().replaceAll("[&|§].", ""));
        } else if (command[0].equals("/mc")) {
            String message = info.content.replace("/mc ", "").replace("mp;", "");
            Message.sendPublicMessage(ConfigManager.getInstance().getChatMessage(info.sender_name, "qq", message));
            Net.broadcastChat(info.sender_name, "qq", message);
        }
    }
}
