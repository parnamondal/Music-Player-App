package android.support.p000v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p000v4.util.Preconditions;
import android.util.TypedValue;

/* renamed from: android.support.v4.content.res.ResourcesCompat */
public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    /* renamed from: android.support.v4.content.res.ResourcesCompat$FontCallback */
    public static abstract class FontCallback {
        public abstract void onFontRetrievalFailed(int i);

        public abstract void onFontRetrieved(@NonNull Typeface typeface);

        @RestrictTo({Scope.LIBRARY_GROUP})
        public final void callbackSuccessAsync(final Typeface typeface, @Nullable Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() {
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public final void callbackFailAsync(final int reason, @Nullable Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() {
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(reason);
                }
            });
        }
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Resources res, @DrawableRes int id, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return res.getDrawable(id, theme);
        }
        return res.getDrawable(id);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources res, @DrawableRes int id, int density, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return res.getDrawableForDensity(id, density, theme);
        }
        if (VERSION.SDK_INT >= 15) {
            return res.getDrawableForDensity(id, density);
        }
        return res.getDrawable(id);
    }

    @ColorInt
    public static int getColor(@NonNull Resources res, @ColorRes int id, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return res.getColor(id, theme);
        }
        return res.getColor(id);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources res, @ColorRes int id, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return res.getColorStateList(id, theme);
        }
        return res.getColorStateList(id);
    }

    @Nullable
    public static Typeface getFont(@NonNull Context context, @FontRes int id) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, new TypedValue(), 0, null, null, false);
    }

    public static void getFont(@NonNull Context context, @FontRes int id, @NonNull FontCallback fontCallback, @Nullable Handler handler) throws NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        loadFont(context, id, new TypedValue(), 0, fontCallback, handler, false);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static Typeface getFont(@NonNull Context context, @FontRes int id, TypedValue value, int style, @Nullable FontCallback fontCallback) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, value, style, fontCallback, null, true);
    }

    private static Typeface loadFont(@NonNull Context context, int id, TypedValue value, int style, @Nullable FontCallback fontCallback, @Nullable Handler handler, boolean isRequestFromLayoutInflator) {
        Resources resources = context.getResources();
        resources.getValue(id, value, true);
        Typeface typeface = loadFont(context, resources, value, id, style, fontCallback, handler, isRequestFromLayoutInflator);
        if (typeface != null || fontCallback != null) {
            return typeface;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Font resource ID #0x");
        sb.append(Integer.toHexString(id));
        sb.append(" could not be retrieved.");
        throw new NotFoundException(sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x00f7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Typeface loadFont(@android.support.annotation.NonNull android.content.Context r19, android.content.res.Resources r20, android.util.TypedValue r21, int r22, int r23, @android.support.annotation.Nullable android.support.p000v4.content.res.ResourcesCompat.FontCallback r24, @android.support.annotation.Nullable android.os.Handler r25, boolean r26) {
        /*
            r9 = r20
            r10 = r21
            r11 = r22
            r12 = r23
            r13 = r24
            r14 = r25
            java.lang.CharSequence r0 = r10.string
            if (r0 == 0) goto L_0x00fd
            java.lang.CharSequence r0 = r10.string
            java.lang.String r15 = r0.toString()
            java.lang.String r0 = "res/"
            boolean r0 = r15.startsWith(r0)
            r16 = 0
            r8 = -3
            if (r0 != 0) goto L_0x0029
            if (r13 == 0) goto L_0x0027
            r13.callbackFailAsync(r8, r14)
            goto L_0x0028
        L_0x0027:
        L_0x0028:
            return r16
        L_0x0029:
            android.graphics.Typeface r7 = android.support.p000v4.graphics.TypefaceCompat.findFromCache(r9, r11, r12)
            if (r7 == 0) goto L_0x0037
            if (r13 == 0) goto L_0x0035
            r13.callbackSuccessAsync(r7, r14)
            goto L_0x0036
        L_0x0035:
        L_0x0036:
            return r7
        L_0x0037:
            java.lang.String r0 = r15.toLowerCase()     // Catch:{ XmlPullParserException -> 0x00d8, IOException -> 0x00bb }
            java.lang.String r1 = ".xml"
            boolean r0 = r0.endsWith(r1)     // Catch:{ XmlPullParserException -> 0x00d8, IOException -> 0x00bb }
            if (r0 == 0) goto L_0x0097
            android.content.res.XmlResourceParser r0 = r9.getXml(r11)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0089 }
            android.support.v4.content.res.FontResourcesParserCompat$FamilyResourceEntry r1 = android.support.p000v4.content.res.FontResourcesParserCompat.parse(r0, r9)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0089 }
            r17 = r1
            if (r17 != 0) goto L_0x0069
            java.lang.String r1 = "ResourcesCompat"
            java.lang.String r2 = "Failed to find font-family tag"
            android.util.Log.e(r1, r2)     // Catch:{ XmlPullParserException -> 0x0064, IOException -> 0x005f }
            if (r13 == 0) goto L_0x005d
            r13.callbackFailAsync(r8, r14)     // Catch:{ XmlPullParserException -> 0x0064, IOException -> 0x005f }
            goto L_0x005e
        L_0x005d:
        L_0x005e:
            return r16
        L_0x005f:
            r0 = move-exception
            r10 = -3
            r1 = r19
            goto L_0x00ae
        L_0x0064:
            r0 = move-exception
            r10 = -3
            r1 = r19
            goto L_0x00b2
        L_0x0069:
            r1 = r19
            r2 = r17
            r3 = r20
            r4 = r22
            r5 = r23
            r6 = r24
            r18 = r7
            r7 = r25
            r10 = -3
            r8 = r26
            android.graphics.Typeface r1 = android.support.p000v4.graphics.TypefaceCompat.createFromResourcesFamilyXml(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ XmlPullParserException -> 0x0085, IOException -> 0x0081 }
            return r1
        L_0x0081:
            r0 = move-exception
            r1 = r19
            goto L_0x00c1
        L_0x0085:
            r0 = move-exception
            r1 = r19
            goto L_0x00de
        L_0x0089:
            r0 = move-exception
            r18 = r7
            r10 = -3
            r1 = r19
            goto L_0x00c1
        L_0x0090:
            r0 = move-exception
            r18 = r7
            r10 = -3
            r1 = r19
            goto L_0x00de
        L_0x0097:
            r18 = r7
            r10 = -3
            r1 = r19
            android.graphics.Typeface r0 = android.support.p000v4.graphics.TypefaceCompat.createFromResourcesFontFile(r1, r9, r11, r15, r12)     // Catch:{ XmlPullParserException -> 0x00b9, IOException -> 0x00b7 }
            r7 = r0
            if (r13 == 0) goto L_0x00b5
            if (r7 == 0) goto L_0x00a9
            r13.callbackSuccessAsync(r7, r14)     // Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x00ad }
            goto L_0x00b6
        L_0x00a9:
            r13.callbackFailAsync(r10, r14)     // Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x00ad }
            goto L_0x00b6
        L_0x00ad:
            r0 = move-exception
        L_0x00ae:
            r18 = r7
            goto L_0x00c1
        L_0x00b1:
            r0 = move-exception
        L_0x00b2:
            r18 = r7
            goto L_0x00de
        L_0x00b5:
        L_0x00b6:
            return r7
        L_0x00b7:
            r0 = move-exception
            goto L_0x00c1
        L_0x00b9:
            r0 = move-exception
            goto L_0x00de
        L_0x00bb:
            r0 = move-exception
            r10 = -3
            r1 = r19
            r18 = r7
        L_0x00c1:
            java.lang.String r2 = "ResourcesCompat"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Failed to read xml resource "
            r3.append(r4)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3, r0)
            goto L_0x00f5
        L_0x00d8:
            r0 = move-exception
            r10 = -3
            r1 = r19
            r18 = r7
        L_0x00de:
            java.lang.String r2 = "ResourcesCompat"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Failed to parse xml resource "
            r3.append(r4)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3, r0)
        L_0x00f5:
            if (r13 == 0) goto L_0x00fb
            r13.callbackFailAsync(r10, r14)
            goto L_0x00fc
        L_0x00fb:
        L_0x00fc:
            return r16
        L_0x00fd:
            r1 = r19
            android.content.res.Resources$NotFoundException r0 = new android.content.res.Resources$NotFoundException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Resource \""
            r2.append(r3)
            java.lang.String r3 = r9.getResourceName(r11)
            r2.append(r3)
            java.lang.String r3 = "\" ("
            r2.append(r3)
            java.lang.String r3 = java.lang.Integer.toHexString(r22)
            r2.append(r3)
            java.lang.String r3 = ") is not a Font: "
            r2.append(r3)
            r3 = r21
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.content.res.ResourcesCompat.loadFont(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, android.support.v4.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean):android.graphics.Typeface");
    }

    private ResourcesCompat() {
    }
}
