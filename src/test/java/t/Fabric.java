package t;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric.FabricMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.FileDownloader;
import org.json.JSONObject;

public class Fabric {
    public static void main(String[] args) {
        String loader = "https://meta.fabricmc.net/v2/versions/loader";
        String test_loader = "https://meta.fabricmc.net/v2/versions/loader/1.18.2/0.13.3/profile/json";
        String parse = NetworkUtil.downloadToStringBuilder(test_loader).toString();
        FabricMinecraftLibrariesParser parser = new FabricMinecraftLibrariesParser(new JSONObject(parse), "", "1.18.2", "0.13.3");
        new FileDownloader().downloadFiles(parser.getLibrariesDownloads());
        System.out.println(parse);
    }
}
