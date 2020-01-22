package com.karumi.dexter.listener;

import android.support.annotation.NonNull;

public final class PermissionDeniedResponse {
    private final boolean permanentlyDenied;
    private final PermissionRequest requestedPermission;

    public PermissionDeniedResponse(@NonNull PermissionRequest permissionRequest, boolean z) {
        this.requestedPermission = permissionRequest;
        this.permanentlyDenied = z;
    }

    public static PermissionDeniedResponse from(@NonNull String str, boolean z) {
        return new PermissionDeniedResponse(new PermissionRequest(str), z);
    }

    public String getPermissionName() {
        return this.requestedPermission.getName();
    }

    public PermissionRequest getRequestedPermission() {
        return this.requestedPermission;
    }

    public boolean isPermanentlyDenied() {
        return this.permanentlyDenied;
    }
}
