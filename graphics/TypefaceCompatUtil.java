package android.support.p000v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatUtil */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    @Nullable
    public static File getTempFile(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(CACHE_FILE_PREFIX);
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        String prefix = sb.toString();
        int i = 0;
        while (i < 100) {
            File cacheDir = context.getCacheDir();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(prefix);
            sb2.append(i);
            File file = new File(cacheDir, sb2.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException e) {
            }
        }
        return null;
    }

    @Nullable
    @RequiresApi(19)
    private static ByteBuffer mmap(File file) {
        Throwable th;
        Throwable th2;
        try {
            FileInputStream fis = new FileInputStream(file);
            try {
                FileChannel channel = fis.getChannel();
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                fis.close();
                return map;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r2;
                th2 = th4;
            }
            if (th != null) {
                try {
                    fis.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                fis.close();
            }
            throw th2;
            throw th2;
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0050, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r5.addSuppressed(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005d, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005e, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x006b, code lost:
        if (r4 != null) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0072, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r4.addSuppressed(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0078, code lost:
        r2.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005d A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x0014] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x006b  */
    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r11, android.os.CancellationSignal r12, android.net.Uri r13) {
        /*
            android.content.ContentResolver r0 = r11.getContentResolver()
            r1 = 0
            java.lang.String r2 = "r"
            android.os.ParcelFileDescriptor r2 = r0.openFileDescriptor(r13, r2, r12)     // Catch:{ IOException -> 0x007f }
            if (r2 != 0) goto L_0x0014
            if (r2 == 0) goto L_0x0013
            r2.close()     // Catch:{ IOException -> 0x007f }
        L_0x0013:
            return r1
        L_0x0014:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            java.io.FileDescriptor r4 = r2.getFileDescriptor()     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            java.nio.channels.FileChannel r4 = r3.getChannel()     // Catch:{ Throwable -> 0x0040, all -> 0x003d }
            long r8 = r4.size()     // Catch:{ Throwable -> 0x0040, all -> 0x003d }
            java.nio.channels.FileChannel$MapMode r5 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ Throwable -> 0x0040, all -> 0x003d }
            r6 = 0
            java.nio.MappedByteBuffer r5 = r4.map(r5, r6, r8)     // Catch:{ Throwable -> 0x0040, all -> 0x003d }
            r3.close()     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ IOException -> 0x007f }
        L_0x003a:
            return r5
        L_0x003d:
            r4 = move-exception
            r5 = r1
            goto L_0x0047
        L_0x0040:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0043 }
        L_0x0043:
            r5 = move-exception
            r10 = r5
            r5 = r4
            r4 = r10
        L_0x0047:
            if (r5 == 0) goto L_0x0056
            r3.close()     // Catch:{ Throwable -> 0x0050, all -> 0x005d }
            goto L_0x005a
        L_0x0050:
            r6 = move-exception
            r5.addSuppressed(r6)     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            goto L_0x005a
        L_0x0056:
            r3.close()     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
        L_0x005a:
            throw r4     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
        L_0x005d:
            r3 = move-exception
            r4 = r1
            goto L_0x0067
        L_0x0060:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r4 = move-exception
            r10 = r4
            r4 = r3
            r3 = r10
        L_0x0067:
            if (r2 == 0) goto L_0x007c
            if (r4 == 0) goto L_0x0078
            r2.close()     // Catch:{ Throwable -> 0x0072 }
            goto L_0x007c
        L_0x0072:
            r5 = move-exception
            r4.addSuppressed(r5)     // Catch:{ IOException -> 0x007f }
            goto L_0x007c
        L_0x0078:
            r2.close()     // Catch:{ IOException -> 0x007f }
        L_0x007c:
            throw r3     // Catch:{ IOException -> 0x007f }
        L_0x007f:
            r2 = move-exception
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @Nullable
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources res, int id) {
        File tmpFile = getTempFile(context);
        ByteBuffer byteBuffer = null;
        if (tmpFile == null) {
            return null;
        }
        try {
            if (copyToFile(tmpFile, res, id)) {
                byteBuffer = mmap(tmpFile);
            }
            return byteBuffer;
        } finally {
            tmpFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream is) {
        FileOutputStream os = null;
        ThreadPolicy old = StrictMode.allowThreadDiskWrites();
        try {
            os = new FileOutputStream(file, false);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = is.read(buffer);
                int readLen = read;
                if (read != -1) {
                    os.write(buffer, 0, readLen);
                } else {
                    return true;
                }
            }
        } catch (IOException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Error copying resource contents to temp file: ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return false;
        } finally {
            closeQuietly(os);
            StrictMode.setThreadPolicy(old);
        }
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            return copyToFile(file, is);
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
