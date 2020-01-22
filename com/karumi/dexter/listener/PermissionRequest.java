package com.karumi.dexter.listener;

import android.support.annotation.NonNull;

public final class PermissionRequest {
    private final String name;

    public PermissionRequest(@NonNull String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Permission name: ");
        sb.append(this.name);
        return sb.toString();
    }
}
