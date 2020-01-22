package com.karumi.dexter.listener;

import android.support.annotation.NonNull;

public final class PermissionGrantedResponse {
    private final PermissionRequest requestedPermission;

    public PermissionGrantedResponse(@NonNull PermissionRequest permissionRequest) {
        this.requestedPermission = permissionRequest;
    }

    public static PermissionGrantedResponse from(@NonNull String str) {
        return new PermissionGrantedResponse(new PermissionRequest(str));
    }

    public String getPermissionName() {
        return this.requestedPermission.getName();
    }

    public PermissionRequest getRequestedPermission() {
        return this.requestedPermission;
    }
}
