package io.github.qmwmq.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * sql日志分析工具类
 */
public class CustomP6SpyLogger implements MessageFormattingStrategy {

    /**
     * 默认构造器
     */
    public CustomP6SpyLogger() {
    }

    /**
     * @param connectionId 连接id
     * @param currentTime  当前时间戳
     * @param elapsed      sql执行时间
     * @param category     类型
     * @param preparedSql  带?的sql
     * @param effectiveSql 带参数的完整sql
     * @param url          数据库连接url
     * @return 拼接的自定义sql
     */
    @Override
    public String formatMessage(
            int connectionId,
            String currentTime,
            long elapsed,
            String category,
            String preparedSql,
            String effectiveSql,
            String url
    ) {
        if (effectiveSql.isEmpty())
            return "";
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        return System.lineSeparator() +
                "[" + time + " execution: " + elapsed + "ms] ==> " +
                effectiveSql.replaceAll(" +", " ");
    }

}
