package com.jt.util;

import java.util.UUID;

public class UUIDUtil {

    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
        System.out.println("生成UUID:" + getUuid());
    }
}
