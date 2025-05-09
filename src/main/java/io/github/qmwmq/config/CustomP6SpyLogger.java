package io.github.qmwmq.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomP6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(
            int connectionId,
            String currentTime, // 时间戳
            long elapsed, // 耗时
            String category,
            String preparedSql, // 带?的sql
            String effectiveSql, // 实际执行的sql
            String url // 数据库连接url
    ) {
        if (effectiveSql.isEmpty())
            return "";
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        return System.lineSeparator() +
                "[" + time + " execution: " + elapsed + "ms] ==> " +
                effectiveSql.replaceAll(" +", " ");
    }

}
