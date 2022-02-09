package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.utils.reader.FileReads;
import com.github.zhuaidadaya.utils.resource.Resources;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.language;

public class TextFormat {
    private final Map<Language, JSONObject> format = new HashMap<>();

    public TextFormat(LanguageResource languageResource) {
        for(Language lang : languageResource.getNames()) {
            String resource = languageResource.get(lang);

            try {
                JSONObject json = new JSONObject(FileReads.read(new BufferedReader(new InputStreamReader(Resources.getResource(resource, getClass())))));
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
                    formatReturn.format(o.toString());
                } catch (Exception ex) {
                    return formatReturn;
                }
            }
            return formatReturn;
        } catch (Exception e) {
            return new Text(source);
        }
    }
}