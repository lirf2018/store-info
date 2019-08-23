package com.yufan.common.utils;

import java.util.*;

/**
 * @功能名称 排序map
 * @作者 lirongfan
 * @时间 2016年5月31日 上午10:22:52
 */
public class MyMap {

    private HashMap map = new HashMap();
    private Set keySet = map.keySet();

    public Object get(String key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public void put(String key, Object value) {
        map.put(key, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort() {
        List list = new ArrayList(map.keySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
            }
        });

        this.keySet = new TreeSet(list);
    }

    public Set keySet() {
        return this.keySet;
    }

}
