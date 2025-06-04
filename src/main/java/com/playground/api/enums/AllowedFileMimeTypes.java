package com.playground.api.enums;

import lombok.Getter;

@Getter
public enum AllowedFileMimeTypes {
    JPG("image/jpeg"),
    PNG("image/png"),
    WEBP("image/webp");

    private final String mimeType;

    AllowedFileMimeTypes(String mimeType) {
        this.mimeType = mimeType;
    }

    public static boolean isValidMimeType(String mimeType) {
        for (AllowedFileMimeTypes type : values()) {
            if (type.mimeType.equals(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
