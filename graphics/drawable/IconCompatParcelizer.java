package android.support.p000v4.graphics.drawable;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import androidx.versionedparcelable.VersionedParcel;

@RestrictTo({Scope.LIBRARY})
/* renamed from: android.support.v4.graphics.drawable.IconCompatParcelizer */
public final class IconCompatParcelizer extends androidx.core.graphics.drawable.IconCompatParcelizer {
    public static IconCompat read(VersionedParcel parcel) {
        return androidx.core.graphics.drawable.IconCompatParcelizer.read(parcel);
    }

    public static void write(IconCompat obj, VersionedParcel parcel) {
        androidx.core.graphics.drawable.IconCompatParcelizer.write(obj, parcel);
    }
}
