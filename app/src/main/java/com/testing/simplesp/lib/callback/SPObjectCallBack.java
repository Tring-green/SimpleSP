package com.testing.simplesp.lib.callback;

import java.lang.reflect.ParameterizedType;

/**
 * Created by admin on 2016/5/27.
 */
public abstract class SPObjectCallBack<T> {

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    protected SPObjectCallBack() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    public Class<T> getClazz() {
        return clazz;

    }

    abstract public void onSuccess(T data);

    abstract public void onError(int errorCode, String errorMessage);
}
