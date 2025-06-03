package io.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页数据封装实体类（泛型版）
 *
 * @param <T> 数据类型
 */
@Data
@Accessors(chain = true)
public class PageVo<T> {

    private long total;
    private Collection<T> records;
    private Map<String, Object> extra;

    /**
     * 私有构造器
     */
    private PageVo() {
    }

    public static class Builder<T> {

        private final PageVo<T> instance = new PageVo<>();

        public Builder<T> total(long total) {
            instance.setTotal(total);
            return this;
        }

        public Builder<T> records(Collection<T> records) {
            instance.setRecords(records);
            return this;
        }

        public Builder<T> putExtra(String key, Object value) {
            if (instance.extra == null)
                instance.extra = new HashMap<>();
            instance.extra.put(key, value);
            return this;
        }

        public PageVo<T> build() {
            return instance;
        }
    }

    // 提供静态方法快速获取 Builder
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

}
