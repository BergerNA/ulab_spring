package com.edu.ulab.app.utils;

import com.edu.ulab.app.exception.NullPointerException;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static void requireNonNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }
}
