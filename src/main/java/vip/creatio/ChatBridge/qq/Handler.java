package vip.creatio.ChatBridge.qq;

import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.manager.MessageManager;
import vip.creatio.ChatBridge.manager.PlayerListManager;

public class Handler {
    private static final Handler instance = new Handler();

    public static Handler getInstance() {
        return instance;
    }

    public void onMessage(Info info) {
        if (ConfigManager.getInstance().isQqMessage() && ConfigManager.getInstance().getQqGroupId().contains(info.source_id)) {
            MessageManager.getInstance().onQqChat(info.sender_name, info.content);
        }
        if (info.content.startsWith("/")) {
            onCommand(info);
        }
    }

    public void onCommand(Info info) {
        String[] command = info.content.split("/s+");
        if (command[0].equals("/list")) {
            String message = "在线玩家共" + PlayerListManager.getInstance().getPlayerList().toArray().length + "人，玩家列表: " + PlayerListManager.getInstance().getStringPlayerList();
            Bot.getInstance().sendGroupMessage(info.source_id, message);
        } else if (command[0].equals("/mc")) {
            MessageManager.getInstance().onQqChat(info.sender_name, info.content.replace("/mc ", ""));
        }
    }
}
