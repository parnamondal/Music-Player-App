package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p000v4.util.ArraySet;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;

    public static class ParcelException extends RuntimeException {
        public ParcelException(Throwable source) {
            super(source);
        }
    }

    /* access modifiers changed from: protected */
    public abstract void closeField();

    /* access modifiers changed from: protected */
    public abstract VersionedParcel createSubParcel();

    /* access modifiers changed from: protected */
    public abstract boolean readBoolean();

    /* access modifiers changed from: protected */
    public abstract Bundle readBundle();

    /* access modifiers changed from: protected */
    public abstract byte[] readByteArray();

    /* access modifiers changed from: protected */
    public abstract double readDouble();

    /* access modifiers changed from: protected */
    public abstract boolean readField(int i);

    /* access modifiers changed from: protected */
    public abstract float readFloat();

    /* access modifiers changed from: protected */
    public abstract int readInt();

    /* access modifiers changed from: protected */
    public abstract long readLong();

    /* access modifiers changed from: protected */
    public abstract <T extends Parcelable> T readParcelable();

    /* access modifiers changed from: protected */
    public abstract String readString();

    /* access modifiers changed from: protected */
    public abstract IBinder readStrongBinder();

    /* access modifiers changed from: protected */
    public abstract void setOutputField(int i);

    /* access modifiers changed from: protected */
    public abstract void writeBoolean(boolean z);

    /* access modifiers changed from: protected */
    public abstract void writeBundle(Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void writeByteArray(byte[] bArr);

    /* access modifiers changed from: protected */
    public abstract void writeByteArray(byte[] bArr, int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void writeDouble(double d);

    /* access modifiers changed from: protected */
    public abstract void writeFloat(float f);

    /* access modifiers changed from: protected */
    public abstract void writeInt(int i);

    /* access modifiers changed from: protected */
    public abstract void writeLong(long j);

    /* access modifiers changed from: protected */
    public abstract void writeParcelable(Parcelable parcelable);

    /* access modifiers changed from: protected */
    public abstract void writeString(String str);

    /* access modifiers changed from: protected */
    public abstract void writeStrongBinder(IBinder iBinder);

    /* access modifiers changed from: protected */
    public abstract void writeStrongInterface(IInterface iInterface);

    public boolean isStream() {
        return false;
    }

    public void setSerializationFlags(boolean allowSerialization, boolean ignoreParcelables) {
    }

    public void writeStrongInterface(IInterface val, int fieldId) {
        setOutputField(fieldId);
        writeStrongInterface(val);
    }

    public void writeBundle(Bundle val, int fieldId) {
        setOutputField(fieldId);
        writeBundle(val);
    }

    public void writeBoolean(boolean val, int fieldId) {
        setOutputField(fieldId);
        writeBoolean(val);
    }

    public void writeByteArray(byte[] b, int fieldId) {
        setOutputField(fieldId);
        writeByteArray(b);
    }

    public void writeByteArray(byte[] b, int offset, int len, int fieldId) {
        setOutputField(fieldId);
        writeByteArray(b, offset, len);
    }

    public void writeInt(int val, int fieldId) {
        setOutputField(fieldId);
        writeInt(val);
    }

    public void writeLong(long val, int fieldId) {
        setOutputField(fieldId);
        writeLong(val);
    }

    public void writeFloat(float val, int fieldId) {
        setOutputField(fieldId);
        writeFloat(val);
    }

    public void writeDouble(double val, int fieldId) {
        setOutputField(fieldId);
        writeDouble(val);
    }

    public void writeString(String val, int fieldId) {
        setOutputField(fieldId);
        writeString(val);
    }

    public void writeStrongBinder(IBinder val, int fieldId) {
        setOutputField(fieldId);
        writeStrongBinder(val);
    }

    public void writeParcelable(Parcelable p, int fieldId) {
        setOutputField(fieldId);
        writeParcelable(p);
    }

    public boolean readBoolean(boolean def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readBoolean();
    }

    public int readInt(int def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readInt();
    }

    public long readLong(long def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readLong();
    }

    public float readFloat(float def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readFloat();
    }

    public double readDouble(double def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readDouble();
    }

    public String readString(String def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readString();
    }

    public IBinder readStrongBinder(IBinder def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readStrongBinder();
    }

    public byte[] readByteArray(byte[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readByteArray();
    }

    public <T extends Parcelable> T readParcelable(T def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readParcelable();
    }

    public Bundle readBundle(Bundle def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readBundle();
    }

    public void writeByte(byte val, int fieldId) {
        setOutputField(fieldId);
        writeInt(val);
    }

    @RequiresApi(api = 21)
    public void writeSize(Size val, int fieldId) {
        setOutputField(fieldId);
        writeBoolean(val != null);
        if (val != null) {
            writeInt(val.getWidth());
            writeInt(val.getHeight());
        }
    }

    @RequiresApi(api = 21)
    public void writeSizeF(SizeF val, int fieldId) {
        setOutputField(fieldId);
        writeBoolean(val != null);
        if (val != null) {
            writeFloat(val.getWidth());
            writeFloat(val.getHeight());
        }
    }

    public void writeSparseBooleanArray(SparseBooleanArray val, int fieldId) {
        setOutputField(fieldId);
        if (val == null) {
            writeInt(-1);
            return;
        }
        int n = val.size();
        writeInt(n);
        for (int i = 0; i < n; i++) {
            writeInt(val.keyAt(i));
            writeBoolean(val.valueAt(i));
        }
    }

    public void writeBooleanArray(boolean[] val, int fieldId) {
        setOutputField(fieldId);
        writeBooleanArray(val);
    }

    /* access modifiers changed from: protected */
    public void writeBooleanArray(boolean[] val) {
        if (val != null) {
            int n = val.length;
            writeInt(n);
            for (int i = 0; i < n; i++) {
                writeInt(val[i] ? 1 : 0);
            }
            return;
        }
        writeInt(-1);
    }

    public boolean[] readBooleanArray(boolean[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readBooleanArray();
    }

    /* access modifiers changed from: protected */
    public boolean[] readBooleanArray() {
        int n = readInt();
        if (n < 0) {
            return null;
        }
        boolean[] val = new boolean[n];
        for (int i = 0; i < n; i++) {
            val[i] = readInt() != 0;
        }
        return val;
    }

    public void writeCharArray(char[] val, int fieldId) {
        setOutputField(fieldId);
        if (val != null) {
            writeInt(n);
            for (char writeInt : val) {
                writeInt(writeInt);
            }
            return;
        }
        writeInt(-1);
    }

    public char[] readCharArray(char[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        int n = readInt();
        if (n < 0) {
            return null;
        }
        char[] val = new char[n];
        for (int i = 0; i < n; i++) {
            val[i] = (char) readInt();
        }
        return val;
    }

    public void writeIntArray(int[] val, int fieldId) {
        setOutputField(fieldId);
        writeIntArray(val);
    }

    /* access modifiers changed from: protected */
    public void writeIntArray(int[] val) {
        if (val != null) {
            writeInt(n);
            for (int writeInt : val) {
                writeInt(writeInt);
            }
            return;
        }
        writeInt(-1);
    }

    public int[] readIntArray(int[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readIntArray();
    }

    /* access modifiers changed from: protected */
    public int[] readIntArray() {
        int n = readInt();
        if (n < 0) {
            return null;
        }
        int[] val = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = readInt();
        }
        return val;
    }

    public void writeLongArray(long[] val, int fieldId) {
        setOutputField(fieldId);
        writeLongArray(val);
    }

    /* access modifiers changed from: protected */
    public void writeLongArray(long[] val) {
        if (val != null) {
            writeInt(n);
            for (long writeLong : val) {
                writeLong(writeLong);
            }
            return;
        }
        writeInt(-1);
    }

    public long[] readLongArray(long[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readLongArray();
    }

    /* access modifiers changed from: protected */
    public long[] readLongArray() {
        int n = readInt();
        if (n < 0) {
            return null;
        }
        long[] val = new long[n];
        for (int i = 0; i < n; i++) {
            val[i] = readLong();
        }
        return val;
    }

    public void writeFloatArray(float[] val, int fieldId) {
        setOutputField(fieldId);
        writeFloatArray(val);
    }

    /* access modifiers changed from: protected */
    public void writeFloatArray(float[] val) {
        if (val != null) {
            writeInt(n);
            for (float writeFloat : val) {
                writeFloat(writeFloat);
            }
            return;
        }
        writeInt(-1);
    }

    public float[] readFloatArray(float[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readFloatArray();
    }

    /* access modifiers changed from: protected */
    public float[] readFloatArray() {
        int n = readInt();
        if (n < 0) {
            return null;
        }
        float[] val = new float[n];
        for (int i = 0; i < n; i++) {
            val[i] = readFloat();
        }
        return val;
    }

    public void writeDoubleArray(double[] val, int fieldId) {
        setOutputField(fieldId);
        writeDoubleArray(val);
    }

    /* access modifiers changed from: protected */
    public void writeDoubleArray(double[] val) {
        if (val != null) {
            writeInt(n);
            for (double writeDouble : val) {
                writeDouble(writeDouble);
            }
            return;
        }
        writeInt(-1);
    }

    public double[] readDoubleArray(double[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readDoubleArray();
    }

    /* access modifiers changed from: protected */
    public double[] readDoubleArray() {
        int n = readInt();
        if (n < 0) {
            return null;
        }
        double[] val = new double[n];
        for (int i = 0; i < n; i++) {
            val[i] = readDouble();
        }
        return val;
    }

    public <T> void writeSet(Set<T> val, int fieldId) {
        writeCollection(val, fieldId);
    }

    public <T> void writeList(List<T> val, int fieldId) {
        writeCollection(val, fieldId);
    }

    private <T> void writeCollection(Collection<T> val, int fieldId) {
        setOutputField(fieldId);
        if (val == null) {
            writeInt(-1);
            return;
        }
        int n = val.size();
        writeInt(n);
        if (n > 0) {
            int type = getType(val.iterator().next());
            writeInt(type);
            switch (type) {
                case 1:
                    for (T writeVersionedParcelable : val) {
                        writeVersionedParcelable(writeVersionedParcelable);
                    }
                    break;
                case 2:
                    for (T writeParcelable : val) {
                        writeParcelable(writeParcelable);
                    }
                    break;
                case 3:
                    for (T writeSerializable : val) {
                        writeSerializable(writeSerializable);
                    }
                    break;
                case 4:
                    for (T writeString : val) {
                        writeString(writeString);
                    }
                    break;
                case 5:
                    for (T writeStrongBinder : val) {
                        writeStrongBinder(writeStrongBinder);
                    }
                    break;
            }
        }
    }

    public <T> void writeArray(T[] val, int fieldId) {
        setOutputField(fieldId);
        writeArray(val);
    }

    /* access modifiers changed from: protected */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> void writeArray(T[] r5) {
        /*
            r4 = this;
            if (r5 != 0) goto L_0x0007
            r0 = -1
            r4.writeInt(r0)
            return
        L_0x0007:
            int r0 = r5.length
            r1 = 0
            r4.writeInt(r0)
            if (r0 <= 0) goto L_0x0061
            r2 = 0
            r2 = r5[r2]
            int r2 = r4.getType(r2)
            r4.writeInt(r2)
            switch(r2) {
                case 1: goto L_0x0053;
                case 2: goto L_0x0045;
                case 3: goto L_0x0037;
                case 4: goto L_0x0029;
                case 5: goto L_0x001c;
                default: goto L_0x001b;
            }
        L_0x001b:
            goto L_0x0062
        L_0x001c:
            if (r1 >= r0) goto L_0x0028
            r3 = r5[r1]
            android.os.IBinder r3 = (android.os.IBinder) r3
            r4.writeStrongBinder(r3)
            int r1 = r1 + 1
            goto L_0x001c
        L_0x0028:
            goto L_0x0062
        L_0x0029:
        L_0x002a:
            if (r1 >= r0) goto L_0x0036
            r3 = r5[r1]
            java.lang.String r3 = (java.lang.String) r3
            r4.writeString(r3)
            int r1 = r1 + 1
            goto L_0x002a
        L_0x0036:
            goto L_0x0062
        L_0x0037:
        L_0x0038:
            if (r1 >= r0) goto L_0x0044
            r3 = r5[r1]
            java.io.Serializable r3 = (java.io.Serializable) r3
            r4.writeSerializable(r3)
            int r1 = r1 + 1
            goto L_0x0038
        L_0x0044:
            goto L_0x0062
        L_0x0045:
        L_0x0046:
            if (r1 >= r0) goto L_0x0052
            r3 = r5[r1]
            android.os.Parcelable r3 = (android.os.Parcelable) r3
            r4.writeParcelable(r3)
            int r1 = r1 + 1
            goto L_0x0046
        L_0x0052:
            goto L_0x0062
        L_0x0053:
        L_0x0054:
            if (r1 >= r0) goto L_0x0060
            r3 = r5[r1]
            androidx.versionedparcelable.VersionedParcelable r3 = (androidx.versionedparcelable.VersionedParcelable) r3
            r4.writeVersionedParcelable(r3)
            int r1 = r1 + 1
            goto L_0x0054
        L_0x0060:
            goto L_0x0062
        L_0x0061:
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.writeArray(java.lang.Object[]):void");
    }

    private <T> int getType(T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        sb.append(" cannot be VersionedParcelled");
        throw new IllegalArgumentException(sb.toString());
    }

    public void writeVersionedParcelable(VersionedParcelable p, int fieldId) {
        setOutputField(fieldId);
        writeVersionedParcelable(p);
    }

    /* access modifiers changed from: protected */
    public void writeVersionedParcelable(VersionedParcelable p) {
        if (p == null) {
            writeString(null);
            return;
        }
        writeVersionedParcelableCreator(p);
        VersionedParcel subParcel = createSubParcel();
        writeToParcel(p, subParcel);
        subParcel.closeField();
    }

    private void writeVersionedParcelableCreator(VersionedParcelable p) {
        try {
            writeString(findParcelClass(p.getClass()).getName());
        } catch (ClassNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(p.getClass().getSimpleName());
            sb.append(" does not have a Parcelizer");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void writeSerializable(Serializable s, int fieldId) {
        setOutputField(fieldId);
        writeSerializable(s);
    }

    private void writeSerializable(Serializable s) {
        if (s == null) {
            writeString(null);
            return;
        }
        String name = s.getClass().getName();
        writeString(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.close();
            writeByteArray(baos.toByteArray());
        } catch (IOException ioe) {
            StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered IOException writing serializable object (name = ");
            sb.append(name);
            sb.append(")");
            throw new RuntimeException(sb.toString(), ioe);
        }
    }

    public void writeException(Exception e, int fieldId) {
        setOutputField(fieldId);
        if (e == null) {
            writeNoException();
            return;
        }
        int code = 0;
        if ((e instanceof Parcelable) && e.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            code = EX_PARCELABLE;
        } else if (e instanceof SecurityException) {
            code = -1;
        } else if (e instanceof BadParcelableException) {
            code = -2;
        } else if (e instanceof IllegalArgumentException) {
            code = -3;
        } else if (e instanceof NullPointerException) {
            code = -4;
        } else if (e instanceof IllegalStateException) {
            code = EX_ILLEGAL_STATE;
        } else if (e instanceof NetworkOnMainThreadException) {
            code = EX_NETWORK_MAIN_THREAD;
        } else if (e instanceof UnsupportedOperationException) {
            code = EX_UNSUPPORTED_OPERATION;
        }
        writeInt(code);
        if (code != 0) {
            writeString(e.getMessage());
            if (code == EX_PARCELABLE) {
                writeParcelable((Parcelable) e);
            }
        } else if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        } else {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void writeNoException() {
        writeInt(0);
    }

    public Exception readException(Exception def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        int code = readExceptionCode();
        if (code != 0) {
            return readException(code, readString());
        }
        return def;
    }

    private int readExceptionCode() {
        return readInt();
    }

    private Exception readException(int code, String msg) {
        return createException(code, msg);
    }

    @NonNull
    protected static Throwable getRootCause(@NonNull Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    private Exception createException(int code, String msg) {
        switch (code) {
            case EX_PARCELABLE /*-9*/:
                return (Exception) readParcelable();
            case EX_UNSUPPORTED_OPERATION /*-7*/:
                return new UnsupportedOperationException(msg);
            case EX_NETWORK_MAIN_THREAD /*-6*/:
                return new NetworkOnMainThreadException();
            case EX_ILLEGAL_STATE /*-5*/:
                return new IllegalStateException(msg);
            case -4:
                return new NullPointerException(msg);
            case -3:
                return new IllegalArgumentException(msg);
            case -2:
                return new BadParcelableException(msg);
            case -1:
                return new SecurityException(msg);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown exception code: ");
                sb.append(code);
                sb.append(" msg ");
                sb.append(msg);
                return new RuntimeException(sb.toString());
        }
    }

    public byte readByte(byte def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return (byte) (readInt() & 255);
    }

    @RequiresApi(api = 21)
    public Size readSize(Size def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        if (readBoolean()) {
            return new Size(readInt(), readInt());
        }
        return null;
    }

    @RequiresApi(api = 21)
    public SizeF readSizeF(SizeF def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        if (readBoolean()) {
            return new SizeF(readFloat(), readFloat());
        }
        return null;
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        int n = readInt();
        if (n < 0) {
            return null;
        }
        SparseBooleanArray sa = new SparseBooleanArray(n);
        for (int i = 0; i < n; i++) {
            sa.put(readInt(), readBoolean());
        }
        return sa;
    }

    public <T> Set<T> readSet(Set<T> def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return (Set) readCollection(fieldId, new ArraySet());
    }

    public <T> List<T> readList(List<T> def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return (List) readCollection(fieldId, new ArrayList());
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <T, S extends java.util.Collection<T>> S readCollection(int r4, S r5) {
        /*
            r3 = this;
            int r0 = r3.readInt()
            r1 = 0
            if (r0 >= 0) goto L_0x0008
            return r1
        L_0x0008:
            if (r0 == 0) goto L_0x005a
            int r2 = r3.readInt()
            if (r0 >= 0) goto L_0x0011
            return r1
        L_0x0011:
            switch(r2) {
                case 1: goto L_0x004c;
                case 2: goto L_0x003e;
                case 3: goto L_0x0030;
                case 4: goto L_0x0022;
                case 5: goto L_0x0015;
                default: goto L_0x0014;
            }
        L_0x0014:
            goto L_0x005b
        L_0x0015:
            if (r0 <= 0) goto L_0x0021
            android.os.IBinder r1 = r3.readStrongBinder()
            r5.add(r1)
            int r0 = r0 + -1
            goto L_0x0015
        L_0x0021:
            goto L_0x005b
        L_0x0022:
        L_0x0023:
            if (r0 <= 0) goto L_0x002f
            java.lang.String r1 = r3.readString()
            r5.add(r1)
            int r0 = r0 + -1
            goto L_0x0023
        L_0x002f:
            goto L_0x005b
        L_0x0030:
        L_0x0031:
            if (r0 <= 0) goto L_0x003d
            java.io.Serializable r1 = r3.readSerializable()
            r5.add(r1)
            int r0 = r0 + -1
            goto L_0x0031
        L_0x003d:
            goto L_0x005b
        L_0x003e:
        L_0x003f:
            if (r0 <= 0) goto L_0x004b
            android.os.Parcelable r1 = r3.readParcelable()
            r5.add(r1)
            int r0 = r0 + -1
            goto L_0x003f
        L_0x004b:
            goto L_0x005b
        L_0x004c:
        L_0x004d:
            if (r0 <= 0) goto L_0x0059
            androidx.versionedparcelable.VersionedParcelable r1 = r3.readVersionedParcelable()
            r5.add(r1)
            int r0 = r0 + -1
            goto L_0x004d
        L_0x0059:
            goto L_0x005b
        L_0x005a:
        L_0x005b:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.readCollection(int, java.util.Collection):java.util.Collection");
    }

    public <T> T[] readArray(T[] def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readArray(def);
    }

    /* access modifiers changed from: protected */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T[] readArray(T[] r5) {
        /*
            r4 = this;
            int r0 = r4.readInt()
            r1 = 0
            if (r0 >= 0) goto L_0x0008
            return r1
        L_0x0008:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r0)
            if (r0 == 0) goto L_0x005f
            int r3 = r4.readInt()
            if (r0 >= 0) goto L_0x0016
            return r1
        L_0x0016:
            switch(r3) {
                case 1: goto L_0x0051;
                case 2: goto L_0x0043;
                case 3: goto L_0x0035;
                case 4: goto L_0x0027;
                case 5: goto L_0x001a;
                default: goto L_0x0019;
            }
        L_0x0019:
            goto L_0x0060
        L_0x001a:
            if (r0 <= 0) goto L_0x0026
            android.os.IBinder r1 = r4.readStrongBinder()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x001a
        L_0x0026:
            goto L_0x0060
        L_0x0027:
        L_0x0028:
            if (r0 <= 0) goto L_0x0034
            java.lang.String r1 = r4.readString()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0028
        L_0x0034:
            goto L_0x0060
        L_0x0035:
        L_0x0036:
            if (r0 <= 0) goto L_0x0042
            java.io.Serializable r1 = r4.readSerializable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0036
        L_0x0042:
            goto L_0x0060
        L_0x0043:
        L_0x0044:
            if (r0 <= 0) goto L_0x0050
            android.os.Parcelable r1 = r4.readParcelable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0044
        L_0x0050:
            goto L_0x0060
        L_0x0051:
        L_0x0052:
            if (r0 <= 0) goto L_0x005e
            androidx.versionedparcelable.VersionedParcelable r1 = r4.readVersionedParcelable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0052
        L_0x005e:
            goto L_0x0060
        L_0x005f:
        L_0x0060:
            java.lang.Object[] r1 = r2.toArray(r5)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.readArray(java.lang.Object[]):java.lang.Object[]");
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T def, int fieldId) {
        if (!readField(fieldId)) {
            return def;
        }
        return readVersionedParcelable();
    }

    /* access modifiers changed from: protected */
    public <T extends VersionedParcelable> T readVersionedParcelable() {
        String name = readString();
        if (name == null) {
            return null;
        }
        return readFromParcel(name, createSubParcel());
    }

    /* access modifiers changed from: protected */
    public Serializable readSerializable() {
        String name = readString();
        if (name == null) {
            return null;
        }
        try {
            return (Serializable) new ObjectInputStream(new ByteArrayInputStream(readByteArray())) {
                /* access modifiers changed from: protected */
                public Class<?> resolveClass(ObjectStreamClass osClass) throws IOException, ClassNotFoundException {
                    Class<?> c = Class.forName(osClass.getName(), false, getClass().getClassLoader());
                    if (c != null) {
                        return c;
                    }
                    return super.resolveClass(osClass);
                }
            }.readObject();
        } catch (IOException ioe) {
            StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            sb.append(name);
            sb.append(")");
            throw new RuntimeException(sb.toString(), ioe);
        } catch (ClassNotFoundException cnfe) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            sb2.append(name);
            sb2.append(")");
            throw new RuntimeException(sb2.toString(), cnfe);
        }
    }

    protected static <T extends VersionedParcelable> T readFromParcel(String parcelCls, VersionedParcel versionedParcel) {
        try {
            return (VersionedParcelable) Class.forName(parcelCls, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", new Class[]{VersionedParcel.class}).invoke(null, new Object[]{versionedParcel});
        } catch (IllegalAccessException e) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e2.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e4);
        }
    }

    protected static <T extends VersionedParcelable> void writeToParcel(T val, VersionedParcel versionedParcel) {
        try {
            findParcelClass(val).getDeclaredMethod("write", new Class[]{val.getClass(), VersionedParcel.class}).invoke(null, new Object[]{val, versionedParcel});
        } catch (IllegalAccessException e) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e2.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e4);
        }
    }

    private static <T extends VersionedParcelable> Class findParcelClass(T val) throws ClassNotFoundException {
        return findParcelClass(val.getClass());
    }

    private static Class findParcelClass(Class<? extends VersionedParcelable> cls) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sParcelizer", new Object[]{cls.getPackage().getName(), cls.getSimpleName()}), false, cls.getClassLoader());
    }
}
