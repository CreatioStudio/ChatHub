package vip.creatio.ChatBridge.qq;

import vip.creatio.ChatBridge.manager.ConfigManager;

public class QQManager {
    private static final QQManager instance = new QQManager();

    private QQManager() {

    }

    public static QQManager getInstance() {
        return instance;
    }

    public void onChat(String player, String serverOn, String message) {
        if (!ConfigManager.getInstance().isQqEnable()) {
            return;
        }
        String msg = ConfigManager.getInstance().getMessage("chat").replace("{player}", player);
        msg = msg.replace("{serverOn}", ConfigManager.getInstance().getServerName(serverOn));
        msg = msg.replace("{message}", message);
        msg = msg.replaceAll("[&|§].", "");
        for (int groupId : ConfigManager.getInstance().getQqGroupId()) {
            Bot.getInstance().sendGroupMessage(groupId, msg);
        }
    }
}
