package com.twitter.microservice.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {
    private CollectionUtil(){
    }
    private static class CollectionUtilContainer{
        static final CollectionUtil SINGLETON_INSTANCE = new CollectionUtil();
    }
    public static CollectionUtil getInstance(){
        return CollectionUtilContainer.SINGLETON_INSTANCE;
    }

    public <T> List<T> convertIterableToList(Iterable<T> iter){
        List<T> list = new ArrayList<>();
        for (T t : iter) {
            list.add(t);
        }
        return list;
    }
}
