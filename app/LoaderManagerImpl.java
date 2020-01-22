package android.support.p000v4.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProvider.Factory;
import android.arch.lifecycle.ViewModelStore;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.LoaderManager.LoaderCallbacks;
import android.support.p000v4.content.Loader;
import android.support.p000v4.content.Loader.OnLoadCompleteListener;
import android.support.p000v4.util.DebugUtils;
import android.support.p000v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

/* renamed from: android.support.v4.app.LoaderManagerImpl */
class LoaderManagerImpl extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    @NonNull
    private final LifecycleOwner mLifecycleOwner;
    @NonNull
    private final LoaderViewModel mLoaderViewModel;

    /* renamed from: android.support.v4.app.LoaderManagerImpl$LoaderInfo */
    public static class LoaderInfo<D> extends MutableLiveData<D> implements OnLoadCompleteListener<D> {
        @Nullable
        private final Bundle mArgs;
        private final int mId;
        private LifecycleOwner mLifecycleOwner;
        @NonNull
        private final Loader<D> mLoader;
        private LoaderObserver<D> mObserver;
        private Loader<D> mPriorLoader;

        LoaderInfo(int id, @Nullable Bundle args, @NonNull Loader<D> loader, @Nullable Loader<D> priorLoader) {
            this.mId = id;
            this.mArgs = args;
            this.mLoader = loader;
            this.mPriorLoader = priorLoader;
            this.mLoader.registerListener(id, this);
        }

        /* access modifiers changed from: 0000 */
        @NonNull
        public Loader<D> getLoader() {
            return this.mLoader;
        }

        /* access modifiers changed from: protected */
        public void onActive() {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("  Starting: ");
                sb.append(this);
                Log.v(str, sb.toString());
            }
            this.mLoader.startLoading();
        }

        /* access modifiers changed from: protected */
        public void onInactive() {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("  Stopping: ");
                sb.append(this);
                Log.v(str, sb.toString());
            }
            this.mLoader.stopLoading();
        }

        /* access modifiers changed from: 0000 */
        @MainThread
        @NonNull
        public Loader<D> setCallback(@NonNull LifecycleOwner owner, @NonNull LoaderCallbacks<D> callback) {
            LoaderObserver<D> observer = new LoaderObserver<>(this.mLoader, callback);
            observe(owner, observer);
            LoaderObserver<D> loaderObserver = this.mObserver;
            if (loaderObserver != null) {
                removeObserver(loaderObserver);
            }
            this.mLifecycleOwner = owner;
            this.mObserver = observer;
            return this.mLoader;
        }

        /* access modifiers changed from: 0000 */
        public void markForRedelivery() {
            LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
            LoaderObserver<D> observer = this.mObserver;
            if (lifecycleOwner != null && observer != null) {
                super.removeObserver(observer);
                observe(lifecycleOwner, observer);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isCallbackWaitingForData() {
            boolean z = false;
            if (!hasActiveObservers()) {
                return false;
            }
            LoaderObserver<D> loaderObserver = this.mObserver;
            if (loaderObserver != null && !loaderObserver.hasDeliveredData()) {
                z = true;
            }
            return z;
        }

        public void removeObserver(@NonNull Observer<? super D> observer) {
            super.removeObserver(observer);
            this.mLifecycleOwner = null;
            this.mObserver = null;
        }

        /* access modifiers changed from: 0000 */
        @MainThread
        public Loader<D> destroy(boolean reset) {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("  Destroying: ");
                sb.append(this);
                Log.v(str, sb.toString());
            }
            this.mLoader.cancelLoad();
            this.mLoader.abandon();
            LoaderObserver<D> observer = this.mObserver;
            if (observer != null) {
                removeObserver(observer);
                if (reset) {
                    observer.reset();
                }
            }
            this.mLoader.unregisterListener(this);
            if ((observer == null || observer.hasDeliveredData()) && !reset) {
                return this.mLoader;
            }
            this.mLoader.reset();
            return this.mPriorLoader;
        }

        public void onLoadComplete(@NonNull Loader<D> loader, @Nullable D data) {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onLoadComplete: ");
                sb.append(this);
                Log.v(str, sb.toString());
            }
            if (Looper.myLooper() == Looper.getMainLooper()) {
                setValue(data);
                return;
            }
            if (LoaderManagerImpl.DEBUG) {
                Log.w(LoaderManagerImpl.TAG, "onLoadComplete was incorrectly called on a background thread");
            }
            postValue(data);
        }

        public void setValue(D value) {
            super.setValue(value);
            Loader<D> loader = this.mPriorLoader;
            if (loader != null) {
                loader.reset();
                this.mPriorLoader = null;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.print(prefix);
            writer.print("mId=");
            writer.print(this.mId);
            writer.print(" mArgs=");
            writer.println(this.mArgs);
            writer.print(prefix);
            writer.print("mLoader=");
            writer.println(this.mLoader);
            Loader<D> loader = this.mLoader;
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append("  ");
            loader.dump(sb.toString(), fd, writer, args);
            if (this.mObserver != null) {
                writer.print(prefix);
                writer.print("mCallbacks=");
                writer.println(this.mObserver);
                LoaderObserver<D> loaderObserver = this.mObserver;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(prefix);
                sb2.append("  ");
                loaderObserver.dump(sb2.toString(), writer);
            }
            writer.print(prefix);
            writer.print("mData=");
            writer.println(getLoader().dataToString(getValue()));
            writer.print(prefix);
            writer.print("mStarted=");
            writer.println(hasActiveObservers());
        }
    }

    /* renamed from: android.support.v4.app.LoaderManagerImpl$LoaderObserver */
    static class LoaderObserver<D> implements Observer<D> {
        @NonNull
        private final LoaderCallbacks<D> mCallback;
        private boolean mDeliveredData = false;
        @NonNull
        private final Loader<D> mLoader;

        LoaderObserver(@NonNull Loader<D> loader, @NonNull LoaderCallbacks<D> callback) {
            this.mLoader = loader;
            this.mCallback = callback;
        }

        public void onChanged(@Nullable D data) {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("  onLoadFinished in ");
                sb.append(this.mLoader);
                sb.append(": ");
                sb.append(this.mLoader.dataToString(data));
                Log.v(str, sb.toString());
            }
            this.mCallback.onLoadFinished(this.mLoader, data);
            this.mDeliveredData = true;
        }

        /* access modifiers changed from: 0000 */
        public boolean hasDeliveredData() {
            return this.mDeliveredData;
        }

        /* access modifiers changed from: 0000 */
        @MainThread
        public void reset() {
            if (this.mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    String str = LoaderManagerImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("  Resetting: ");
                    sb.append(this.mLoader);
                    Log.v(str, sb.toString());
                }
                this.mCallback.onLoaderReset(this.mLoader);
            }
        }

        public String toString() {
            return this.mCallback.toString();
        }

        public void dump(String prefix, PrintWriter writer) {
            writer.print(prefix);
            writer.print("mDeliveredData=");
            writer.println(this.mDeliveredData);
        }
    }

    /* renamed from: android.support.v4.app.LoaderManagerImpl$LoaderViewModel */
    static class LoaderViewModel extends ViewModel {
        private static final Factory FACTORY = new Factory() {
            @NonNull
            public <T extends ViewModel> T create(@NonNull Class<T> cls) {
                return new LoaderViewModel();
            }
        };
        private boolean mCreatingLoader = false;
        private SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat<>();

        LoaderViewModel() {
        }

        @NonNull
        static LoaderViewModel getInstance(ViewModelStore viewModelStore) {
            return (LoaderViewModel) new ViewModelProvider(viewModelStore, FACTORY).get(LoaderViewModel.class);
        }

        /* access modifiers changed from: 0000 */
        public void startCreatingLoader() {
            this.mCreatingLoader = true;
        }

        /* access modifiers changed from: 0000 */
        public boolean isCreatingLoader() {
            return this.mCreatingLoader;
        }

        /* access modifiers changed from: 0000 */
        public void finishCreatingLoader() {
            this.mCreatingLoader = false;
        }

        /* access modifiers changed from: 0000 */
        public void putLoader(int id, @NonNull LoaderInfo info) {
            this.mLoaders.put(id, info);
        }

        /* access modifiers changed from: 0000 */
        public <D> LoaderInfo<D> getLoader(int id) {
            return (LoaderInfo) this.mLoaders.get(id);
        }

        /* access modifiers changed from: 0000 */
        public void removeLoader(int id) {
            this.mLoaders.remove(id);
        }

        /* access modifiers changed from: 0000 */
        public boolean hasRunningLoaders() {
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                if (((LoaderInfo) this.mLoaders.valueAt(index)).isCallbackWaitingForData()) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void markForRedelivery() {
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                ((LoaderInfo) this.mLoaders.valueAt(index)).markForRedelivery();
            }
        }

        /* access modifiers changed from: protected */
        public void onCleared() {
            super.onCleared();
            int size = this.mLoaders.size();
            for (int index = 0; index < size; index++) {
                ((LoaderInfo) this.mLoaders.valueAt(index)).destroy(true);
            }
            this.mLoaders.clear();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            if (this.mLoaders.size() > 0) {
                writer.print(prefix);
                writer.println("Loaders:");
                StringBuilder sb = new StringBuilder();
                sb.append(prefix);
                sb.append("    ");
                String innerPrefix = sb.toString();
                for (int i = 0; i < this.mLoaders.size(); i++) {
                    LoaderInfo info = (LoaderInfo) this.mLoaders.valueAt(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(this.mLoaders.keyAt(i));
                    writer.print(": ");
                    writer.println(info.toString());
                    info.dump(innerPrefix, fd, writer, args);
                }
            }
        }
    }

    LoaderManagerImpl(@NonNull LifecycleOwner lifecycleOwner, @NonNull ViewModelStore viewModelStore) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mLoaderViewModel = LoaderViewModel.getInstance(viewModelStore);
    }

    /* JADX INFO: finally extract failed */
    @MainThread
    @NonNull
    private <D> Loader<D> createAndInstallLoader(int id, @Nullable Bundle args, @NonNull LoaderCallbacks<D> callback, @Nullable Loader<D> priorLoader) {
        try {
            this.mLoaderViewModel.startCreatingLoader();
            Loader<D> loader = callback.onCreateLoader(id, args);
            if (loader != null) {
                if (loader.getClass().isMemberClass()) {
                    if (!Modifier.isStatic(loader.getClass().getModifiers())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                        sb.append(loader);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                LoaderInfo loaderInfo = new LoaderInfo(id, args, loader, priorLoader);
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("  Created new loader ");
                    sb2.append(loaderInfo);
                    Log.v(str, sb2.toString());
                }
                this.mLoaderViewModel.putLoader(id, loaderInfo);
                this.mLoaderViewModel.finishCreatingLoader();
                return loaderInfo.setCallback(this.mLifecycleOwner, callback);
            }
            throw new IllegalArgumentException("Object returned from onCreateLoader must not be null");
        } catch (Throwable th) {
            this.mLoaderViewModel.finishCreatingLoader();
            throw th;
        }
    }

    @MainThread
    @NonNull
    public <D> Loader<D> initLoader(int id, @Nullable Bundle args, @NonNull LoaderCallbacks<D> callback) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() == Looper.myLooper()) {
            LoaderInfo<D> info = this.mLoaderViewModel.getLoader(id);
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("initLoader in ");
                sb.append(this);
                sb.append(": args=");
                sb.append(args);
                Log.v(str, sb.toString());
            }
            if (info == null) {
                return createAndInstallLoader(id, args, callback, null);
            }
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("  Re-using existing loader ");
                sb2.append(info);
                Log.v(str2, sb2.toString());
            }
            return info.setCallback(this.mLifecycleOwner, callback);
        } else {
            throw new IllegalStateException("initLoader must be called on the main thread");
        }
    }

    @MainThread
    @NonNull
    public <D> Loader<D> restartLoader(int id, @Nullable Bundle args, @NonNull LoaderCallbacks<D> callback) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() == Looper.myLooper()) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("restartLoader in ");
                sb.append(this);
                sb.append(": args=");
                sb.append(args);
                Log.v(str, sb.toString());
            }
            LoaderInfo<D> info = this.mLoaderViewModel.getLoader(id);
            Loader<D> priorLoader = null;
            if (info != null) {
                priorLoader = info.destroy(false);
            }
            return createAndInstallLoader(id, args, callback, priorLoader);
        } else {
            throw new IllegalStateException("restartLoader must be called on the main thread");
        }
    }

    @MainThread
    public void destroyLoader(int id) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        } else if (Looper.getMainLooper() == Looper.myLooper()) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("destroyLoader in ");
                sb.append(this);
                sb.append(" of ");
                sb.append(id);
                Log.v(str, sb.toString());
            }
            LoaderInfo info = this.mLoaderViewModel.getLoader(id);
            if (info != null) {
                info.destroy(true);
                this.mLoaderViewModel.removeLoader(id);
            }
        } else {
            throw new IllegalStateException("destroyLoader must be called on the main thread");
        }
    }

    @Nullable
    public <D> Loader<D> getLoader(int id) {
        if (!this.mLoaderViewModel.isCreatingLoader()) {
            LoaderInfo<D> info = this.mLoaderViewModel.getLoader(id);
            if (info != null) {
                return info.getLoader();
            }
            return null;
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    public void markForRedelivery() {
        this.mLoaderViewModel.markForRedelivery();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mLifecycleOwner, sb);
        sb.append("}}");
        return sb.toString();
    }

    @Deprecated
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        this.mLoaderViewModel.dump(prefix, fd, writer, args);
    }

    public boolean hasRunningLoaders() {
        return this.mLoaderViewModel.hasRunningLoaders();
    }
}
