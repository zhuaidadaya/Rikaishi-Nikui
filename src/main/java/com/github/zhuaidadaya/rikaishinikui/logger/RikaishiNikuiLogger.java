package com.github.zhuaidadaya.rikaishinikui.logger;

import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiTaskManager;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.utils.times.TimeType;
import com.github.zhuaidadaya.utils.times.Times;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class RikaishiNikuiLogger {
    private final Logger logger;
    private final UUID taskId;
    private final RikaishiNikuiTaskManager manager;
    private final String name;
    private String format = "%t=s [%c/%level] %msg%n";

    public RikaishiNikuiLogger(UUID taskId, RikaishiNikuiTaskManager manager, Logger logger) {
        name = logger.getName();
        this.logger = logger;
        this.taskId = taskId;
        this.manager = manager;
    }

    public RikaishiNikuiLogger(UUID taskId, RikaishiNikuiTaskManager manager, String logFormat, Logger logger) {
        name = logger.getName();
        this.logger = logger;
        this.taskId = taskId;
        this.manager = manager;
        this.format = logFormat;
    }

    public void info(String log) {
        synchronized (this) {
            logger.info(log);
            manager.log(taskId, format(log, "INFO"));
        }
    }

    public void info(String log, Throwable throwable) {
        synchronized (this) {
            logger.info(log, throwable);
            manager.log(taskId, format(log, "INFO"), LogLevel.INFO);
            manager.log(taskId, format(textFormatter.formatTrace(throwable).getText(), "INFO"));
        }
    }

    public void error(String log) {
        synchronized (this) {
            logger.error(log);
            manager.log(taskId, format(log, "ERROR"));
        }
    }

    public void error(String log, Throwable throwable) {
        synchronized (this) {
            logger.error(log, throwable);
            manager.log(taskId, format(log, "ERROR"), LogLevel.ERROR);
            manager.log(taskId, format(textFormatter.formatTrace(throwable).getText(), "ERROR"));
        }
    }

    public void warn(String log) {
        synchronized (this) {
            logger.warn(log);
            manager.log(taskId, format(log, "WARN"));
        }
    }

    public void warn(String log, Throwable throwable) {
        synchronized (this) {
            logger.warn(log, throwable);
            manager.log(taskId, format(log, "WARN"), LogLevel.WARN);
            manager.log(taskId, format(textFormatter.formatTrace(throwable).getText(), "WARN"));
        }
    }

    public void debug(String log) {
        synchronized (this) {
            logger.debug(log);
            manager.log(taskId, format(log, "DEBUG"));
        }
    }

    public void debug(String log, Throwable throwable) {
        synchronized (this) {
            logger.debug(log, throwable);
            manager.log(taskId, format(log, "DEBUG"), LogLevel.DEBUG);
            manager.log(taskId, format(textFormatter.formatTrace(throwable).getText(), "DEBUG"));
        }
    }

    public String format(String log, String level) {
        String formatted = format;
        formatted = formatted.replace("%t=f", Times.getTime(TimeType.LONG_LOG));
        formatted = formatted.replace("%t=s", Times.getTime(TimeType.LOG));

        formatted = formatted.replace("%c", name);
        formatted = formatted.replace("%level", level);
        formatted = formatted.replace("%msg", log);
        formatted = formatted.replace("%n", "\n");

        return formatted;
    }
}
