package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.utils.file.FileUtil;
import com.github.zhuaidadaya.utils.resource.Resources;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.language;

public class TextFormat {
    private final Map<Language, JSONObject> format = new HashMap<>();

    public TextFormat(LanguageResource languageResource) {
        for(Language lang : languageResource.getNames()) {
            String resource = languageResource.get(lang);

            try {
                JSONObject json = new JSONObject(FileUtil.read(new BufferedReader(new InputStreamReader(Resources.getResource(resource, getClass()), StandardCharsets.UTF_8))));
                format.put(lang, json);
            } catch (Exception e) {

            }
        }
    }

    public String getText(String source, Object... args) {
        return format(source, args).getText();
    }

    public Text format(String source, Object... args) {
        try {
            JSONObject text = null;
            try {
                text = format.get(language).getJSONObject(source);
            } catch (Exception ex) {

            }

            Text formatReturn;
            if(text == null)
                formatReturn = new Text(format.get(language).getString(source));
            else
                formatReturn = new Text(text);

            for(Object o : args) {
                try {
                    formatReturn.format(Matcher.quoteReplacement(o.toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return formatReturn;
                }
            }
            return formatReturn;
        } catch (Exception e) {
            return new Text(source);
        }
    }
}