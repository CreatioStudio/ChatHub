package vip.creatio.ChatBridge.qq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Info {
    public final JSONObject data;
    public final int time;
    public final String post_type;
    public final String server;
    public final String source_type;
    public final int source_id;
    public final int message_id;
    public final int user_id;
    public final String source_sub_type;
    public final JSONArray raw_content;
    public final String content;
    public final JSONObject sender;
    public final String sender_name;

    public Info(JSONObject data) {
        this.data = data;
        time = data.getInteger("time");
        post_type = data.getString("post_type");
        server = data.getString("server");
        message_id = data.getInteger("message_id");
        user_id = data.getInteger("user_id");
        source_type = data.getString("message_type");
        source_sub_type = data.getString("sub_type");
        raw_content = data.getJSONArray("message");
        String contentTemp = data.getString("raw_message");
        contentTemp = contentTemp.replace("CQ:at,qq=", "@");
        contentTemp = contentTemp.replaceAll("\\[CQ:image,file=.*?]", "[图片]");
        contentTemp = contentTemp.replaceAll("\\[CQ:share,file=.*?]", "[链接]");
        contentTemp = contentTemp.replaceAll("\\[CQ:face,id=.*?]", "[表情]");
        contentTemp = contentTemp.replaceAll("\\[CQ:record,file=.*?]", "[语音]");
        content = contentTemp;
        sender = data.getJSONObject("sender");
        if (source_type.equals("private")) {
            source_id = data.getInteger("user_id");
            sender_name = sender.getString("nickname");
        } else if (source_type.equals("group")) {
            source_id = data.getInteger("group_id");
            String card = sender.getString("card");
            sender_name = card.equals("") ? sender.getString("nickname") : card;
        } else {
            source_id = 0;
            sender_name = "";
        }
    }
}
