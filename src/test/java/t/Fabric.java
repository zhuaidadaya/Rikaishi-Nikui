package t;

import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;

public class Fabric {
    public static void main(String[] args) {
        String loader = "https://meta.fabricmc.net/v2/versions/loader";
        String test_loader = "https://meta.fabricmc.net/v2/versions/loader/1.18.2/0.13.3/profile/json";
        String parse = NetworkUtil.downloadToStringBuilder(test_loader).toString();
        System.out.println(parse);
    }
}
