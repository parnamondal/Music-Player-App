package android.support.p000v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.compat.C0021R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.content.res.ColorStateListInflaterCompat */
public final class ColorStateListInflaterCompat {
    private static final int DEFAULT_COLOR = -65536;

    private ColorStateListInflaterCompat() {
    }

    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources r, @NonNull XmlPullParser parser, @Nullable Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            int next = parser.next();
            type = next;
            if (next == 2) {
                break;
            }
        } while (type != 1);
        if (type == 2) {
            return createFromXmlInner(r, parser, attrs, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            return inflate(r, parser, attrs, theme);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parser.getPositionDescription());
        sb.append(": invalid color state list tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }

    private static ColorStateList inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        AttributeSet attributeSet = attrs;
        int i = 1;
        int innerDepth = parser.getDepth() + 1;
        int defaultColor = -65536;
        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == i) {
                Resources resources = r;
                Theme theme2 = theme;
                int i2 = innerDepth;
                int i3 = defaultColor;
                break;
            }
            int depth = parser.getDepth();
            int depth2 = depth;
            if (depth < innerDepth && type == 3) {
                Resources resources2 = r;
                Theme theme3 = theme;
                int i4 = innerDepth;
                int i5 = defaultColor;
                break;
            } else if (type != 2 || depth2 > innerDepth || !parser.getName().equals("item")) {
                Resources resources3 = r;
                Theme theme4 = theme;
                innerDepth = innerDepth;
                defaultColor = defaultColor;
                i = 1;
            } else {
                TypedArray a = obtainAttributes(r, theme, attributeSet, C0021R.styleable.ColorStateListItem);
                int baseColor = a.getColor(C0021R.styleable.ColorStateListItem_android_color, -65281);
                float alphaMod = 1.0f;
                if (a.hasValue(C0021R.styleable.ColorStateListItem_android_alpha)) {
                    alphaMod = a.getFloat(C0021R.styleable.ColorStateListItem_android_alpha, 1.0f);
                } else if (a.hasValue(C0021R.styleable.ColorStateListItem_alpha)) {
                    alphaMod = a.getFloat(C0021R.styleable.ColorStateListItem_alpha, 1.0f);
                }
                a.recycle();
                int numAttrs = attrs.getAttributeCount();
                int[] stateSpec = new int[numAttrs];
                int innerDepth2 = innerDepth;
                int j = 0;
                int i6 = 0;
                while (i6 < numAttrs) {
                    int numAttrs2 = numAttrs;
                    int stateResId = attributeSet.getAttributeNameResource(i6);
                    int defaultColor2 = defaultColor;
                    if (!(stateResId == 16843173 || stateResId == 16843551 || stateResId == C0021R.attr.alpha)) {
                        int j2 = j + 1;
                        stateSpec[j] = attributeSet.getAttributeBooleanValue(i6, false) ? stateResId : -stateResId;
                        j = j2;
                    }
                    i6++;
                    numAttrs = numAttrs2;
                    defaultColor = defaultColor2;
                }
                int defaultColor3 = defaultColor;
                int[] stateSpec2 = StateSet.trimStateSet(stateSpec, j);
                int color = modulateColorAlpha(baseColor, alphaMod);
                if (listSize == 0 || stateSpec2.length == 0) {
                    defaultColor3 = color;
                }
                colorList = GrowingArrayUtils.append(colorList, listSize, color);
                stateSpecList = (int[][]) GrowingArrayUtils.append((T[]) stateSpecList, listSize, stateSpec2);
                listSize++;
                innerDepth = innerDepth2;
                defaultColor = defaultColor3;
                i = 1;
            }
        }
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        return new ColorStateList(stateSpecs, colors);
    }

    private static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int color, @FloatRange(from = 0.0d, mo110to = 1.0d) float alphaMod) {
        return (16777215 & color) | (Math.round(((float) Color.alpha(color)) * alphaMod) << 24);
    }
}
