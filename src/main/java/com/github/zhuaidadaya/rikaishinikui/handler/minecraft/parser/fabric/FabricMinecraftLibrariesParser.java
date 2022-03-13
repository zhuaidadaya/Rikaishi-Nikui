package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric;

import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.loader.FabricLoaderInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.LibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class FabricMinecraftLibrariesParser extends LibrariesParser {
    private final JSONArray libraries;
    private final String area;
    private final String gameVersion;
    private final String loaderVersion;
    private FabricLoaderInformation fabricLoader = new FabricLoaderInformation();

    public FabricMinecraftLibrariesParser(JSONObject json, String area, String gameVersion) {
        libraries = json.getJSONArray("libraries");
        this.area = area;
        this.gameVersion = gameVersion;
        detectFabricVersion();
        this.loaderVersion = fabricLoader.getVersion();
    }

    public FabricMinecraftLibrariesParser(JSONArray json, String area, String gameVersion) {
        libraries = json;
        this.area = area;
        this.gameVersion = gameVersion;
        detectFabricVersion();
        this.loaderVersion = fabricLoader.getVersion();
    }

    public FabricLoaderInformation getFabricLoader() {
        return fabricLoader;
    }

    public void detectFabricVersion() {
        for (FabricMinecraftLibraryParser parser : getLibraries().values()) {
            String name = parser.getName();
            if (name.contains("net.fabricmc:fabric-loader:")) {
                this.fabricLoader = new FabricLoaderInformation().setVersion(name.substring(27)).setFabric(true);
            }
        }
    }

    public Map<String, FabricMinecraftLibraryParser> getLibraries() {
        Object2ObjectRBTreeMap<String, FabricMinecraftLibraryParser> libs = new Object2ObjectRBTreeMap<>();
        for (Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());
            try {
                FabricMinecraftLibraryParser libraryParser = new FabricMinecraftLibraryParser(json, gameVersion, loaderVersion);

                if (libraryParser.getLibrary() != null) {
                    libs.put(libraryParser.getUrl(), libraryParser);
                }
            } catch (Exception e2) {

            }
        }
        return libs;
    }

    public Set<NetworkFileInformation> getLibrariesDownloads() {
        Map<String, FabricMinecraftLibraryParser> libs = getLibraries();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for (String s : libs.keySet()) {
            FabricMinecraftLibraryParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, new File(String.format("%s/libraries/%s", area, lib.getPath())).getAbsolutePath(), "-1"));
        }
        return downloads;
    }
}
