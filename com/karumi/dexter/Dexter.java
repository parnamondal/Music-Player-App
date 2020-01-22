package com.karumi.dexter;

import android.app.Activity;
import android.content.Context;
import com.karumi.dexter.DexterBuilder.MultiPermissionListener;
import com.karumi.dexter.DexterBuilder.Permission;
import com.karumi.dexter.DexterBuilder.SinglePermissionListener;
import com.karumi.dexter.listener.EmptyPermissionRequestErrorListener;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Dexter implements DexterBuilder, MultiPermissionListener, Permission, SinglePermissionListener {
    private static DexterInstance instance;
    private PermissionRequestErrorListener errorListener = new EmptyPermissionRequestErrorListener();
    private MultiplePermissionsListener listener = new BaseMultiplePermissionsListener();
    private Collection<String> permissions;
    private boolean shouldExecuteOnSameThread = false;

    private Dexter(Activity activity) {
        initialize(activity);
    }

    private Thread getThread() {
        return this.shouldExecuteOnSameThread ? ThreadFactory.makeSameThread() : ThreadFactory.makeMainThread();
    }

    private static void initialize(Context context) {
        DexterInstance dexterInstance = instance;
        if (dexterInstance == null) {
            instance = new DexterInstance(context, new AndroidPermissionService(), new IntentProvider());
        } else {
            dexterInstance.setContext(context);
        }
    }

    static void onActivityDestroyed() {
        DexterInstance dexterInstance = instance;
        if (dexterInstance != null) {
            dexterInstance.onActivityDestroyed();
        }
    }

    static void onActivityReady(Activity activity) {
        DexterInstance dexterInstance = instance;
        if (dexterInstance != null) {
            dexterInstance.onActivityReady(activity);
        }
    }

    static void onPermissionsRequested(Collection<String> collection, Collection<String> collection2) {
        DexterInstance dexterInstance = instance;
        if (dexterInstance != null) {
            dexterInstance.onPermissionRequestGranted(collection);
            instance.onPermissionRequestDenied(collection2);
        }
    }

    public static Permission withActivity(Activity activity) {
        return new Dexter(activity);
    }

    public void check() {
        try {
            instance.checkPermissions(this.listener, this.permissions, getThread());
        } catch (DexterException e) {
            this.errorListener.onError(e.error);
        }
    }

    public DexterBuilder onSameThread() {
        this.shouldExecuteOnSameThread = true;
        return this;
    }

    public DexterBuilder withErrorListener(PermissionRequestErrorListener permissionRequestErrorListener) {
        this.errorListener = permissionRequestErrorListener;
        return this;
    }

    public DexterBuilder withListener(MultiplePermissionsListener multiplePermissionsListener) {
        this.listener = multiplePermissionsListener;
        return this;
    }

    public DexterBuilder withListener(PermissionListener permissionListener) {
        this.listener = new MultiplePermissionsListenerToPermissionListenerAdapter(permissionListener);
        return this;
    }

    public SinglePermissionListener withPermission(String str) {
        this.permissions = Collections.singletonList(str);
        return this;
    }

    public MultiPermissionListener withPermissions(Collection<String> collection) {
        this.permissions = new ArrayList(collection);
        return this;
    }

    public MultiPermissionListener withPermissions(String... strArr) {
        this.permissions = Arrays.asList(strArr);
        return this;
    }
}
