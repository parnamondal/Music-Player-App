package android.support.p000v4.util;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.io.PrintWriter;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.util.TimeUtils */
public final class TimeUtils {
    @RestrictTo({Scope.LIBRARY_GROUP})
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr = new char[24];
    private static final Object sFormatSync = new Object();

    private static int accumField(int amt, int suffix, boolean always, int zeropad) {
        if (amt > 99 || (always && zeropad >= 3)) {
            return suffix + 3;
        }
        if (amt > 9 || (always && zeropad >= 2)) {
            return suffix + 2;
        }
        if (always || amt > 0) {
            return suffix + 1;
        }
        return 0;
    }

    private static int printField(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
        if (!always && amt <= 0) {
            return pos;
        }
        int startPos = pos;
        if ((always && zeropad >= 3) || amt > 99) {
            int dig = amt / 100;
            formatStr[pos] = (char) (dig + 48);
            pos++;
            amt -= dig * 100;
        }
        if ((always && zeropad >= 2) || amt > 9 || startPos != pos) {
            int dig2 = amt / 10;
            formatStr[pos] = (char) (dig2 + 48);
            pos++;
            amt -= dig2 * 10;
        }
        formatStr[pos] = (char) (amt + 48);
        int pos2 = pos + 1;
        formatStr[pos2] = suffix;
        return pos2 + 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0137, code lost:
        if (r9 != r7) goto L_0x013e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int formatDurationLocked(long r27, int r29) {
        /*
            r0 = r27
            r2 = r29
            char[] r3 = sFormatStr
            int r3 = r3.length
            if (r3 >= r2) goto L_0x000e
            char[] r3 = new char[r2]
            sFormatStr = r3
            goto L_0x000f
        L_0x000e:
        L_0x000f:
            char[] r3 = sFormatStr
            r4 = 32
            r5 = 0
            int r7 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x0028
            r5 = 0
            int r2 = r2 + -1
        L_0x001c:
            if (r5 >= r2) goto L_0x0021
            r3[r5] = r4
            goto L_0x001c
        L_0x0021:
            r4 = 48
            r3[r5] = r4
            int r4 = r5 + 1
            return r4
        L_0x0028:
            int r7 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0030
            r5 = 43
            r10 = r5
            goto L_0x0034
        L_0x0030:
            r5 = 45
            long r0 = -r0
            r10 = r5
        L_0x0034:
            r5 = 1000(0x3e8, double:4.94E-321)
            long r7 = r0 % r5
            int r11 = (int) r7
            long r5 = r0 / r5
            double r5 = (double) r5
            double r5 = java.lang.Math.floor(r5)
            int r5 = (int) r5
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 86400(0x15180, float:1.21072E-40)
            if (r5 <= r9) goto L_0x0050
            int r6 = r5 / r9
            int r9 = r9 * r6
            int r5 = r5 - r9
            r12 = r6
            goto L_0x0051
        L_0x0050:
            r12 = r6
        L_0x0051:
            r6 = 3600(0xe10, float:5.045E-42)
            if (r5 <= r6) goto L_0x005c
            int r6 = r5 / 3600
            int r7 = r6 * 3600
            int r5 = r5 - r7
            r13 = r6
            goto L_0x005d
        L_0x005c:
            r13 = r7
        L_0x005d:
            r6 = 60
            if (r5 <= r6) goto L_0x0069
            int r6 = r5 / 60
            int r7 = r6 * 60
            int r5 = r5 - r7
            r15 = r5
            r14 = r6
            goto L_0x006b
        L_0x0069:
            r15 = r5
            r14 = r8
        L_0x006b:
            r5 = 0
            r16 = 3
            r9 = 2
            r8 = 0
            r7 = 1
            if (r2 == 0) goto L_0x00a9
            int r6 = accumField(r12, r7, r8, r8)
            if (r6 <= 0) goto L_0x007b
            r8 = 1
        L_0x007b:
            int r8 = accumField(r13, r7, r8, r9)
            int r6 = r6 + r8
            if (r6 <= 0) goto L_0x0084
            r8 = 1
            goto L_0x0085
        L_0x0084:
            r8 = 0
        L_0x0085:
            int r8 = accumField(r14, r7, r8, r9)
            int r6 = r6 + r8
            if (r6 <= 0) goto L_0x008e
            r8 = 1
            goto L_0x008f
        L_0x008e:
            r8 = 0
        L_0x008f:
            int r8 = accumField(r15, r7, r8, r9)
            int r6 = r6 + r8
            if (r6 <= 0) goto L_0x0098
            r8 = 3
            goto L_0x0099
        L_0x0098:
            r8 = 0
        L_0x0099:
            int r8 = accumField(r11, r9, r7, r8)
            int r8 = r8 + r7
            int r6 = r6 + r8
        L_0x009f:
            if (r6 >= r2) goto L_0x00a8
            r3[r5] = r4
            int r5 = r5 + 1
            int r6 = r6 + 1
            goto L_0x009f
        L_0x00a8:
            goto L_0x00aa
        L_0x00a9:
        L_0x00aa:
            r3[r5] = r10
            int r17 = r5 + 1
            r8 = r17
            if (r2 == 0) goto L_0x00b4
            r4 = 1
            goto L_0x00b5
        L_0x00b4:
            r4 = 0
        L_0x00b5:
            r18 = r4
            r6 = 100
            r19 = 0
            r20 = 0
            r4 = r3
            r5 = r12
            r21 = 1
            r7 = r17
            r22 = r8
            r23 = 0
            r8 = r19
            r19 = 2
            r9 = r20
            int r9 = printField(r4, r5, r6, r7, r8, r9)
            r6 = 104(0x68, float:1.46E-43)
            r8 = r22
            if (r9 == r8) goto L_0x00da
            r17 = 1
            goto L_0x00dc
        L_0x00da:
            r17 = 0
        L_0x00dc:
            if (r18 == 0) goto L_0x00e1
            r20 = 2
            goto L_0x00e3
        L_0x00e1:
            r20 = 0
        L_0x00e3:
            r4 = r3
            r5 = r13
            r7 = r9
            r24 = r8
            r8 = r17
            r17 = r9
            r9 = r20
            int r9 = printField(r4, r5, r6, r7, r8, r9)
            r6 = 109(0x6d, float:1.53E-43)
            r8 = r24
            if (r9 == r8) goto L_0x00fb
            r17 = 1
            goto L_0x00fd
        L_0x00fb:
            r17 = 0
        L_0x00fd:
            if (r18 == 0) goto L_0x0102
            r20 = 2
            goto L_0x0104
        L_0x0102:
            r20 = 0
        L_0x0104:
            r4 = r3
            r5 = r14
            r7 = r9
            r25 = r8
            r8 = r17
            r17 = r9
            r9 = r20
            int r9 = printField(r4, r5, r6, r7, r8, r9)
            r6 = 115(0x73, float:1.61E-43)
            r8 = r25
            if (r9 == r8) goto L_0x011a
            goto L_0x011c
        L_0x011a:
            r21 = 0
        L_0x011c:
            if (r18 == 0) goto L_0x011f
            goto L_0x0121
        L_0x011f:
            r19 = 0
        L_0x0121:
            r4 = r3
            r5 = r15
            r7 = r9
            r26 = r8
            r8 = r21
            r17 = r9
            r9 = r19
            int r9 = printField(r4, r5, r6, r7, r8, r9)
            r6 = 109(0x6d, float:1.53E-43)
            r8 = 1
            if (r18 == 0) goto L_0x013a
            r7 = r26
            if (r9 == r7) goto L_0x013c
            goto L_0x013e
        L_0x013a:
            r7 = r26
        L_0x013c:
            r16 = 0
        L_0x013e:
            r4 = r3
            r5 = r11
            r17 = r7
            r7 = r9
            r19 = r9
            r9 = r16
            int r4 = printField(r4, r5, r6, r7, r8, r9)
            r5 = 115(0x73, float:1.61E-43)
            r3[r4] = r5
            int r5 = r4 + 1
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.util.TimeUtils.formatDurationLocked(long, int):int");
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static void formatDuration(long duration, StringBuilder builder) {
        synchronized (sFormatSync) {
            builder.append(sFormatStr, 0, formatDurationLocked(duration, 0));
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static void formatDuration(long duration, PrintWriter pw, int fieldLen) {
        synchronized (sFormatSync) {
            pw.print(new String(sFormatStr, 0, formatDurationLocked(duration, fieldLen)));
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static void formatDuration(long duration, PrintWriter pw) {
        formatDuration(duration, pw, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static void formatDuration(long time, long now, PrintWriter pw) {
        if (time == 0) {
            pw.print("--");
        } else {
            formatDuration(time - now, pw, 0);
        }
    }

    private TimeUtils() {
    }
}
