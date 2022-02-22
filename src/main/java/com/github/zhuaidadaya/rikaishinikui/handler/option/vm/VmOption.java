package com.github.zhuaidadaya.rikaishinikui.handler.option.vm;

import org.json.JSONObject;

import java.util.UUID;

public class VmOption {
    private String id = UUID.randomUUID().toString();
    private String description = "";
    private String pairHead = "";
    private String pairEnd = "";
    private boolean isEnable = false;
    private boolean isOption = false;
    private boolean isPair = true;

    public VmOption(String detail) {
        this.isPair = false;
        setDetail(detail);
    }

    public VmOption(String pairHead, String pairEnd) {
        this.pairHead = pairHead;
        this.pairEnd = pairEnd;
    }

    public VmOption(JSONObject json) {
        apply(json);
    }

    public String getDetail() {
        return pairHead + pairEnd;
    }

    public void setDetail(String detail) {
        if(detail.contains("=")) {
            isPair = true;
            pairHead = detail.substring(0, detail.indexOf("=") + 1);
            pairEnd = detail.substring(detail.lastIndexOf("=") + 1);
        } else {
            isPair = false;
            pairHead = detail;
            pairEnd = "";
        }
    }

    public String getDescription() {
        return description.replace("=", "");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void apply(JSONObject json) {
        this.isPair = json.getBoolean("pair");
        this.isOption = json.getBoolean("opt");
        this.pairHead = json.getString("pair-head");
        this.pairEnd = json.getString("pair-end");
        String detail = pairHead + pairEnd;
        if(isOption) {
            this.isEnable = json.getBoolean("enable");
        }
        if(detail.startsWith("-XX:")) {
            isOption = true;
            isEnable = detail.startsWith("-XX:+");
        }
        this.description = json.getString("description");
        this.id = json.getString("id");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("opt", isOption);
        json.put("pair", isPair);
        json.put("pair-head", pairHead);
        json.put("pair-end", pairEnd);
        if(isOption) {
            json.put("enable", isEnable);
        }

        if(isOption) {
            json.put("description", "vm.option." + getDetail().replace("-XX:+", "-XX:").replace("-XX:-", "-XX:"));
        } else {
            json.put("description", "vm.option." + getName().replace("=", ""));
        }

        json.put("id", id);
        return json;
    }

    public String getName() {
        return pairHead;
    }

    public String toString() {
        if(getName().matches("((-Xss)|(-Xm[xns]))[0-9]+[TGMKB]")) {
            return getName().substring(0, 4);
        } else {
            String result = pairHead.replace("=", "").replace("-XX:+", "").replace("-XX:-", "");
            if(result.startsWith("-D")) {
                result = result.replaceFirst("-D", "");
            }
            if(result.startsWith("-")) {
                result = result.replaceFirst("-", "");
            }
            return result;
        }
    }

    public String getPairHead() {
        return pairHead;
    }

    public void setPairHead(String pairHead) {
        this.pairHead = pairHead;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isOption() {
        return isOption;
    }

    public void setOption(boolean option) {
        isOption = option;
    }

    public String getPairEnd() {
        return pairEnd;
    }

    public void setPairEnd(String pairEnd) {
        this.pairEnd = pairEnd;
    }

    public boolean isPair() {
        return isPair;
    }

    public void setPair(boolean pair) {
        isPair = pair;
    }

    public static String getExample(String input) {
        return switch(input.toLowerCase()) {
            case "g1gc" -> "-XX:+UseG1GC";
            case "server" -> "-server";
            case "client" -> "-client";
            default -> "";
        };
    }
}
