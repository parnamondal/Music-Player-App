package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.graphics.drawable.DrawableCompat;
import android.support.p000v4.view.ActionProvider;
import android.support.p000v4.view.ActionProvider.SubUiVisibilityListener;
import android.support.p000v4.view.GravityCompat;
import android.support.p003v7.appcompat.C0333R;
import android.support.p003v7.view.ActionBarPolicy;
import android.support.p003v7.view.menu.ActionMenuItemView;
import android.support.p003v7.view.menu.ActionMenuItemView.PopupCallback;
import android.support.p003v7.view.menu.BaseMenuPresenter;
import android.support.p003v7.view.menu.MenuBuilder;
import android.support.p003v7.view.menu.MenuItemImpl;
import android.support.p003v7.view.menu.MenuPopupHelper;
import android.support.p003v7.view.menu.MenuPresenter.Callback;
import android.support.p003v7.view.menu.MenuView;
import android.support.p003v7.view.menu.MenuView.ItemView;
import android.support.p003v7.view.menu.ShowableListMenu;
import android.support.p003v7.view.menu.SubMenuBuilder;
import android.support.p003v7.widget.ActionMenuView.ActionMenuChildView;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;

/* renamed from: android.support.v7.widget.ActionMenuPresenter */
class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$ActionButtonSubmenu */
    private class ActionButtonSubmenu extends MenuPopupHelper {
        public ActionButtonSubmenu(Context context, SubMenuBuilder subMenu, View anchorView) {
            super(context, subMenu, anchorView, false, C0333R.attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl) subMenu.getItem()).isActionButton()) {
                setAnchorView(ActionMenuPresenter.this.mOverflowButton == null ? (View) ActionMenuPresenter.this.mMenuView : ActionMenuPresenter.this.mOverflowButton);
            }
            setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        /* access modifiers changed from: protected */
        public void onDismiss() {
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            actionMenuPresenter.mActionButtonPopup = null;
            actionMenuPresenter.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$ActionMenuPopupCallback */
    private class ActionMenuPopupCallback extends PopupCallback {
        ActionMenuPopupCallback() {
        }

        public ShowableListMenu getPopup() {
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                return ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            return null;
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$OpenOverflowRunnable */
    private class OpenOverflowRunnable implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup popup) {
            this.mPopup = popup;
        }

        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            View menuView = (View) ActionMenuPresenter.this.mMenuView;
            if (!(menuView == null || menuView.getWindowToken() == null || !this.mPopup.tryShow())) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$OverflowMenuButton */
    private class OverflowMenuButton extends AppCompatImageView implements ActionMenuChildView {
        private final float[] mTempPts = new float[2];

        public OverflowMenuButton(Context context) {
            super(context, null, C0333R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            TooltipCompat.setTooltipText(this, getContentDescription());
            setOnTouchListener(new ForwardingListener(this, ActionMenuPresenter.this) {
                public ShowableListMenu getPopup() {
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        return null;
                    }
                    return ActionMenuPresenter.this.mOverflowPopup.getPopup();
                }

                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }

                public boolean onForwardingStopped() {
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        return false;
                    }
                    ActionMenuPresenter.this.hideOverflowMenu();
                    return true;
                }
            });
        }

        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        public boolean needsDividerAfter() {
            return false;
        }

        /* access modifiers changed from: protected */
        public boolean setFrame(int l, int t, int r, int b) {
            boolean changed = super.setFrame(l, t, r, b);
            Drawable d = getDrawable();
            Drawable bg = getBackground();
            if (!(d == null || bg == null)) {
                int width = getWidth();
                int height = getHeight();
                int halfEdge = Math.max(width, height) / 2;
                int centerX = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                int centerY = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                DrawableCompat.setHotspotBounds(bg, centerX - halfEdge, centerY - halfEdge, centerX + halfEdge, centerY + halfEdge);
            }
            return changed;
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$OverflowPopup */
    private class OverflowPopup extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, C0333R.attr.actionOverflowMenuStyle);
            setGravity(GravityCompat.END);
            setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        /* access modifiers changed from: protected */
        public void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$PopupPresenterCallback */
    private class PopupPresenterCallback implements Callback {
        PopupPresenterCallback() {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            boolean z = false;
            if (subMenu == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) subMenu).getItem().getItemId();
            Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                z = cb.onOpenSubMenu(subMenu);
            }
            return z;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof SubMenuBuilder) {
                menu.getRootMenu().close(false);
            }
            Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    /* renamed from: android.support.v7.widget.ActionMenuPresenter$SavedState */
    private static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel in) {
            this.openSubMenuId = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.openSubMenuId);
        }
    }

    public ActionMenuPresenter(Context context) {
        super(context, C0333R.layout.abc_action_menu_layout, C0333R.layout.abc_action_menu_item_layout);
    }

    public void initForMenu(@NonNull Context context, @Nullable MenuBuilder menu) {
        super.initForMenu(context, menu);
        Resources res = context.getResources();
        ActionBarPolicy abp = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = abp.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = abp.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = abp.getMaxActionButtons();
        }
        int width = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int spec = MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(spec, spec);
            }
            width -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = width;
        this.mMinCellSize = (int) (res.getDisplayMetrics().density * 56.0f);
        this.mScrapActionButtonView = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    public void setWidthLimit(int width, boolean strict) {
        this.mWidthLimit = width;
        this.mStrictWidthLimit = strict;
        this.mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
        this.mReserveOverflowSet = true;
    }

    public void setItemLimit(int itemCount) {
        this.mMaxItems = itemCount;
        this.mMaxItemsSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean isExclusive) {
        this.mExpandedActionViewsExclusive = isExclusive;
    }

    public void setOverflowIcon(Drawable icon) {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            overflowMenuButton.setImageDrawable(icon);
            return;
        }
        this.mPendingOverflowIconSet = true;
        this.mPendingOverflowIcon = icon;
    }

    public Drawable getOverflowIcon() {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            return overflowMenuButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public MenuView getMenuView(ViewGroup root) {
        MenuView oldMenuView = this.mMenuView;
        MenuView result = super.getMenuView(root);
        if (oldMenuView != result) {
            ((ActionMenuView) result).setPresenter(this);
        }
        return result;
    }

    public View getItemView(MenuItemImpl item, View convertView, ViewGroup parent) {
        View actionView = item.getActionView();
        if (actionView == null || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? 8 : 0);
        ActionMenuView menuParent = (ActionMenuView) parent;
        LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    public void bindItemView(MenuItemImpl item, ItemView itemView) {
        itemView.initialize(item, 0);
        ActionMenuItemView actionItemView = (ActionMenuItemView) itemView;
        actionItemView.setItemInvoker((ActionMenuView) this.mMenuView);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(this.mPopupCallback);
    }

    public boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        return item.isActionButton();
    }

    public void updateMenuView(boolean cleared) {
        super.updateMenuView(cleared);
        ((View) this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            int count = actionItems.size();
            for (int i = 0; i < count; i++) {
                ActionProvider provider = ((MenuItemImpl) actionItems.get(i)).getSupportActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        boolean hasOverflow = false;
        if (this.mReserveOverflow && nonActionItems != null) {
            int count2 = nonActionItems.size();
            boolean z = false;
            if (count2 == 1) {
                hasOverflow = !((MenuItemImpl) nonActionItems.get(0)).isActionViewExpanded();
            } else {
                if (count2 > 0) {
                    z = true;
                }
                hasOverflow = z;
            }
        }
        if (hasOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            ViewGroup parent = (ViewGroup) this.mOverflowButton.getParent();
            if (parent != this.mMenuView) {
                if (parent != null) {
                    parent.removeView(this.mOverflowButton);
                }
                ActionMenuView menuView = (ActionMenuView) this.mMenuView;
                menuView.addView(this.mOverflowButton, menuView.generateOverflowButtonLayoutParams());
            }
        } else {
            OverflowMenuButton overflowMenuButton = this.mOverflowButton;
            if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView) {
                ((ViewGroup) this.mMenuView).removeView(this.mOverflowButton);
            }
        }
        ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    public boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(parent, childIndex);
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != this.mMenu) {
            topSubMenu = (SubMenuBuilder) topSubMenu.getParentMenu();
        }
        View anchor = findViewForItem(topSubMenu.getItem());
        if (anchor == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        boolean preserveIconSpacing = false;
        int count = subMenu.size();
        int i = 0;
        while (true) {
            if (i >= count) {
                break;
            }
            MenuItem childItem = subMenu.getItem(i);
            if (childItem.isVisible() && childItem.getIcon() != null) {
                preserveIconSpacing = true;
                break;
            }
            i++;
        }
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenu, anchor);
        this.mActionButtonPopup.setForceShowIcon(preserveIconSpacing);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    private View findViewForItem(MenuItem item) {
        ViewGroup parent = (ViewGroup) this.mMenuView;
        if (parent == null) {
            return null;
        }
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if ((child instanceof ItemView) && ((ItemView) child).getItemData() == item) {
                return child;
            }
        }
        return null;
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null || this.mMenu.getNonActionItems().isEmpty()) {
            return false;
        }
        OverflowPopup overflowPopup = new OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true);
        this.mPostedOpenRunnable = new OpenOverflowRunnable(overflowPopup);
        ((View) this.mMenuView).post(this.mPostedOpenRunnable);
        super.onSubMenuSelected(null);
        return true;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable == null || this.mMenuView == null) {
            MenuPopupHelper popup = this.mOverflowPopup;
            if (popup == null) {
                return false;
            }
            popup.dismiss();
            return true;
        }
        ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
        this.mPostedOpenRunnable = null;
        return true;
    }

    public boolean dismissPopupMenus() {
        return hideOverflowMenu() | hideSubMenus();
    }

    public boolean hideSubMenus() {
        ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
        if (actionButtonSubmenu == null) {
            return false;
        }
        actionButtonSubmenu.dismiss();
        return true;
    }

    public boolean isOverflowMenuShowing() {
        OverflowPopup overflowPopup = this.mOverflowPopup;
        return overflowPopup != null && overflowPopup.isShowing();
    }

    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x018b  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0157  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0187  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean flagActionItems() {
        /*
            r24 = this;
            r0 = r24
            android.support.v7.view.menu.MenuBuilder r1 = r0.mMenu
            if (r1 == 0) goto L_0x0011
            android.support.v7.view.menu.MenuBuilder r1 = r0.mMenu
            java.util.ArrayList r1 = r1.getVisibleItems()
            int r2 = r1.size()
            goto L_0x0013
        L_0x0011:
            r1 = 0
            r2 = 0
        L_0x0013:
            int r3 = r0.mMaxItems
            int r4 = r0.mActionItemWidthLimit
            r5 = 0
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r5)
            android.support.v7.view.menu.MenuView r7 = r0.mMenuView
            android.view.ViewGroup r7 = (android.view.ViewGroup) r7
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
        L_0x0025:
            if (r12 >= r2) goto L_0x0050
            java.lang.Object r13 = r1.get(r12)
            android.support.v7.view.menu.MenuItemImpl r13 = (android.support.p003v7.view.menu.MenuItemImpl) r13
            boolean r14 = r13.requiresActionButton()
            if (r14 == 0) goto L_0x0036
            int r8 = r8 + 1
            goto L_0x0040
        L_0x0036:
            boolean r14 = r13.requestsActionButton()
            if (r14 == 0) goto L_0x003f
            int r9 = r9 + 1
            goto L_0x0040
        L_0x003f:
            r11 = 1
        L_0x0040:
            boolean r14 = r0.mExpandedActionViewsExclusive
            if (r14 == 0) goto L_0x004c
            boolean r14 = r13.isActionViewExpanded()
            if (r14 == 0) goto L_0x004c
            r3 = 0
            goto L_0x004d
        L_0x004c:
        L_0x004d:
            int r12 = r12 + 1
            goto L_0x0025
        L_0x0050:
            boolean r12 = r0.mReserveOverflow
            if (r12 == 0) goto L_0x005d
            if (r11 != 0) goto L_0x005a
            int r12 = r8 + r9
            if (r12 <= r3) goto L_0x005d
        L_0x005a:
            int r3 = r3 + -1
            goto L_0x005e
        L_0x005d:
        L_0x005e:
            int r3 = r3 - r8
            android.util.SparseBooleanArray r12 = r0.mActionButtonGroups
            r12.clear()
            r13 = 0
            r14 = 0
            boolean r15 = r0.mStrictWidthLimit
            if (r15 == 0) goto L_0x0075
            int r15 = r0.mMinCellSize
            int r14 = r4 / r15
            int r16 = r4 % r15
            int r17 = r16 / r14
            int r13 = r15 + r17
            goto L_0x0076
        L_0x0075:
        L_0x0076:
            r15 = 0
        L_0x0077:
            if (r15 >= r2) goto L_0x01ae
            java.lang.Object r17 = r1.get(r15)
            r5 = r17
            android.support.v7.view.menu.MenuItemImpl r5 = (android.support.p003v7.view.menu.MenuItemImpl) r5
            boolean r17 = r5.requiresActionButton()
            if (r17 == 0) goto L_0x00d0
            r17 = r2
            android.view.View r2 = r0.mScrapActionButtonView
            android.view.View r2 = r0.getItemView(r5, r2, r7)
            r19 = r8
            android.view.View r8 = r0.mScrapActionButtonView
            if (r8 != 0) goto L_0x0098
            r0.mScrapActionButtonView = r2
            goto L_0x0099
        L_0x0098:
        L_0x0099:
            boolean r8 = r0.mStrictWidthLimit
            if (r8 == 0) goto L_0x00a5
            r8 = 0
            int r20 = android.support.p003v7.widget.ActionMenuView.measureChildForCells(r2, r13, r14, r6, r8)
            int r14 = r14 - r20
            goto L_0x00a8
        L_0x00a5:
            r2.measure(r6, r6)
        L_0x00a8:
            int r8 = r2.getMeasuredWidth()
            int r4 = r4 - r8
            if (r10 != 0) goto L_0x00b1
            r10 = r8
            goto L_0x00b2
        L_0x00b1:
        L_0x00b2:
            r20 = r2
            int r2 = r5.getGroupId()
            if (r2 == 0) goto L_0x00c1
            r21 = r4
            r4 = 1
            r12.put(r2, r4)
            goto L_0x00c4
        L_0x00c1:
            r21 = r4
            r4 = 1
        L_0x00c4:
            r5.setIsActionButton(r4)
            r22 = r7
            r4 = r21
            r0 = 0
            r21 = r1
            goto L_0x019f
        L_0x00d0:
            r17 = r2
            r19 = r8
            boolean r2 = r5.requestsActionButton()
            if (r2 == 0) goto L_0x0195
            int r2 = r5.getGroupId()
            boolean r8 = r12.get(r2)
            if (r3 > 0) goto L_0x00ea
            if (r8 == 0) goto L_0x00e7
            goto L_0x00ea
        L_0x00e7:
            r20 = r3
            goto L_0x00f8
        L_0x00ea:
            if (r4 <= 0) goto L_0x00f6
            r20 = r3
            boolean r3 = r0.mStrictWidthLimit
            if (r3 == 0) goto L_0x00f4
            if (r14 <= 0) goto L_0x00f8
        L_0x00f4:
            r3 = 1
            goto L_0x00f9
        L_0x00f6:
            r20 = r3
        L_0x00f8:
            r3 = 0
        L_0x00f9:
            if (r3 == 0) goto L_0x0145
            r21 = r3
            android.view.View r3 = r0.mScrapActionButtonView
            android.view.View r3 = r0.getItemView(r5, r3, r7)
            r22 = r7
            android.view.View r7 = r0.mScrapActionButtonView
            if (r7 != 0) goto L_0x010c
            r0.mScrapActionButtonView = r3
            goto L_0x010d
        L_0x010c:
        L_0x010d:
            boolean r7 = r0.mStrictWidthLimit
            if (r7 == 0) goto L_0x0120
            r7 = 0
            int r23 = android.support.p003v7.widget.ActionMenuView.measureChildForCells(r3, r13, r14, r6, r7)
            int r14 = r14 - r23
            if (r23 != 0) goto L_0x011e
            r7 = 0
            r21 = r7
            goto L_0x011f
        L_0x011e:
        L_0x011f:
            goto L_0x0123
        L_0x0120:
            r3.measure(r6, r6)
        L_0x0123:
            int r7 = r3.getMeasuredWidth()
            int r4 = r4 - r7
            if (r10 != 0) goto L_0x012c
            r10 = r7
            goto L_0x012d
        L_0x012c:
        L_0x012d:
            r23 = r3
            boolean r3 = r0.mStrictWidthLimit
            if (r3 == 0) goto L_0x013b
            if (r4 < 0) goto L_0x0137
            r3 = 1
            goto L_0x0138
        L_0x0137:
            r3 = 0
        L_0x0138:
            r3 = r21 & r3
            goto L_0x0149
        L_0x013b:
            int r3 = r4 + r10
            if (r3 <= 0) goto L_0x0141
            r3 = 1
            goto L_0x0142
        L_0x0141:
            r3 = 0
        L_0x0142:
            r3 = r21 & r3
            goto L_0x0149
        L_0x0145:
            r21 = r3
            r22 = r7
        L_0x0149:
            if (r3 == 0) goto L_0x0154
            if (r2 == 0) goto L_0x0154
            r7 = 1
            r12.put(r2, r7)
            r21 = r1
            goto L_0x0189
        L_0x0154:
            if (r8 == 0) goto L_0x0187
            r7 = 0
            r12.put(r2, r7)
            r7 = 0
        L_0x015c:
            if (r7 >= r15) goto L_0x0184
            java.lang.Object r18 = r1.get(r7)
            r0 = r18
            android.support.v7.view.menu.MenuItemImpl r0 = (android.support.p003v7.view.menu.MenuItemImpl) r0
            r21 = r1
            int r1 = r0.getGroupId()
            if (r1 != r2) goto L_0x017c
            boolean r1 = r0.isActionButton()
            if (r1 == 0) goto L_0x0177
            int r20 = r20 + 1
        L_0x0177:
            r1 = 0
            r0.setIsActionButton(r1)
            goto L_0x017d
        L_0x017c:
        L_0x017d:
            int r7 = r7 + 1
            r1 = r21
            r0 = r24
            goto L_0x015c
        L_0x0184:
            r21 = r1
            goto L_0x0189
        L_0x0187:
            r21 = r1
        L_0x0189:
            if (r3 == 0) goto L_0x018e
            int r20 = r20 + -1
        L_0x018e:
            r5.setIsActionButton(r3)
            r3 = r20
            r0 = 0
            goto L_0x019f
        L_0x0195:
            r21 = r1
            r20 = r3
            r22 = r7
            r0 = 0
            r5.setIsActionButton(r0)
        L_0x019f:
            int r15 = r15 + 1
            r2 = r17
            r8 = r19
            r1 = r21
            r7 = r22
            r0 = r24
            r5 = 0
            goto L_0x0077
        L_0x01ae:
            r21 = r1
            r17 = r2
            r20 = r3
            r22 = r7
            r19 = r8
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.widget.ActionMenuPresenter.flagActionItems():boolean");
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState();
        state.openSubMenuId = this.mOpenSubMenuId;
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState saved = (SavedState) state;
            if (saved.openSubMenuId > 0) {
                MenuItem item = this.mMenu.findItem(saved.openSubMenuId);
                if (item != null) {
                    onSubMenuSelected((SubMenuBuilder) item.getSubMenu());
                }
            }
        }
    }

    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            super.onSubMenuSelected(null);
        } else if (this.mMenu != null) {
            this.mMenu.close(false);
        }
    }

    public void setMenuView(ActionMenuView menuView) {
        this.mMenuView = menuView;
        menuView.initialize(this.mMenu);
    }
}
