package vip.creatio.ChatBridge.qq;

import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.manager.MessageManager;

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
        if (info.content.startsWith("/mc")) {
            MessageManager.getInstance().onQqChat(info.sender_name, info.content.replace("/mc ", ""));
        }
    }
}
