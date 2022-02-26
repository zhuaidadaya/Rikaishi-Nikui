package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
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

    public boolean hasFormat(String source) {
        try {
            format.get(language).get(source).toString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public Texts formatTexts(String source, Object... args) {
//        try {
//            JSONObject text = null;
//            try {
//                text = format.get(language).getJSONObject(source);
//            } catch (Exception ex) {
//
//            }
//
//            Texts formatReturn;
//            if(text == null)
//                formatReturn = new Texts(new Text(format.get(language).getString(source)));
//            else
//                formatReturn = new Texts(text);
//
//            Collection<Text> texts = new LinkedHashSet<>();
//            for(Object o : args) {
//                try {
//                    JSONObject json = new JSONObject();
//                    texts.add(new Text(json));
//                } catch (Exception e) {
//                    try {
//                        formatReturn.format(Matcher.quoteReplacement(o.toString()));
//                    } catch (Exception ex) {
//                        return formatReturn;
//                    }
//                }
//            }
//
//            return formatReturn;
//        } catch (Exception e) {
//            return new Texts(new Text(source));
//        }
//    }

    public SingleText format(String source, Object... args) {
        try {
            JSONObject text = null;
            try {
                text = format.get(language).getJSONObject(source);
            } catch (Exception ex) {

            }

            SingleText formatReturn;
            if(text == null)
                formatReturn = new SingleText(format.get(language).getString(source));
            else
                formatReturn = new SingleText(text);

            for(Object o : args) {
                try {
                    formatReturn.format(Matcher.quoteReplacement(o.toString()));
                } catch (Exception ex) {
                    return formatReturn;
                }
            }
            return formatReturn;
        } catch (Exception e) {
            return new SingleText(source);
        }
    }
}