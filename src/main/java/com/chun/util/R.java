/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.chun.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R<T> extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 返回值
     */
    private T data;

    /**
     * 存放字典
     */
    private Map<String, Object> dict;

    //默认返回是200
    public R() {
        put("code", 200);
        put("msg", "success");
        put("data", data);
    }

    public R(Integer code, String msg) {
        put("code", code);
        put("msg", msg);
    }


    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R data(T data) {
        this.put("data", data);
        return this;
    }


    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
