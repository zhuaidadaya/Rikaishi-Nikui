package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class MinecraftLaunchInformation {
    private MinecraftVersionInformation versionInformation;
    private String os;
    private JavaVersionInformation java = new JavaVersionInformation("java","",true);
    private Account account = new Account();
    private LinkedHashMap<String,VmOption>vmOptions;

    public MinecraftLaunchInformation(MinecraftVersionInformation information, String os, JavaVersionInformation java, Account account) {
        versionInformation = information;
        this.os = os;
        this.java = java;
        this.account = account;
        vmOptions = information.getVmOptions();
    }

    public String getOs() {
        return os;
    }

    public Account getAccount() {
        return account;
    }

    public LinkedHashMap<String,VmOption> getVmOptions() {
        return vmOptions;
    }

    public void setVmOptions(LinkedHashMap<String,VmOption> vmOptions) {
        this.vmOptions = vmOptions;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public JavaVersionInformation getJava() {
        return java;
    }

    public void setJava(JavaVersionInformation java) {
        this.java = java;
    }

    public MinecraftLaunchInformation(JSONObject json) {
        apply(json);
    }

    public MinecraftVersionInformation getVersionInformation() {
        return versionInformation;
    }

    public void setVersionInformation(MinecraftVersionInformation information) {
        this.versionInformation = information;
    }

    public void apply(JSONObject json) {
        this.versionInformation = new MinecraftVersionInformation(json.getJSONObject("version_information"));
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("version_information",versionInformation.toJSONObject());
        json.put("java", java);
        json.put("os", os);

        return json;
    }
}
