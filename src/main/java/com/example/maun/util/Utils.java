package com.example.maun.util;

import java.util.UUID;

public class Utils {
    public static UUID generateUUID(UUID id) {
        return id==null? UUID.randomUUID():id;
    }
}
