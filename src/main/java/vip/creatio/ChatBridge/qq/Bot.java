package vip.creatio.ChatBridge.qq;

import com.alibaba.fastjson.JSONObject;
import net.md_5.bungee.api.ProxyServer;
import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.tool.Net;

import java.io.IOException;

public class Bot {
    private static final Bot instance = new Bot();

    private Bot() {
    }

    public static Bot getInstance() {
        return instance;
    }

    public void useAPI(String url, JSONObject data) {
        new Thread(() -> {
            try {
                Net.getInstance().postJSONRequest(url, data);
            } catch (IOException e) {
                ProxyServer.getInstance().getLogger().severe("Cannot use api " + url);
            }
        }).start();
    }

    public String getAPIUrl(String name) {
        return ConfigManager.getInstance().getQqApiURL() + name;
    }

    public void sendGroupMessage(int groupId, String message) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("message", message);
        useAPI(getAPIUrl("/send_group_msg"), data);
    }
}
