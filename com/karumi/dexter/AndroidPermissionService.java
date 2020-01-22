package com.karumi.dexter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.PermissionChecker;

class AndroidPermissionService {
    AndroidPermissionService() {
    }

    /* access modifiers changed from: 0000 */
    public int checkSelfPermission(@NonNull Context context, @NonNull String str) {
        return PermissionChecker.checkSelfPermission(context, str);
    }

    /* access modifiers changed from: 0000 */
    public void requestPermissions(@Nullable Activity activity, @NonNull String[] strArr, int i) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, strArr, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldShowRequestPermissionRationale(@Nullable Activity activity, @NonNull String str) {
        if (activity == null) {
            return false;
        }
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, str);
    }
}
