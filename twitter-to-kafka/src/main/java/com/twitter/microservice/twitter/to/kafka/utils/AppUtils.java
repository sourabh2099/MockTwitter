package com.twitter.microservice.twitter.to.kafka.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class AppUtils {
    public static long genRandomLong(){
        String uuid = UUID.randomUUID().toString();
        return ByteBuffer.wrap(uuid.getBytes()).getLong();
    }
}
