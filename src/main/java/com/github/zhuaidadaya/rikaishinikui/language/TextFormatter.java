package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.regex.Matcher;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.language;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class TextFormatter {
    private final LanguageResource format;

    public TextFormatter(LanguageResource languageResource) {
        this.format = languageResource;
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

    public Text format(String source, Object... args) {
        try {
            JSONObject json = format.get(language).getJSONObject(source);
            return formatMultipleText(json, args);
        } catch (Exception e) {
            return formatSingleText(source, args);
        }
    }

    public MultipleText formatMultipleText(JSONObject json, Object... args) {
        MultipleText texts = new MultipleText(json);
        texts.format(args);
        return texts;
    }

    public SingleText formatSingleText(String source, JSONObject json, Object... args) {
        SingleText formatReturn;
        if (json == null) {
            formatReturn = new SingleText(format.get(language).get(source));
        } else {
            formatReturn = new SingleText(json);
        }

        for (Object o : args) {
            try {
                formatReturn.format(Matcher.quoteReplacement(o.toString()));
            } catch (Exception ex) {
                return formatReturn;
            }
        }
        return formatReturn;
    }

    public SingleText formatSingleText(String source, Object... args) {
        try {
            JSONObject text = null;
            try {
                text = format.get(language).getJSONObject(source);
            } catch (Exception ex) {

            }

            return formatSingleText(source, text, args);
        } catch (Exception e) {
            return new SingleText(source);
        }
    }

    public ObjectArrayList<SingleText> formatSingTextsFromFile(File file) {
        ObjectArrayList<SingleText> texts = new ObjectArrayList<>();
        try {
            StringBuilder builder = FileUtil.readAsStringBuilder(new BufferedReader(new FileReader(file)));
            BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
            String cache;
            while ((cache = reader.readLine()) != null) {
                texts.add(new SingleText(new JSONObject(cache)));
            }
        } catch (Exception e) {

        }

        return texts;
    }

    public ObjectArrayList<MultipleText> formatMultipleTextsFromFile(File file) {
        ObjectArrayList<MultipleText> texts = new ObjectArrayList<>();
        try {
            StringBuilder builder = FileUtil.readAsStringBuilder(new BufferedReader(new FileReader(file)));
            BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
            String cache;
            while ((cache = reader.readLine()) != null) {
                texts.add(new MultipleText(new JSONObject(cache)));
            }
        } catch (Exception e) {

        }

        return texts;
    }

        public SingleText formatTrace(Throwable t) {
        StringBuilder builder = new StringBuilder();
        builder.append(t.toString()).append("\n");
        for (StackTraceElement s : t.getStackTrace()) {
            builder.append(textFormatter.format("happened.error.at", s.toString() + "\n").getText());
        }
        builder.delete(builder.length() - 1, builder.length());
        return new SingleText(builder.toString(), new RikaishiNikuiColor(246, 55, 65));
    }
}