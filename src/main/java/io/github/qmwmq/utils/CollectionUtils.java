package io.github.qmwmq.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CollectionUtils {

    /**
     * 通用分批处理工具
     *
     * @param collection 要分批的集合
     * @param batchSize  每批数量
     * @param consumer   每批执行的逻辑
     * @param <T>        任意类型
     */
    public <T> void batchProcess(Collection<T> collection, int batchSize, Consumer<Collection<T>> consumer) {
        if (collection == null)
            return;

        List<T> list = collection.stream()
                .filter(Objects::nonNull)
                .toList();

        if (list.isEmpty())
            return;

        int total = list.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<T> batch = list.subList(i, end);
            consumer.accept(batch);
        }
    }

}
