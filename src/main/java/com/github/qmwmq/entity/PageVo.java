package com.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PageVo {

    /**
     * 默认构造器
     */
    public PageVo() {
    }

    /**
     * 两个参数的构造器
     *
     * @param total   total
     * @param records records
     */
    public PageVo(long total, Collection<?> records) {
        this.total = total;
        this.records = records;
    }

    private long total;
    private Collection<?> records;
    private Map<String, Object> extra;

    /**
     * 添加额外信息
     *
     * @param key   key
     * @param value value
     */
    public PageVo putExtra(String key, Object value) {
        if (extra == null)
            extra = new HashMap<>();
        extra.put(key, value);
        return this;
    }

}
