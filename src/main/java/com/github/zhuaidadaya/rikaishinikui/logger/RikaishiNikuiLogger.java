package com.github.zhuaidadaya.rikaishinikui.logger;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.utils.times.TimeType;
import com.github.zhuaidadaya.utils.times.Times;
import org.apache.logging.log4j.Logger;

import java.awt.*;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.launcher;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormat;

public class RikaishiNikuiLogger {
    private final Logger logger;
    private final String format;
    private final String timeFormat;

    public RikaishiNikuiLogger(Logger logger, String format, String timeFormat) {
        this.logger = logger;
        this.format = format;
        this.timeFormat = timeFormat;
    }

    public void info(Object message) {
        logger.info(message);
        launcher.appendLogFrameText(new Text(message.toString() + "\n"), false);
    }

    public void info(String message, Object... formatted) {
        logger.info(message, formatted);
        launcher.appendLogFrameText(format(new Text(String.format(message.replace("{}", "%s"), formatted)), "INFO", false), false);
    }

    public void error(Object message) {
        logger.error(message);
        launcher.appendLogFrameText(format(new Text(message.toString()).setColor(new Color(152, 12, 10)), "ERROR", true), false);
    }

    public void error(Object message, Throwable throwable) {
        logger.error(message, throwable);
        launcher.appendLogFrameText(format(new Text(message.toString()).setColor(new Color(152, 12, 10)), "ERROR", true), false);
        for(StackTraceElement s : throwable.getStackTrace())
            launcher.appendLogFrameText(format(textFormat.format("happened.error.at", s.toString()), "ERROR", true), false);
    }

    public Text format(Text message, String level, boolean error) {
        Text text = new Text(format.replace("%d", Times.getTime(TimeType.CUSTOM, timeFormat)).replace("%c", logger.getName()).replace("%l", level).replace("%m", message.getText()).replace("%n", "\n"));
        if(error)
            text.setColor(new Color(152, 12, 10));
        else
            text.setColor(message.getColor());
        return text;
    }
}
