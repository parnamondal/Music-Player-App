package android.support.p000v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* renamed from: android.support.v4.util.LongSparseArray */
public class LongSparseArray<E> implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            int initialCapacity2 = ContainerHelpers.idealLongArraySize(initialCapacity);
            this.mKeys = new long[initialCapacity2];
            this.mValues = new Object[initialCapacity2];
        }
        this.mSize = 0;
    }

    public LongSparseArray<E> clone() {
        try {
            LongSparseArray<E> clone = (LongSparseArray) super.clone();
            clone.mKeys = (long[]) this.mKeys.clone();
            clone.mValues = (Object[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Nullable
    public E get(long key) {
        return get(key, null);
    }

    public E get(long key, E valueIfKeyNotFound) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            E[] eArr = this.mValues;
            if (eArr[i] != DELETED) {
                return eArr[i];
            }
        }
        return valueIfKeyNotFound;
    }

    public void delete(long key) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            Object[] objArr = this.mValues;
            Object obj = objArr[i];
            Object obj2 = DELETED;
            if (obj != obj2) {
                objArr[i] = obj2;
                this.mGarbage = true;
            }
        }
    }

    public void remove(long key) {
        delete(key);
    }

    public void removeAt(int index) {
        Object[] objArr = this.mValues;
        Object obj = objArr[index];
        Object obj2 = DELETED;
        if (obj != obj2) {
            objArr[index] = obj2;
            this.mGarbage = true;
        }
    }

    /* renamed from: gc */
    private void m4gc() {
        int n = this.mSize;
        int o = 0;
        long[] keys = this.mKeys;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public void put(long key, E value) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
        } else {
            int i2 = i ^ -1;
            if (i2 < this.mSize) {
                Object[] objArr = this.mValues;
                if (objArr[i2] == DELETED) {
                    this.mKeys[i2] = key;
                    objArr[i2] = value;
                    return;
                }
            }
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                m4gc();
                i2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key) ^ -1;
            }
            int i3 = this.mSize;
            if (i3 >= this.mKeys.length) {
                int n = ContainerHelpers.idealLongArraySize(i3 + 1);
                long[] nkeys = new long[n];
                Object[] nvalues = new Object[n];
                long[] jArr = this.mKeys;
                System.arraycopy(jArr, 0, nkeys, 0, jArr.length);
                Object[] objArr2 = this.mValues;
                System.arraycopy(objArr2, 0, nvalues, 0, objArr2.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            int i4 = this.mSize;
            if (i4 - i2 != 0) {
                long[] jArr2 = this.mKeys;
                System.arraycopy(jArr2, i2, jArr2, i2 + 1, i4 - i2);
                Object[] objArr3 = this.mValues;
                System.arraycopy(objArr3, i2, objArr3, i2 + 1, this.mSize - i2);
            }
            this.mKeys[i2] = key;
            this.mValues[i2] = value;
            this.mSize++;
        }
    }

    public void putAll(@NonNull LongSparseArray<? extends E> other) {
        int size = other.size();
        for (int i = 0; i < size; i++) {
            put(other.keyAt(i), other.valueAt(i));
        }
    }

    public int size() {
        if (this.mGarbage) {
            m4gc();
        }
        return this.mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public long keyAt(int index) {
        if (this.mGarbage) {
            m4gc();
        }
        return this.mKeys[index];
    }

    public E valueAt(int index) {
        if (this.mGarbage) {
            m4gc();
        }
        return this.mValues[index];
    }

    public void setValueAt(int index, E value) {
        if (this.mGarbage) {
            m4gc();
        }
        this.mValues[index] = value;
    }

    public int indexOfKey(long key) {
        if (this.mGarbage) {
            m4gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            m4gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public boolean containsKey(long key) {
        return indexOfKey(key) >= 0;
    }

    public boolean containsValue(E value) {
        return indexOfValue(value) >= 0;
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(long key, E value) {
        int i = this.mSize;
        if (i == 0 || key > this.mKeys[i - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                m4gc();
            }
            int pos = this.mSize;
            if (pos >= this.mKeys.length) {
                int n = ContainerHelpers.idealLongArraySize(pos + 1);
                long[] nkeys = new long[n];
                Object[] nvalues = new Object[n];
                long[] jArr = this.mKeys;
                System.arraycopy(jArr, 0, nkeys, 0, jArr.length);
                Object[] objArr = this.mValues;
                System.arraycopy(objArr, 0, nvalues, 0, objArr.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            this.mKeys[pos] = key;
            this.mValues[pos] = value;
            this.mSize = pos + 1;
            return;
        }
        put(key, value);
    }

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(keyAt(i));
            buffer.append('=');
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
}
