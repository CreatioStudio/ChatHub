package io.github.zhanganzhi.chatbridge.advancedban;

import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.UUIDManager;

public class AdvancedBan {
    public PunishmentManager getPunishmentManager() {
        return PunishmentManager.get();
    }

    public UUIDManager getUUIDManager() {
        return UUIDManager.get();
    }
}
