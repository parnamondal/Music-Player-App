package android.support.p000v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: android.support.v4.content.LocalBroadcastManager */
public final class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock = new Object();
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<>();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<>();
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap<>();

    /* renamed from: android.support.v4.content.LocalBroadcastManager$BroadcastRecord */
    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent _intent, ArrayList<ReceiverRecord> _receivers) {
            this.intent = _intent;
            this.receivers = _receivers;
        }
    }

    /* renamed from: android.support.v4.content.LocalBroadcastManager$ReceiverRecord */
    private static final class ReceiverRecord {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter _filter, BroadcastReceiver _receiver) {
            this.filter = _filter;
            this.receiver = _receiver;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(128);
            builder.append("Receiver{");
            builder.append(this.receiver);
            builder.append(" filter=");
            builder.append(this.filter);
            if (this.dead) {
                builder.append(" DEAD");
            }
            builder.append("}");
            return builder.toString();
        }
    }

    @NonNull
    public static LocalBroadcastManager getInstance(@NonNull Context context) {
        LocalBroadcastManager localBroadcastManager;
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            localBroadcastManager = mInstance;
        }
        return localBroadcastManager;
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message msg) {
                if (msg.what != 1) {
                    super.handleMessage(msg);
                } else {
                    LocalBroadcastManager.this.executePendingBroadcasts();
                }
            }
        };
    }

    public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        synchronized (this.mReceivers) {
            ReceiverRecord entry = new ReceiverRecord(filter, receiver);
            ArrayList arrayList = (ArrayList) this.mReceivers.get(receiver);
            if (arrayList == null) {
                arrayList = new ArrayList(1);
                this.mReceivers.put(receiver, arrayList);
            }
            arrayList.add(entry);
            for (int i = 0; i < filter.countActions(); i++) {
                String action = filter.getAction(i);
                ArrayList arrayList2 = (ArrayList) this.mActions.get(action);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList(1);
                    this.mActions.put(action, arrayList2);
                }
                arrayList2.add(entry);
            }
        }
    }

    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        synchronized (this.mReceivers) {
            ArrayList<ReceiverRecord> filters = (ArrayList) this.mReceivers.remove(receiver);
            if (filters != null) {
                for (int i = filters.size() - 1; i >= 0; i--) {
                    ReceiverRecord filter = (ReceiverRecord) filters.get(i);
                    filter.dead = true;
                    for (int j = 0; j < filter.filter.countActions(); j++) {
                        String action = filter.filter.getAction(j);
                        ArrayList<ReceiverRecord> receivers = (ArrayList) this.mActions.get(action);
                        if (receivers != null) {
                            for (int k = receivers.size() - 1; k >= 0; k--) {
                                ReceiverRecord rec = (ReceiverRecord) receivers.get(k);
                                if (rec.receiver == receiver) {
                                    rec.dead = true;
                                    receivers.remove(k);
                                }
                            }
                            if (receivers.size() <= 0) {
                                this.mActions.remove(action);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x01d0, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01db, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendBroadcast(@android.support.annotation.NonNull android.content.Intent r19) {
        /*
            r18 = this;
            r1 = r18
            r2 = r19
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord>> r3 = r1.mReceivers
            monitor-enter(r3)
            java.lang.String r5 = r19.getAction()     // Catch:{ all -> 0x01dc }
            android.content.Context r0 = r1.mAppContext     // Catch:{ all -> 0x01dc }
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ all -> 0x01dc }
            java.lang.String r0 = r2.resolveTypeIfNeeded(r0)     // Catch:{ all -> 0x01dc }
            android.net.Uri r8 = r19.getData()     // Catch:{ all -> 0x01dc }
            java.lang.String r4 = r19.getScheme()     // Catch:{ all -> 0x01dc }
            r11 = r4
            java.util.Set r9 = r19.getCategories()     // Catch:{ all -> 0x01dc }
            int r4 = r19.getFlags()     // Catch:{ all -> 0x01dc }
            r4 = r4 & 8
            r12 = 0
            if (r4 == 0) goto L_0x002e
            r4 = 1
            goto L_0x002f
        L_0x002e:
            r4 = 0
        L_0x002f:
            r14 = r4
            if (r14 == 0) goto L_0x0058
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01dc }
            r6.<init>()     // Catch:{ all -> 0x01dc }
            java.lang.String r7 = "Resolving type "
            r6.append(r7)     // Catch:{ all -> 0x01dc }
            r6.append(r0)     // Catch:{ all -> 0x01dc }
            java.lang.String r7 = " scheme "
            r6.append(r7)     // Catch:{ all -> 0x01dc }
            r6.append(r11)     // Catch:{ all -> 0x01dc }
            java.lang.String r7 = " of intent "
            r6.append(r7)     // Catch:{ all -> 0x01dc }
            r6.append(r2)     // Catch:{ all -> 0x01dc }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01dc }
            android.util.Log.v(r4, r6)     // Catch:{ all -> 0x01dc }
        L_0x0058:
            java.util.HashMap<java.lang.String, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord>> r4 = r1.mActions     // Catch:{ all -> 0x01dc }
            java.lang.String r6 = r19.getAction()     // Catch:{ all -> 0x01dc }
            java.lang.Object r4 = r4.get(r6)     // Catch:{ all -> 0x01dc }
            java.util.ArrayList r4 = (java.util.ArrayList) r4     // Catch:{ all -> 0x01dc }
            r15 = r4
            if (r15 == 0) goto L_0x01d3
            if (r14 == 0) goto L_0x007f
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01dc }
            r6.<init>()     // Catch:{ all -> 0x01dc }
            java.lang.String r7 = "Action list: "
            r6.append(r7)     // Catch:{ all -> 0x01dc }
            r6.append(r15)     // Catch:{ all -> 0x01dc }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01dc }
            android.util.Log.v(r4, r6)     // Catch:{ all -> 0x01dc }
        L_0x007f:
            r4 = 0
            r6 = r12
            r7 = r4
            r10 = r6
        L_0x0084:
            int r4 = r15.size()     // Catch:{ all -> 0x01dc }
            if (r10 >= r4) goto L_0x0182
            java.lang.Object r4 = r15.get(r10)     // Catch:{ all -> 0x01dc }
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r4 = (android.support.p000v4.content.LocalBroadcastManager.ReceiverRecord) r4     // Catch:{ all -> 0x01dc }
            r6 = r4
            if (r14 == 0) goto L_0x00ba
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x01dc }
            r12.<init>()     // Catch:{ all -> 0x01dc }
            java.lang.String r13 = "Matching against filter "
            r12.append(r13)     // Catch:{ all -> 0x01dc }
            android.content.IntentFilter r13 = r6.filter     // Catch:{ all -> 0x01dc }
            r12.append(r13)     // Catch:{ all -> 0x01dc }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x01dc }
            android.util.Log.v(r4, r12)     // Catch:{ all -> 0x01dc }
        L_0x00ba:
            boolean r4 = r6.broadcasting     // Catch:{ all -> 0x01dc }
            if (r4 == 0) goto L_0x00de
            if (r14 == 0) goto L_0x00d6
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.String r12 = "  Filter's target already added"
            android.util.Log.v(r4, r12)     // Catch:{ all -> 0x01dc }
            r16 = r0
            r0 = r7
            r17 = r10
            goto L_0x0179
        L_0x00d6:
            r16 = r0
            r0 = r7
            r17 = r10
            goto L_0x0179
        L_0x00de:
            android.content.IntentFilter r4 = r6.filter     // Catch:{ all -> 0x01dc }
            java.lang.String r12 = "LocalBroadcastManager"
            r13 = r6
            r6 = r0
            r16 = r0
            r0 = r7
            r7 = r11
            r17 = r10
            r10 = r12
            int r4 = r4.match(r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x01dc }
            if (r4 < 0) goto L_0x0137
            if (r14 == 0) goto L_0x011c
            java.lang.String r6 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x01dc }
            r7.<init>()     // Catch:{ all -> 0x01dc }
            java.lang.String r10 = "  Filter matched!  match=0x"
            r7.append(r10)     // Catch:{ all -> 0x01dc }
            java.lang.String r10 = java.lang.Integer.toHexString(r4)     // Catch:{ all -> 0x01dc }
            r7.append(r10)     // Catch:{ all -> 0x01dc }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x01dc }
            android.util.Log.v(r6, r7)     // Catch:{ all -> 0x01dc }
        L_0x011c:
            if (r0 != 0) goto L_0x012b
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ all -> 0x01dc }
            r6.<init>()     // Catch:{ all -> 0x01dc }
            r7 = r6
            r0 = r7
            goto L_0x012c
        L_0x012b:
        L_0x012c:
            r0.add(r13)     // Catch:{ all -> 0x01dc }
            r6 = 1
            r13.broadcasting = r6     // Catch:{ all -> 0x01dc }
            r7 = r0
            goto L_0x017a
        L_0x0137:
            if (r14 == 0) goto L_0x0178
            switch(r4) {
                case -4: goto L_0x0151;
                case -3: goto L_0x014c;
                case -2: goto L_0x0147;
                case -1: goto L_0x0142;
                default: goto L_0x013e;
            }     // Catch:{ all -> 0x01dc }
        L_0x013e:
            java.lang.String r6 = "unknown reason"
            goto L_0x0156
        L_0x0142:
            java.lang.String r6 = "type"
            goto L_0x0158
        L_0x0147:
            java.lang.String r6 = "data"
            goto L_0x0158
        L_0x014c:
            java.lang.String r6 = "action"
            goto L_0x0158
        L_0x0151:
            java.lang.String r6 = "category"
            goto L_0x0158
        L_0x0156:
        L_0x0158:
            java.lang.String r7 = "LocalBroadcastManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x01dc }
            r10.<init>()     // Catch:{ all -> 0x01dc }
            java.lang.String r12 = "  Filter did not match: "
            r10.append(r12)     // Catch:{ all -> 0x01dc }
            r10.append(r6)     // Catch:{ all -> 0x01dc }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x01dc }
            android.util.Log.v(r7, r10)     // Catch:{ all -> 0x01dc }
            goto L_0x0179
        L_0x0178:
        L_0x0179:
            r7 = r0
        L_0x017a:
            int r10 = r17 + 1
            r0 = r16
            r12 = 0
            goto L_0x0084
        L_0x0182:
            r16 = r0
            r0 = r7
            r17 = r10
            if (r0 == 0) goto L_0x01d1
            r4 = 0
            r6 = r4
        L_0x018f:
            int r4 = r0.size()     // Catch:{ all -> 0x01dc }
            if (r6 >= r4) goto L_0x01a7
            java.lang.Object r4 = r0.get(r6)     // Catch:{ all -> 0x01dc }
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r4 = (android.support.p000v4.content.LocalBroadcastManager.ReceiverRecord) r4     // Catch:{ all -> 0x01dc }
            r7 = 0
            r4.broadcasting = r7     // Catch:{ all -> 0x01dc }
            int r6 = r6 + 1
            goto L_0x018f
        L_0x01a7:
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r4 = r1.mPendingBroadcasts     // Catch:{ all -> 0x01dc }
            android.support.v4.content.LocalBroadcastManager$BroadcastRecord r6 = new android.support.v4.content.LocalBroadcastManager$BroadcastRecord     // Catch:{ all -> 0x01dc }
            r6.<init>(r2, r0)     // Catch:{ all -> 0x01dc }
            r4.add(r6)     // Catch:{ all -> 0x01dc }
            android.os.Handler r4 = r1.mHandler     // Catch:{ all -> 0x01dc }
            r6 = 1
            boolean r4 = r4.hasMessages(r6)     // Catch:{ all -> 0x01dc }
            if (r4 != 0) goto L_0x01cb
            android.os.Handler r4 = r1.mHandler     // Catch:{ all -> 0x01dc }
            r4.sendEmptyMessage(r6)     // Catch:{ all -> 0x01dc }
            goto L_0x01cc
        L_0x01cb:
        L_0x01cc:
            monitor-exit(r3)     // Catch:{ all -> 0x01dc }
            r3 = 1
            return r3
        L_0x01d1:
            goto L_0x01d6
        L_0x01d3:
            r16 = r0
        L_0x01d6:
            monitor-exit(r3)     // Catch:{ all -> 0x01dc }
            r0 = 0
            return r0
        L_0x01dc:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x01dc }
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.content.LocalBroadcastManager.sendBroadcast(android.content.Intent):boolean");
    }

    public void sendBroadcastSync(@NonNull Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        if (r1 >= r0.length) goto L_0x0001;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        r2 = r0[r1];
        r3 = r2.receivers.size();
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        if (r4 >= r3) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002a, code lost:
        r5 = (android.support.p000v4.content.LocalBroadcastManager.ReceiverRecord) r2.receivers.get(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0034, code lost:
        if (r5.dead != false) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        r5.receiver.onReceive(r9.mAppContext, r2.intent);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0044, code lost:
        r1 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executePendingBroadcasts() {
        /*
            r9 = this;
            r0 = 0
        L_0x0001:
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord>> r1 = r9.mReceivers
            monitor-enter(r1)
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r2 = r9.mPendingBroadcasts     // Catch:{ all -> 0x0048 }
            int r2 = r2.size()     // Catch:{ all -> 0x0048 }
            if (r2 > 0) goto L_0x000e
            monitor-exit(r1)     // Catch:{ all -> 0x0048 }
            return
        L_0x000e:
            android.support.v4.content.LocalBroadcastManager$BroadcastRecord[] r0 = new android.support.p000v4.content.LocalBroadcastManager.BroadcastRecord[r2]     // Catch:{ all -> 0x0048 }
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r3 = r9.mPendingBroadcasts     // Catch:{ all -> 0x0048 }
            r3.toArray(r0)     // Catch:{ all -> 0x0048 }
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r3 = r9.mPendingBroadcasts     // Catch:{ all -> 0x0048 }
            r3.clear()     // Catch:{ all -> 0x0048 }
            monitor-exit(r1)     // Catch:{ all -> 0x0048 }
            r1 = 0
        L_0x001c:
            int r2 = r0.length
            if (r1 >= r2) goto L_0x0047
            r2 = r0[r1]
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord> r3 = r2.receivers
            int r3 = r3.size()
            r4 = 0
        L_0x0028:
            if (r4 >= r3) goto L_0x0044
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord> r5 = r2.receivers
            java.lang.Object r5 = r5.get(r4)
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r5 = (android.support.p000v4.content.LocalBroadcastManager.ReceiverRecord) r5
            boolean r6 = r5.dead
            if (r6 != 0) goto L_0x0040
            android.content.BroadcastReceiver r6 = r5.receiver
            android.content.Context r7 = r9.mAppContext
            android.content.Intent r8 = r2.intent
            r6.onReceive(r7, r8)
            goto L_0x0041
        L_0x0040:
        L_0x0041:
            int r4 = r4 + 1
            goto L_0x0028
        L_0x0044:
            int r1 = r1 + 1
            goto L_0x001c
        L_0x0047:
            goto L_0x0001
        L_0x0048:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0048 }
            throw r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.content.LocalBroadcastManager.executePendingBroadcasts():void");
    }
}
