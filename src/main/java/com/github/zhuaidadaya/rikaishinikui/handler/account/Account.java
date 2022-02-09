package com.github.zhuaidadaya.rikaishinikui.handler.account;

import java.util.Random;
import java.util.UUID;

public class Account {
    private String name;
    private String uuid;
    private String type = "offline";

    public Account() {
        this.name = "Player-" + new Random().nextInt(10000);
        this.uuid = UUID.randomUUID().toString();
    }

    public Account(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    public Account(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public Account(String name, String uuid, String type) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
