package com.karumi.dexter;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import java.util.LinkedList;
import java.util.List;

public final class MultiplePermissionsReport {
    private final List<PermissionDeniedResponse> deniedPermissionResponses = new LinkedList();
    private final List<PermissionGrantedResponse> grantedPermissionResponses = new LinkedList();

    MultiplePermissionsReport() {
    }

    /* access modifiers changed from: 0000 */
    public boolean addDeniedPermissionResponse(PermissionDeniedResponse permissionDeniedResponse) {
        return this.deniedPermissionResponses.add(permissionDeniedResponse);
    }

    /* access modifiers changed from: 0000 */
    public boolean addGrantedPermissionResponse(PermissionGrantedResponse permissionGrantedResponse) {
        return this.grantedPermissionResponses.add(permissionGrantedResponse);
    }

    public boolean areAllPermissionsGranted() {
        return this.deniedPermissionResponses.isEmpty();
    }

    /* access modifiers changed from: 0000 */
    public void clear() {
        this.grantedPermissionResponses.clear();
        this.deniedPermissionResponses.clear();
    }

    public List<PermissionDeniedResponse> getDeniedPermissionResponses() {
        return this.deniedPermissionResponses;
    }

    public List<PermissionGrantedResponse> getGrantedPermissionResponses() {
        return this.grantedPermissionResponses;
    }

    public boolean isAnyPermissionPermanentlyDenied() {
        for (PermissionDeniedResponse isPermanentlyDenied : this.deniedPermissionResponses) {
            if (isPermanentlyDenied.isPermanentlyDenied()) {
                return true;
            }
        }
        return false;
    }
}
