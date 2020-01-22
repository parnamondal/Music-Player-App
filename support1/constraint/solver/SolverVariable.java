package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 7;
    public static final int STRENGTH_BARRIER = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueConstantId = 1;
    private static int uniqueErrorId = 1;
    private static int uniqueId = 1;
    private static int uniqueSlackId = 1;
    private static int uniqueUnrestrictedId = 1;
    public float computedValue;
    int definitionId = -1;

    /* renamed from: id */
    public int f3id = -1;
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[7];
    public int usageInRowCount = 0;

    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    static void increaseErrorId() {
        uniqueErrorId++;
    }

    private static String getUniqueName(Type type, String prefix) {
        if (prefix != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(uniqueErrorId);
            return sb.toString();
        }
        switch (type) {
            case UNRESTRICTED:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("U");
                int i = uniqueUnrestrictedId + 1;
                uniqueUnrestrictedId = i;
                sb2.append(i);
                return sb2.toString();
            case CONSTANT:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("C");
                int i2 = uniqueConstantId + 1;
                uniqueConstantId = i2;
                sb3.append(i2);
                return sb3.toString();
            case SLACK:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("S");
                int i3 = uniqueSlackId + 1;
                uniqueSlackId = i3;
                sb4.append(i3);
                return sb4.toString();
            case ERROR:
                StringBuilder sb5 = new StringBuilder();
                sb5.append("e");
                int i4 = uniqueErrorId + 1;
                uniqueErrorId = i4;
                sb5.append(i4);
                return sb5.toString();
            case UNKNOWN:
                StringBuilder sb6 = new StringBuilder();
                sb6.append("V");
                int i5 = uniqueId + 1;
                uniqueId = i5;
                sb6.append(i5);
                return sb6.toString();
            default:
                throw new AssertionError(type.name());
        }
    }

    public SolverVariable(String name, Type type) {
        this.mName = name;
        this.mType = type;
    }

    public SolverVariable(Type type, String prefix) {
        this.mType = type;
    }

    /* access modifiers changed from: 0000 */
    public void clearStrengths() {
        for (int i = 0; i < 7; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    /* access modifiers changed from: 0000 */
    public String strengthsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String representation = sb.toString();
        boolean negative = false;
        boolean empty = true;
        for (int j = 0; j < this.strengthVector.length; j++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(representation);
            sb2.append(this.strengthVector[j]);
            String representation2 = sb2.toString();
            float[] fArr = this.strengthVector;
            if (fArr[j] > 0.0f) {
                negative = false;
            } else if (fArr[j] < 0.0f) {
                negative = true;
            }
            if (this.strengthVector[j] != 0.0f) {
                empty = false;
            }
            if (j < this.strengthVector.length - 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(representation2);
                sb3.append(", ");
                representation = sb3.toString();
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(representation2);
                sb4.append("] ");
                representation = sb4.toString();
            }
        }
        if (negative) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(representation);
            sb5.append(" (-)");
            representation = sb5.toString();
        }
        if (!empty) {
            return representation;
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append(representation);
        sb6.append(" (*)");
        return sb6.toString();
    }

    public final void addToRow(ArrayRow row) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i >= i2) {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = row;
                this.mClientEquationsCount = i3 + 1;
                return;
            } else if (this.mClientEquations[i] != row) {
                i++;
            } else {
                return;
            }
        }
    }

    public final void removeFromRow(ArrayRow row) {
        int count = this.mClientEquationsCount;
        for (int i = 0; i < count; i++) {
            if (this.mClientEquations[i] == row) {
                for (int j = 0; j < (count - i) - 1; j++) {
                    ArrayRow[] arrayRowArr = this.mClientEquations;
                    arrayRowArr[i + j] = arrayRowArr[i + j + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public final void updateReferencesWithNewDefinition(ArrayRow definition) {
        int count = this.mClientEquationsCount;
        for (int i = 0; i < count; i++) {
            this.mClientEquations[i].variables.updateFromRow(this.mClientEquations[i], definition, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.f3id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setType(Type type, String prefix) {
        this.mType = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
}
