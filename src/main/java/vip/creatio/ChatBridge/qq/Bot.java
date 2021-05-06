package vip.creatio.ChatBridge.qq;

import com.alibaba.fastjson.JSONObject;
import vip.creatio.ChatBridge.config.ConfigManager;
import vip.creatio.ChatBridge.tool.Net;

import java.util.List;

public class Bot {
    private static final Bot instance = new Bot();

    private Bot() {
    }

    public static Bot getInstance() {
        return instance;
    }

    public void useAPI(String url, JSONObject data) {
        new Thread(() -> Net.postJSONRequest(url, data)).start();
    }

    public String getAPIUrl(String name) {
        return ConfigManager.getInstance().getQqApiURL() + name;
    }

    public void reply(Info info, String message) {
        if (info.source_type.equals("private")) {
            sendPrivateMessage(info.source_id, message);
        } else if (info.source_type.equals("group")) {
            sendGroupMessage(info.source_id, message);
        }
    }

    public void sendPrivateMessage(int userId, String message) {
        JSONObject data = new JSONObject();
        data.put("user_id", userId);
        data.put("message", message);
        useAPI(getAPIUrl("/send_private_msg"), data);
    }

    public void sendGroupMessage(int groupId, String message) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("message", message);
        useAPI(getAPIUrl("/send_group_msg"), data);
    }

    public void sendGroupMessageAll(List<Integer> groupIdList, String message) {
        for (int groupId : groupIdList) {
            sendGroupMessage(groupId, message);
        }
    }
}
