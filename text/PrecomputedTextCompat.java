package android.support.p000v4.text;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.UiThread;
import android.support.p000v4.p002os.TraceCompat;
import android.support.p000v4.util.ObjectsCompat;
import android.support.p000v4.util.Preconditions;
import android.text.Layout.Alignment;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/* renamed from: android.support.v4.text.PrecomputedTextCompat */
public class PrecomputedTextCompat implements Spannable {
    private static final char LINE_FEED = '\n';
    @GuardedBy("sLock")
    @NonNull
    private static Executor sExecutor = null;
    private static final Object sLock = new Object();
    @NonNull
    private final int[] mParagraphEnds;
    @NonNull
    private final Params mParams;
    @NonNull
    private final Spannable mText;
    @Nullable
    private final PrecomputedText mWrapped;

    /* renamed from: android.support.v4.text.PrecomputedTextCompat$Params */
    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        @NonNull
        private final TextPaint mPaint;
        @Nullable
        private final TextDirectionHeuristic mTextDir;
        final android.text.PrecomputedText.Params mWrapped;

        /* renamed from: android.support.v4.text.PrecomputedTextCompat$Params$Builder */
        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            @NonNull
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(@NonNull TextPaint paint) {
                this.mPaint = paint;
                if (VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                } else {
                    this.mTextDir = null;
                }
            }

            @RequiresApi(23)
            public Builder setBreakStrategy(int strategy) {
                this.mBreakStrategy = strategy;
                return this;
            }

            @RequiresApi(23)
            public Builder setHyphenationFrequency(int frequency) {
                this.mHyphenationFrequency = frequency;
                return this;
            }

            @RequiresApi(18)
            public Builder setTextDirection(@NonNull TextDirectionHeuristic textDir) {
                this.mTextDir = textDir;
                return this;
            }

            @NonNull
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
        }

        Params(@NonNull TextPaint paint, @NonNull TextDirectionHeuristic textDir, int strategy, int frequency) {
            if (VERSION.SDK_INT >= 28) {
                this.mWrapped = new android.text.PrecomputedText.Params.Builder(paint).setBreakStrategy(strategy).setHyphenationFrequency(frequency).setTextDirection(textDir).build();
            } else {
                this.mWrapped = null;
            }
            this.mPaint = paint;
            this.mTextDir = textDir;
            this.mBreakStrategy = strategy;
            this.mHyphenationFrequency = frequency;
        }

        @RequiresApi(28)
        public Params(@NonNull android.text.PrecomputedText.Params wrapped) {
            this.mPaint = wrapped.getTextPaint();
            this.mTextDir = wrapped.getTextDirection();
            this.mBreakStrategy = wrapped.getBreakStrategy();
            this.mHyphenationFrequency = wrapped.getHyphenationFrequency();
            this.mWrapped = wrapped;
        }

        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        @Nullable
        @RequiresApi(18)
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        @RequiresApi(23)
        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        @RequiresApi(23)
        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public boolean equals(@Nullable Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof Params)) {
                return false;
            }
            Params other = (Params) o;
            android.text.PrecomputedText.Params params = this.mWrapped;
            if (params != null) {
                return params.equals(other.mWrapped);
            }
            if (VERSION.SDK_INT >= 23 && (this.mBreakStrategy != other.getBreakStrategy() || this.mHyphenationFrequency != other.getHyphenationFrequency())) {
                return false;
            }
            if ((VERSION.SDK_INT >= 18 && this.mTextDir != other.getTextDirection()) || this.mPaint.getTextSize() != other.getTextPaint().getTextSize() || this.mPaint.getTextScaleX() != other.getTextPaint().getTextScaleX() || this.mPaint.getTextSkewX() != other.getTextPaint().getTextSkewX()) {
                return false;
            }
            if ((VERSION.SDK_INT >= 21 && (this.mPaint.getLetterSpacing() != other.getTextPaint().getLetterSpacing() || !TextUtils.equals(this.mPaint.getFontFeatureSettings(), other.getTextPaint().getFontFeatureSettings()))) || this.mPaint.getFlags() != other.getTextPaint().getFlags()) {
                return false;
            }
            if (VERSION.SDK_INT >= 24) {
                if (!this.mPaint.getTextLocales().equals(other.getTextPaint().getTextLocales())) {
                    return false;
                }
            } else if (VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(other.getTextPaint().getTextLocale())) {
                return false;
            }
            if (this.mPaint.getTypeface() == null) {
                if (other.getTextPaint().getTypeface() != null) {
                    return false;
                }
            } else if (!this.mPaint.getTypeface().equals(other.getTextPaint().getTypeface())) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            if (VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("textSize=");
            sb2.append(this.mPaint.getTextSize());
            sb.append(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(", textScaleX=");
            sb3.append(this.mPaint.getTextScaleX());
            sb.append(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(", textSkewX=");
            sb4.append(this.mPaint.getTextSkewX());
            sb.append(sb4.toString());
            if (VERSION.SDK_INT >= 21) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(", letterSpacing=");
                sb5.append(this.mPaint.getLetterSpacing());
                sb.append(sb5.toString());
                StringBuilder sb6 = new StringBuilder();
                sb6.append(", elegantTextHeight=");
                sb6.append(this.mPaint.isElegantTextHeight());
                sb.append(sb6.toString());
            }
            if (VERSION.SDK_INT >= 24) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(", textLocale=");
                sb7.append(this.mPaint.getTextLocales());
                sb.append(sb7.toString());
            } else if (VERSION.SDK_INT >= 17) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(", textLocale=");
                sb8.append(this.mPaint.getTextLocale());
                sb.append(sb8.toString());
            }
            StringBuilder sb9 = new StringBuilder();
            sb9.append(", typeface=");
            sb9.append(this.mPaint.getTypeface());
            sb.append(sb9.toString());
            if (VERSION.SDK_INT >= 26) {
                StringBuilder sb10 = new StringBuilder();
                sb10.append(", variationSettings=");
                sb10.append(this.mPaint.getFontVariationSettings());
                sb.append(sb10.toString());
            }
            StringBuilder sb11 = new StringBuilder();
            sb11.append(", textDir=");
            sb11.append(this.mTextDir);
            sb.append(sb11.toString());
            StringBuilder sb12 = new StringBuilder();
            sb12.append(", breakStrategy=");
            sb12.append(this.mBreakStrategy);
            sb.append(sb12.toString());
            StringBuilder sb13 = new StringBuilder();
            sb13.append(", hyphenationFrequency=");
            sb13.append(this.mHyphenationFrequency);
            sb.append(sb13.toString());
            sb.append("}");
            return sb.toString();
        }
    }

    /* renamed from: android.support.v4.text.PrecomputedTextCompat$PrecomputedTextFutureTask */
    private static class PrecomputedTextFutureTask extends FutureTask<PrecomputedTextCompat> {

        /* renamed from: android.support.v4.text.PrecomputedTextCompat$PrecomputedTextFutureTask$PrecomputedTextCallback */
        private static class PrecomputedTextCallback implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(@NonNull Params params, @NonNull CharSequence cs) {
                this.mParams = params;
                this.mText = cs;
            }

            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }

        PrecomputedTextFutureTask(@NonNull Params params, @NonNull CharSequence text) {
            super(new PrecomputedTextCallback(params, text));
        }
    }

    public static PrecomputedTextCompat create(@NonNull CharSequence text, @NonNull Params params) {
        int paraEnd;
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(params);
        try {
            TraceCompat.beginSection("PrecomputedText");
            if (VERSION.SDK_INT >= 28 && params.mWrapped != null) {
                return new PrecomputedTextCompat(PrecomputedText.create(text, params.mWrapped), params);
            }
            ArrayList<Integer> ends = new ArrayList<>();
            int end = text.length();
            int paraStart = 0;
            while (paraStart < end) {
                int paraEnd2 = TextUtils.indexOf(text, LINE_FEED, paraStart, end);
                if (paraEnd2 < 0) {
                    paraEnd = end;
                } else {
                    paraEnd = paraEnd2 + 1;
                }
                ends.add(Integer.valueOf(paraEnd));
                paraStart = paraEnd;
            }
            int[] result = new int[ends.size()];
            for (int i = 0; i < ends.size(); i++) {
                result[i] = ((Integer) ends.get(i)).intValue();
            }
            if (VERSION.SDK_INT >= 23) {
                Builder.obtain(text, 0, text.length(), params.getTextPaint(), ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
            } else if (VERSION.SDK_INT >= 21) {
                new StaticLayout(text, params.getTextPaint(), ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            PrecomputedTextCompat precomputedTextCompat = new PrecomputedTextCompat(text, params, result);
            TraceCompat.endSection();
            return precomputedTextCompat;
        } finally {
            TraceCompat.endSection();
        }
    }

    private PrecomputedTextCompat(@NonNull CharSequence text, @NonNull Params params, @NonNull int[] paraEnds) {
        this.mText = new SpannableString(text);
        this.mParams = params;
        this.mParagraphEnds = paraEnds;
        this.mWrapped = null;
    }

    @RequiresApi(28)
    private PrecomputedTextCompat(@NonNull PrecomputedText precomputed, @NonNull Params params) {
        this.mText = precomputed;
        this.mParams = params;
        this.mParagraphEnds = null;
        this.mWrapped = precomputed;
    }

    @Nullable
    @RequiresApi(28)
    @RestrictTo({Scope.LIBRARY_GROUP})
    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText) spannable;
        }
        return null;
    }

    @NonNull
    public Params getParams() {
        return this.mParams;
    }

    @IntRange(from = 0)
    public int getParagraphCount() {
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphCount();
        }
        return this.mParagraphEnds.length;
    }

    @IntRange(from = 0)
    public int getParagraphStart(@IntRange(from = 0) int paraIndex) {
        int i = 0;
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphStart(paraIndex);
        }
        if (paraIndex != 0) {
            i = this.mParagraphEnds[paraIndex - 1];
        }
        return i;
    }

    @IntRange(from = 0)
    public int getParagraphEnd(@IntRange(from = 0) int paraIndex) {
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphEnd(paraIndex);
        }
        return this.mParagraphEnds[paraIndex];
    }

    private int findParaIndex(@IntRange(from = 0) int pos) {
        int i = 0;
        while (true) {
            int[] iArr = this.mParagraphEnds;
            if (i >= iArr.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("pos must be less than ");
                int[] iArr2 = this.mParagraphEnds;
                sb.append(iArr2[iArr2.length - 1]);
                sb.append(", gave ");
                sb.append(pos);
                throw new IndexOutOfBoundsException(sb.toString());
            } else if (pos < iArr[i]) {
                return i;
            } else {
                i++;
            }
        }
    }

    @UiThread
    public static Future<PrecomputedTextCompat> getTextFuture(@NonNull CharSequence charSequence, @NonNull Params params, @Nullable Executor executor) {
        PrecomputedTextFutureTask task = new PrecomputedTextFutureTask(params, charSequence);
        if (executor == null) {
            synchronized (sLock) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                executor = sExecutor;
            }
        }
        executor.execute(task);
        return task;
    }

    public void setSpan(Object what, int start, int end, int flags) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        } else if (VERSION.SDK_INT >= 28) {
            this.mWrapped.setSpan(what, start, end, flags);
        } else {
            this.mText.setSpan(what, start, end, flags);
        }
    }

    public void removeSpan(Object what) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        } else if (VERSION.SDK_INT >= 28) {
            this.mWrapped.removeSpan(what);
        } else {
            this.mText.removeSpan(what);
        }
    }

    public <T> T[] getSpans(int start, int end, Class<T> type) {
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getSpans(start, end, type);
        }
        return this.mText.getSpans(start, end, type);
    }

    public int getSpanStart(Object tag) {
        return this.mText.getSpanStart(tag);
    }

    public int getSpanEnd(Object tag) {
        return this.mText.getSpanEnd(tag);
    }

    public int getSpanFlags(Object tag) {
        return this.mText.getSpanFlags(tag);
    }

    public int nextSpanTransition(int start, int limit, Class type) {
        return this.mText.nextSpanTransition(start, limit, type);
    }

    public int length() {
        return this.mText.length();
    }

    public char charAt(int index) {
        return this.mText.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return this.mText.subSequence(start, end);
    }

    public String toString() {
        return this.mText.toString();
    }
}
