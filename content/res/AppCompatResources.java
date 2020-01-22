package android.support.p003v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.content.res.ColorStateListInflaterCompat;
import android.support.p003v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;

/* renamed from: android.support.v7.content.res.AppCompatResources */
public final class AppCompatResources {
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    private static final Object sColorStateCacheLock = new Object();
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<>(0);

    /* renamed from: android.support.v7.content.res.AppCompatResources$ColorStateListCacheEntry */
    private static class ColorStateListCacheEntry {
        final Configuration configuration;
        final ColorStateList value;

        ColorStateListCacheEntry(@NonNull ColorStateList value2, @NonNull Configuration configuration2) {
            this.value = value2;
            this.configuration = configuration2;
        }
    }

    private AppCompatResources() {
    }

    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int resId) {
        if (VERSION.SDK_INT >= 23) {
            return context.getColorStateList(resId);
        }
        ColorStateList csl = getCachedColorStateList(context, resId);
        if (csl != null) {
            return csl;
        }
        ColorStateList csl2 = inflateColorStateList(context, resId);
        if (csl2 == null) {
            return ContextCompat.getColorStateList(context, resId);
        }
        addColorStateListToCache(context, resId, csl2);
        return csl2;
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return AppCompatDrawableManager.get().getDrawable(context, resId);
    }

    @Nullable
    private static ColorStateList inflateColorStateList(Context context, int resId) {
        if (isColorInt(context, resId)) {
            return null;
        }
        Resources r = context.getResources();
        try {
            return ColorStateListInflaterCompat.createFromXml(r, r.getXml(resId), context.getTheme());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to inflate ColorStateList, leaving it to the framework", e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0037, code lost:
        return null;
     */
    @android.support.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.res.ColorStateList getCachedColorStateList(@android.support.annotation.NonNull android.content.Context r5, @android.support.annotation.ColorRes int r6) {
        /*
            java.lang.Object r0 = sColorStateCacheLock
            monitor-enter(r0)
            java.util.WeakHashMap<android.content.Context, android.util.SparseArray<android.support.v7.content.res.AppCompatResources$ColorStateListCacheEntry>> r1 = sColorStateCaches     // Catch:{ all -> 0x0038 }
            java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0038 }
            android.util.SparseArray r1 = (android.util.SparseArray) r1     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x0034
            int r2 = r1.size()     // Catch:{ all -> 0x0038 }
            if (r2 <= 0) goto L_0x0034
            java.lang.Object r2 = r1.get(r6)     // Catch:{ all -> 0x0038 }
            android.support.v7.content.res.AppCompatResources$ColorStateListCacheEntry r2 = (android.support.p003v7.content.res.AppCompatResources.ColorStateListCacheEntry) r2     // Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x0033
            android.content.res.Configuration r3 = r2.configuration     // Catch:{ all -> 0x0038 }
            android.content.res.Resources r4 = r5.getResources()     // Catch:{ all -> 0x0038 }
            android.content.res.Configuration r4 = r4.getConfiguration()     // Catch:{ all -> 0x0038 }
            boolean r3 = r3.equals(r4)     // Catch:{ all -> 0x0038 }
            if (r3 == 0) goto L_0x002f
            android.content.res.ColorStateList r3 = r2.value     // Catch:{ all -> 0x0038 }
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            return r3
        L_0x002f:
            r1.remove(r6)     // Catch:{ all -> 0x0038 }
            goto L_0x0035
        L_0x0033:
            goto L_0x0035
        L_0x0034:
        L_0x0035:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            r0 = 0
            return r0
        L_0x0038:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.content.res.AppCompatResources.getCachedColorStateList(android.content.Context, int):android.content.res.ColorStateList");
    }

    private static void addColorStateListToCache(@NonNull Context context, @ColorRes int resId, @NonNull ColorStateList value) {
        synchronized (sColorStateCacheLock) {
            SparseArray sparseArray = (SparseArray) sColorStateCaches.get(context);
            if (sparseArray == null) {
                sparseArray = new SparseArray();
                sColorStateCaches.put(context, sparseArray);
            }
            sparseArray.append(resId, new ColorStateListCacheEntry(value, context.getResources().getConfiguration()));
        }
    }

    private static boolean isColorInt(@NonNull Context context, @ColorRes int resId) {
        Resources r = context.getResources();
        TypedValue value = getTypedValue();
        r.getValue(resId, value, true);
        if (value.type < 28 || value.type > 31) {
            return false;
        }
        return true;
    }

    @NonNull
    private static TypedValue getTypedValue() {
        TypedValue tv = (TypedValue) TL_TYPED_VALUE.get();
        if (tv != null) {
            return tv;
        }
        TypedValue tv2 = new TypedValue();
        TL_TYPED_VALUE.set(tv2);
        return tv2;
    }
}
