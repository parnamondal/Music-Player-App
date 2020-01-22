package com.karumi.dexter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.p000v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import java.util.Arrays;
import java.util.LinkedList;

public final class DexterActivity extends Activity implements OnRequestPermissionsResultCallback {
    private boolean isTargetSdkUnderAndroidM() {
        boolean z = false;
        try {
            if (getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.targetSdkVersion < 23) {
                z = true;
            }
            return z;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Dexter.onActivityReady(this);
        getWindow().addFlags(16);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Dexter.onActivityDestroyed();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Dexter.onActivityReady(this);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        if (isTargetSdkUnderAndroidM()) {
            linkedList2.addAll(Arrays.asList(strArr));
        } else {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                String str = strArr[i2];
                switch (iArr[i2]) {
                    case -2:
                    case -1:
                        linkedList2.add(str);
                        break;
                    case 0:
                        linkedList.add(str);
                        break;
                }
            }
        }
        Dexter.onPermissionsRequested(linkedList, linkedList2);
    }
}
