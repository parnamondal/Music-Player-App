package android.support.p003v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlSerializer;

/* renamed from: android.support.v7.widget.ActivityChooserModel */
class ActivityChooserModel extends DataSetObservable {
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry = new HashMap();
    private static final Object sRegistryLock = new Object();
    private final List<ActivityResolveInfo> mActivities = new ArrayList();
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter = new DefaultSorter();
    boolean mCanReadHistoricalData = true;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords = new ArrayList();
    private boolean mHistoricalRecordsChanged = true;
    final String mHistoryFileName;
    private int mHistoryMaxSize = 50;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private boolean mReadShareHistoryCalled = false;
    private boolean mReloadActivities = false;

    /* renamed from: android.support.v7.widget.ActivityChooserModel$ActivityChooserModelClient */
    public interface ActivityChooserModelClient {
        void setActivityChooserModel(ActivityChooserModel activityChooserModel);
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$ActivityResolveInfo */
    public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo2) {
            this.resolveInfo = resolveInfo2;
        }

        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo) obj).weight)) {
                return false;
            }
            return true;
        }

        public int compareTo(ActivityResolveInfo another) {
            return Float.floatToIntBits(another.weight) - Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("resolveInfo:");
            builder.append(this.resolveInfo.toString());
            builder.append("; weight:");
            builder.append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$ActivitySorter */
    public interface ActivitySorter {
        void sort(Intent intent, List<ActivityResolveInfo> list, List<HistoricalRecord> list2);
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$DefaultSorter */
    private static final class DefaultSorter implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = new HashMap();

        DefaultSorter() {
        }

        public void sort(Intent intent, List<ActivityResolveInfo> activities, List<HistoricalRecord> historicalRecords) {
            Map<ComponentName, ActivityResolveInfo> componentNameToActivityMap = this.mPackageNameToActivityMap;
            componentNameToActivityMap.clear();
            int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                ActivityResolveInfo activity = (ActivityResolveInfo) activities.get(i);
                activity.weight = 0.0f;
                componentNameToActivityMap.put(new ComponentName(activity.resolveInfo.activityInfo.packageName, activity.resolveInfo.activityInfo.name), activity);
            }
            int lastShareIndex = historicalRecords.size() - 1;
            float nextRecordWeight = ActivityChooserModel.DEFAULT_HISTORICAL_RECORD_WEIGHT;
            for (int i2 = lastShareIndex; i2 >= 0; i2--) {
                HistoricalRecord historicalRecord = (HistoricalRecord) historicalRecords.get(i2);
                ActivityResolveInfo activity2 = (ActivityResolveInfo) componentNameToActivityMap.get(historicalRecord.activity);
                if (activity2 != null) {
                    activity2.weight += historicalRecord.weight * nextRecordWeight;
                    nextRecordWeight *= WEIGHT_DECAY_COEFFICIENT;
                }
            }
            Collections.sort(activities);
        }
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$HistoricalRecord */
    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(String activityName, long time2, float weight2) {
            this(ComponentName.unflattenFromString(activityName), time2, weight2);
        }

        public HistoricalRecord(ComponentName activityName, long time2, float weight2) {
            this.activity = activityName;
            this.time = time2;
            this.weight = weight2;
        }

        public int hashCode() {
            int i = 1 * 31;
            ComponentName componentName = this.activity;
            int result = (i + (componentName == null ? 0 : componentName.hashCode())) * 31;
            long j = this.time;
            return ((result + ((int) (j ^ (j >>> 32)))) * 31) + Float.floatToIntBits(this.weight);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalRecord other = (HistoricalRecord) obj;
            ComponentName componentName = this.activity;
            if (componentName == null) {
                if (other.activity != null) {
                    return false;
                }
            } else if (!componentName.equals(other.activity)) {
                return false;
            }
            if (this.time == other.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(other.weight)) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("; activity:");
            builder.append(this.activity);
            builder.append("; time:");
            builder.append(this.time);
            builder.append("; weight:");
            builder.append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$OnChooseActivityListener */
    public interface OnChooseActivityListener {
        boolean onChooseActivity(ActivityChooserModel activityChooserModel, Intent intent);
    }

    /* renamed from: android.support.v7.widget.ActivityChooserModel$PersistHistoryAsyncTask */
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void> {
        PersistHistoryAsyncTask() {
        }

        public Void doInBackground(Object... args) {
            List<HistoricalRecord> historicalRecords = args[0];
            String historyFileName = args[1];
            try {
                FileOutputStream fos = ActivityChooserModel.this.mContext.openFileOutput(historyFileName, 0);
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    serializer.setOutput(fos, null);
                    serializer.startDocument("UTF-8", Boolean.valueOf(true));
                    serializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                    int recordCount = historicalRecords.size();
                    for (int i = 0; i < recordCount; i++) {
                        HistoricalRecord record = (HistoricalRecord) historicalRecords.remove(0);
                        serializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                        serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_ACTIVITY, record.activity.flattenToString());
                        serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_TIME, String.valueOf(record.time));
                        serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_WEIGHT, String.valueOf(record.weight));
                        serializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                    }
                    serializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                    serializer.endDocument();
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IllegalArgumentException iae) {
                    String str = ActivityChooserModel.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error writing historical record file: ");
                    sb.append(ActivityChooserModel.this.mHistoryFileName);
                    Log.e(str, sb.toString(), iae);
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IllegalStateException ise) {
                    String str2 = ActivityChooserModel.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error writing historical record file: ");
                    sb2.append(ActivityChooserModel.this.mHistoryFileName);
                    Log.e(str2, sb2.toString(), ise);
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException ioe) {
                    String str3 = ActivityChooserModel.LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Error writing historical record file: ");
                    sb3.append(ActivityChooserModel.this.mHistoryFileName);
                    Log.e(str3, sb3.toString(), ioe);
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Throwable th) {
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e2) {
                        }
                    }
                    throw th;
                }
                return null;
            } catch (FileNotFoundException fnfe) {
                String str4 = ActivityChooserModel.LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Error writing historical record file: ");
                sb4.append(historyFileName);
                Log.e(str4, sb4.toString(), fnfe);
                return null;
            }
        }
    }

    public static ActivityChooserModel get(Context context, String historyFileName) {
        ActivityChooserModel dataModel;
        synchronized (sRegistryLock) {
            dataModel = (ActivityChooserModel) sDataModelRegistry.get(historyFileName);
            if (dataModel == null) {
                dataModel = new ActivityChooserModel(context, historyFileName);
                sDataModelRegistry.put(historyFileName, dataModel);
            }
        }
        return dataModel;
    }

    private ActivityChooserModel(Context context, String historyFileName) {
        this.mContext = context.getApplicationContext();
        if (TextUtils.isEmpty(historyFileName) || historyFileName.endsWith(HISTORY_FILE_EXTENSION)) {
            this.mHistoryFileName = historyFileName;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(historyFileName);
        sb.append(HISTORY_FILE_EXTENSION);
        this.mHistoryFileName = sb.toString();
    }

    public void setIntent(Intent intent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent != intent) {
                this.mIntent = intent;
                this.mReloadActivities = true;
                ensureConsistentState();
            }
        }
    }

    public Intent getIntent() {
        Intent intent;
        synchronized (this.mInstanceLock) {
            intent = this.mIntent;
        }
        return intent;
    }

    public int getActivityCount() {
        int size;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            size = this.mActivities.size();
        }
        return size;
    }

    public ResolveInfo getActivity(int index) {
        ResolveInfo resolveInfo;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            resolveInfo = ((ActivityResolveInfo) this.mActivities.get(index)).resolveInfo;
        }
        return resolveInfo;
    }

    public int getActivityIndex(ResolveInfo activity) {
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            List<ActivityResolveInfo> activities = this.mActivities;
            int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                if (((ActivityResolveInfo) activities.get(i)).resolveInfo == activity) {
                    return i;
                }
            }
            return -1;
        }
    }

    public Intent chooseActivity(int index) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            ensureConsistentState();
            ActivityResolveInfo chosenActivity = (ActivityResolveInfo) this.mActivities.get(index);
            ComponentName chosenName = new ComponentName(chosenActivity.resolveInfo.activityInfo.packageName, chosenActivity.resolveInfo.activityInfo.name);
            Intent choiceIntent = new Intent(this.mIntent);
            choiceIntent.setComponent(chosenName);
            if (this.mActivityChoserModelPolicy != null) {
                if (this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(choiceIntent))) {
                    return null;
                }
            }
            addHistoricalRecord(new HistoricalRecord(chosenName, System.currentTimeMillis(), (float) DEFAULT_HISTORICAL_RECORD_WEIGHT));
            return choiceIntent;
        }
    }

    public void setOnChooseActivityListener(OnChooseActivityListener listener) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = listener;
        }
    }

    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            if (this.mActivities.isEmpty()) {
                return null;
            }
            ResolveInfo resolveInfo = ((ActivityResolveInfo) this.mActivities.get(0)).resolveInfo;
            return resolveInfo;
        }
    }

    public void setDefaultActivity(int index) {
        float weight;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            ActivityResolveInfo newDefaultActivity = (ActivityResolveInfo) this.mActivities.get(index);
            ActivityResolveInfo oldDefaultActivity = (ActivityResolveInfo) this.mActivities.get(0);
            if (oldDefaultActivity != null) {
                weight = (oldDefaultActivity.weight - newDefaultActivity.weight) + 5.0f;
            } else {
                weight = DEFAULT_HISTORICAL_RECORD_WEIGHT;
            }
            addHistoricalRecord(new HistoricalRecord(new ComponentName(newDefaultActivity.resolveInfo.activityInfo.packageName, newDefaultActivity.resolveInfo.activityInfo.name), System.currentTimeMillis(), weight));
        }
    }

    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        } else if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty(this.mHistoryFileName)) {
                new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setActivitySorter(android.support.p003v7.widget.ActivityChooserModel.ActivitySorter r3) {
        /*
            r2 = this;
            java.lang.Object r0 = r2.mInstanceLock
            monitor-enter(r0)
            android.support.v7.widget.ActivityChooserModel$ActivitySorter r1 = r2.mActivitySorter     // Catch:{ all -> 0x0018 }
            if (r1 != r3) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            return
        L_0x0009:
            r2.mActivitySorter = r3     // Catch:{ all -> 0x0018 }
            boolean r1 = r2.sortActivitiesIfNeeded()     // Catch:{ all -> 0x0018 }
            if (r1 == 0) goto L_0x0015
            r2.notifyChanged()     // Catch:{ all -> 0x0018 }
            goto L_0x0016
        L_0x0015:
        L_0x0016:
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            return
        L_0x0018:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.widget.ActivityChooserModel.setActivitySorter(android.support.v7.widget.ActivityChooserModel$ActivitySorter):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setHistoryMaxSize(int r3) {
        /*
            r2 = this;
            java.lang.Object r0 = r2.mInstanceLock
            monitor-enter(r0)
            int r1 = r2.mHistoryMaxSize     // Catch:{ all -> 0x001b }
            if (r1 != r3) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x001b }
            return
        L_0x0009:
            r2.mHistoryMaxSize = r3     // Catch:{ all -> 0x001b }
            r2.pruneExcessiveHistoricalRecordsIfNeeded()     // Catch:{ all -> 0x001b }
            boolean r1 = r2.sortActivitiesIfNeeded()     // Catch:{ all -> 0x001b }
            if (r1 == 0) goto L_0x0018
            r2.notifyChanged()     // Catch:{ all -> 0x001b }
            goto L_0x0019
        L_0x0018:
        L_0x0019:
            monitor-exit(r0)     // Catch:{ all -> 0x001b }
            return
        L_0x001b:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001b }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.widget.ActivityChooserModel.setHistoryMaxSize(int):void");
    }

    public int getHistoryMaxSize() {
        int i;
        synchronized (this.mInstanceLock) {
            i = this.mHistoryMaxSize;
        }
        return i;
    }

    public int getHistorySize() {
        int size;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            size = this.mHistoricalRecords.size();
        }
        return size;
    }

    private void ensureConsistentState() {
        boolean stateChanged = loadActivitiesIfNeeded() | readHistoricalDataIfNeeded();
        pruneExcessiveHistoricalRecordsIfNeeded();
        if (stateChanged) {
            sortActivitiesIfNeeded();
            notifyChanged();
        }
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter == null || this.mIntent == null || this.mActivities.isEmpty() || this.mHistoricalRecords.isEmpty()) {
            return false;
        }
        this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
        return true;
    }

    private boolean loadActivitiesIfNeeded() {
        if (!this.mReloadActivities || this.mIntent == null) {
            return false;
        }
        this.mReloadActivities = false;
        this.mActivities.clear();
        List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
        int resolveInfoCount = resolveInfos.size();
        for (int i = 0; i < resolveInfoCount; i++) {
            this.mActivities.add(new ActivityResolveInfo((ResolveInfo) resolveInfos.get(i)));
        }
        return true;
    }

    private boolean readHistoricalDataIfNeeded() {
        if (!this.mCanReadHistoricalData || !this.mHistoricalRecordsChanged || TextUtils.isEmpty(this.mHistoryFileName)) {
            return false;
        }
        this.mCanReadHistoricalData = false;
        this.mReadShareHistoryCalled = true;
        readHistoricalDataImpl();
        return true;
    }

    private boolean addHistoricalRecord(HistoricalRecord historicalRecord) {
        boolean added = this.mHistoricalRecords.add(historicalRecord);
        if (added) {
            this.mHistoricalRecordsChanged = true;
            pruneExcessiveHistoricalRecordsIfNeeded();
            persistHistoricalDataIfNeeded();
            sortActivitiesIfNeeded();
            notifyChanged();
        }
        return added;
    }

    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int pruneCount = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (pruneCount > 0) {
            this.mHistoricalRecordsChanged = true;
            for (int i = 0; i < pruneCount; i++) {
                HistoricalRecord historicalRecord = (HistoricalRecord) this.mHistoricalRecords.remove(0);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003b, code lost:
        if (r0 == null) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r0.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readHistoricalDataImpl() {
        /*
            r11 = this;
            r0 = 0
            android.content.Context r1 = r11.mContext     // Catch:{ FileNotFoundException -> 0x00da }
            java.lang.String r2 = r11.mHistoryFileName     // Catch:{ FileNotFoundException -> 0x00da }
            java.io.FileInputStream r1 = r1.openFileInput(r2)     // Catch:{ FileNotFoundException -> 0x00da }
            r0 = r1
            org.xmlpull.v1.XmlPullParser r1 = android.util.Xml.newPullParser()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r2 = "UTF-8"
            r1.setInput(r0, r2)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r2 = 0
        L_0x0015:
            r3 = 1
            if (r2 == r3) goto L_0x0021
            r4 = 2
            if (r2 == r4) goto L_0x0021
            int r3 = r1.next()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r2 = r3
            goto L_0x0015
        L_0x0021:
            java.lang.String r4 = "historical-records"
            java.lang.String r5 = r1.getName()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            boolean r4 = r4.equals(r5)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            if (r4 == 0) goto L_0x0082
            java.util.List<android.support.v7.widget.ActivityChooserModel$HistoricalRecord> r4 = r11.mHistoricalRecords     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r4.clear()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
        L_0x0033:
            int r5 = r1.next()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r2 = r5
            if (r2 != r3) goto L_0x0042
            if (r0 == 0) goto L_0x00ce
            r0.close()     // Catch:{ IOException -> 0x00cc }
            goto L_0x00cb
        L_0x0042:
            r5 = 3
            if (r2 == r5) goto L_0x0081
            r5 = 4
            if (r2 != r5) goto L_0x0049
            goto L_0x0033
        L_0x0049:
            java.lang.String r5 = r1.getName()     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r6 = "historical-record"
            boolean r6 = r6.equals(r5)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            if (r6 == 0) goto L_0x0079
            java.lang.String r6 = "activity"
            r7 = 0
            java.lang.String r6 = r1.getAttributeValue(r7, r6)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r8 = "time"
            java.lang.String r8 = r1.getAttributeValue(r7, r8)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            long r8 = java.lang.Long.parseLong(r8)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r10 = "weight"
            java.lang.String r7 = r1.getAttributeValue(r7, r10)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            float r7 = java.lang.Float.parseFloat(r7)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            android.support.v7.widget.ActivityChooserModel$HistoricalRecord r10 = new android.support.v7.widget.ActivityChooserModel$HistoricalRecord     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r10.<init>(r6, r8, r7)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            r4.add(r10)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            goto L_0x0033
        L_0x0079:
            org.xmlpull.v1.XmlPullParserException r3 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r6 = "Share records file not well-formed."
            r3.<init>(r6)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            throw r3     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
        L_0x0081:
            goto L_0x0033
        L_0x0082:
            org.xmlpull.v1.XmlPullParserException r3 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            java.lang.String r4 = "Share records file does not start with historical-records tag."
            r3.<init>(r4)     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
            throw r3     // Catch:{ XmlPullParserException -> 0x00ac, IOException -> 0x008c }
        L_0x008a:
            r1 = move-exception
            goto L_0x00d0
        L_0x008c:
            r1 = move-exception
            java.lang.String r2 = LOG_TAG     // Catch:{ all -> 0x008a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x008a }
            r3.<init>()     // Catch:{ all -> 0x008a }
            java.lang.String r4 = "Error reading historical recrod file: "
            r3.append(r4)     // Catch:{ all -> 0x008a }
            java.lang.String r4 = r11.mHistoryFileName     // Catch:{ all -> 0x008a }
            r3.append(r4)     // Catch:{ all -> 0x008a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x008a }
            android.util.Log.e(r2, r3, r1)     // Catch:{ all -> 0x008a }
            if (r0 == 0) goto L_0x00ce
            r0.close()     // Catch:{ IOException -> 0x00cc }
            goto L_0x00cb
        L_0x00ac:
            r1 = move-exception
            java.lang.String r2 = LOG_TAG     // Catch:{ all -> 0x008a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x008a }
            r3.<init>()     // Catch:{ all -> 0x008a }
            java.lang.String r4 = "Error reading historical recrod file: "
            r3.append(r4)     // Catch:{ all -> 0x008a }
            java.lang.String r4 = r11.mHistoryFileName     // Catch:{ all -> 0x008a }
            r3.append(r4)     // Catch:{ all -> 0x008a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x008a }
            android.util.Log.e(r2, r3, r1)     // Catch:{ all -> 0x008a }
            if (r0 == 0) goto L_0x00ce
            r0.close()     // Catch:{ IOException -> 0x00cc }
        L_0x00cb:
            goto L_0x00cf
        L_0x00cc:
            r1 = move-exception
            goto L_0x00cb
        L_0x00ce:
        L_0x00cf:
            return
        L_0x00d0:
            if (r0 == 0) goto L_0x00d8
            r0.close()     // Catch:{ IOException -> 0x00d6 }
            goto L_0x00d9
        L_0x00d6:
            r2 = move-exception
            goto L_0x00d9
        L_0x00d8:
        L_0x00d9:
            throw r1
        L_0x00da:
            r1 = move-exception
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.widget.ActivityChooserModel.readHistoricalDataImpl():void");
    }
}
