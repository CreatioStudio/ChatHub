package vip.creatio.chathub.bungee.manager;

import java.util.HashMap;

public class MessageManager {
    private static final MessageManager instance = new MessageManager();
    private static final HashMap<Integer, Boolean> data = new HashMap<>();
    private int lastId = 0;

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        return instance;
    }

    public int onChat() {
        int thisId = lastId;
        data.put(thisId, false);
        lastId += 1;
        return thisId;
    }

    public void cancelMessage(int id) {
        data.put(id, true);
    }

    public boolean isCanceled(int id) {
        boolean result = data.get(id);
        data.remove(id);
        return result;
    }
}
