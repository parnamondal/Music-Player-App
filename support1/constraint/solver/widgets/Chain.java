package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem system, int orientation) {
        ChainHead[] chainsArray;
        int chainsSize;
        int offset;
        if (orientation == 0) {
            offset = 0;
            chainsSize = constraintWidgetContainer.mHorizontalChainsSize;
            chainsArray = constraintWidgetContainer.mHorizontalChainsArray;
        } else {
            offset = 2;
            chainsSize = constraintWidgetContainer.mVerticalChainsSize;
            chainsArray = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < chainsSize; i++) {
            ChainHead first = chainsArray[i];
            first.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, system, orientation, offset, first)) {
                applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:286:0x0652 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:290:0x0664  */
    /* JADX WARNING: Removed duplicated region for block: B:291:0x0669  */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x0670  */
    /* JADX WARNING: Removed duplicated region for block: B:295:0x0675  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x0678  */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x068c  */
    /* JADX WARNING: Removed duplicated region for block: B:304:0x0690  */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x069c  */
    /* JADX WARNING: Removed duplicated region for block: B:307:0x069f A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer r44, android.support.constraint.solver.LinearSystem r45, int r46, int r47, android.support.constraint.solver.widgets.ChainHead r48) {
        /*
            r0 = r44
            r10 = r45
            r11 = r48
            android.support.constraint.solver.widgets.ConstraintWidget r12 = r11.mFirst
            android.support.constraint.solver.widgets.ConstraintWidget r13 = r11.mLast
            android.support.constraint.solver.widgets.ConstraintWidget r14 = r11.mFirstVisibleWidget
            android.support.constraint.solver.widgets.ConstraintWidget r15 = r11.mLastVisibleWidget
            android.support.constraint.solver.widgets.ConstraintWidget r9 = r11.mHead
            r1 = r12
            r2 = 0
            r3 = 0
            float r4 = r11.mTotalWeight
            android.support.constraint.solver.widgets.ConstraintWidget r8 = r11.mFirstMatchConstraintWidget
            android.support.constraint.solver.widgets.ConstraintWidget r7 = r11.mLastMatchConstraintWidget
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r0.mListDimensionBehaviors
            r5 = r5[r46]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r16 = r1
            if (r5 != r6) goto L_0x0025
            r5 = 1
            goto L_0x0026
        L_0x0025:
            r5 = 0
        L_0x0026:
            r18 = r5
            r5 = 0
            r6 = 0
            r19 = 0
            if (r46 != 0) goto L_0x0052
            int r1 = r9.mHorizontalChainStyle
            if (r1 != 0) goto L_0x0034
            r1 = 1
            goto L_0x0035
        L_0x0034:
            r1 = 0
        L_0x0035:
            int r5 = r9.mHorizontalChainStyle
            r22 = r1
            r1 = 1
            if (r5 != r1) goto L_0x003e
            r1 = 1
            goto L_0x003f
        L_0x003e:
            r1 = 0
        L_0x003f:
            int r5 = r9.mHorizontalChainStyle
            r6 = 2
            if (r5 != r6) goto L_0x0046
            r5 = 1
            goto L_0x0047
        L_0x0046:
            r5 = 0
        L_0x0047:
            r21 = r1
            r23 = r2
            r19 = r5
            r6 = r16
            r16 = r3
            goto L_0x0075
        L_0x0052:
            int r1 = r9.mVerticalChainStyle
            if (r1 != 0) goto L_0x0058
            r1 = 1
            goto L_0x0059
        L_0x0058:
            r1 = 0
        L_0x0059:
            int r5 = r9.mVerticalChainStyle
            r22 = r1
            r1 = 1
            if (r5 != r1) goto L_0x0062
            r1 = 1
            goto L_0x0063
        L_0x0062:
            r1 = 0
        L_0x0063:
            int r5 = r9.mVerticalChainStyle
            r6 = 2
            if (r5 != r6) goto L_0x006a
            r5 = 1
            goto L_0x006b
        L_0x006a:
            r5 = 0
        L_0x006b:
            r21 = r1
            r23 = r2
            r19 = r5
            r6 = r16
            r16 = r3
        L_0x0075:
            r3 = 5
            if (r16 != 0) goto L_0x015e
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r6.mListAnchors
            r2 = r2[r47]
            r24 = 4
            if (r18 != 0) goto L_0x0084
            if (r19 == 0) goto L_0x0083
            goto L_0x0084
        L_0x0083:
            goto L_0x0086
        L_0x0084:
            r24 = 1
        L_0x0086:
            int r25 = r2.getMargin()
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r2.mTarget
            if (r5 == 0) goto L_0x009b
            if (r6 == r12) goto L_0x009b
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r2.mTarget
            int r5 = r5.getMargin()
            int r25 = r25 + r5
            r5 = r25
            goto L_0x009e
        L_0x009b:
            r5 = r25
        L_0x009e:
            if (r19 == 0) goto L_0x00a9
            if (r6 == r12) goto L_0x00a9
            if (r6 == r14) goto L_0x00a9
            r24 = 6
            r27 = r24
            goto L_0x00b6
        L_0x00a9:
            if (r22 == 0) goto L_0x00b3
            if (r18 == 0) goto L_0x00b3
            r24 = 4
            r27 = r24
            goto L_0x00b6
        L_0x00b3:
            r27 = r24
        L_0x00b6:
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r2.mTarget
            if (r1 == 0) goto L_0x00e0
            if (r6 != r14) goto L_0x00c8
            android.support.constraint.solver.SolverVariable r1 = r2.mSolverVariable
            r25 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r2.mTarget
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            r10.addGreaterThan(r1, r4, r5, r3)
            goto L_0x00d4
        L_0x00c8:
            r25 = r4
            android.support.constraint.solver.SolverVariable r1 = r2.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r2.mTarget
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            r3 = 6
            r10.addGreaterThan(r1, r4, r5, r3)
        L_0x00d4:
            android.support.constraint.solver.SolverVariable r1 = r2.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r2.mTarget
            android.support.constraint.solver.SolverVariable r3 = r3.mSolverVariable
            r4 = r27
            r10.addEquality(r1, r3, r5, r4)
            goto L_0x00e4
        L_0x00e0:
            r25 = r4
            r4 = r27
        L_0x00e4:
            if (r18 == 0) goto L_0x0124
            int r1 = r6.getVisibility()
            r3 = 8
            if (r1 == r3) goto L_0x010e
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r6.mListDimensionBehaviors
            r1 = r1[r46]
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r3) goto L_0x010e
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r6.mListAnchors
            int r3 = r47 + 1
            r1 = r1[r3]
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r6.mListAnchors
            r3 = r3[r47]
            android.support.constraint.solver.SolverVariable r3 = r3.mSolverVariable
            r26 = r2
            r27 = r4
            r2 = 0
            r4 = 5
            r10.addGreaterThan(r1, r3, r2, r4)
            goto L_0x0112
        L_0x010e:
            r26 = r2
            r27 = r4
        L_0x0112:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r6.mListAnchors
            r1 = r1[r47]
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r0.mListAnchors
            r2 = r2[r47]
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            r3 = 0
            r4 = 6
            r10.addGreaterThan(r1, r2, r3, r4)
            goto L_0x0128
        L_0x0124:
            r26 = r2
            r27 = r4
        L_0x0128:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r6.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 == 0) goto L_0x014e
            android.support.constraint.solver.widgets.ConstraintWidget r2 = r1.mOwner
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r3 = r3[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x014a
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r3 = r3[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r3 = r3.mOwner
            if (r3 == r6) goto L_0x0147
            goto L_0x014a
        L_0x0147:
            r23 = r2
            goto L_0x0151
        L_0x014a:
            r2 = 0
            r23 = r2
            goto L_0x0151
        L_0x014e:
            r2 = 0
            r23 = r2
        L_0x0151:
            if (r23 == 0) goto L_0x0157
            r2 = r23
            r6 = r2
            goto L_0x015a
        L_0x0157:
            r2 = 1
            r16 = r2
        L_0x015a:
            r4 = r25
            goto L_0x0075
        L_0x015e:
            r25 = r4
            if (r15 == 0) goto L_0x0188
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r13.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 == 0) goto L_0x0188
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r15.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.SolverVariable r2 = r1.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r13.mListAnchors
            int r4 = r47 + 1
            r3 = r3[r4]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            android.support.constraint.solver.SolverVariable r3 = r3.mSolverVariable
            int r4 = r1.getMargin()
            int r4 = -r4
            r5 = 5
            r10.addLowerThan(r2, r3, r4, r5)
            goto L_0x0189
        L_0x0188:
            r5 = 5
        L_0x0189:
            if (r18 == 0) goto L_0x01aa
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r0.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r13.mListAnchors
            int r3 = r47 + 1
            r2 = r2[r3]
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r13.mListAnchors
            int r4 = r47 + 1
            r3 = r3[r4]
            int r3 = r3.getMargin()
            r4 = 6
            r10.addGreaterThan(r1, r2, r3, r4)
            goto L_0x01ab
        L_0x01aa:
        L_0x01ab:
            java.util.ArrayList<android.support.constraint.solver.widgets.ConstraintWidget> r4 = r11.mWeightedMatchConstraintsWidgets
            if (r4 == 0) goto L_0x02a5
            int r1 = r4.size()
            r2 = 1
            if (r1 <= r2) goto L_0x029a
            r3 = 0
            r20 = 0
            boolean r2 = r11.mHasUndefinedWeights
            if (r2 == 0) goto L_0x01c5
            boolean r2 = r11.mHasComplexMatchWeights
            if (r2 != 0) goto L_0x01c5
            int r2 = r11.mWidgetsMatchCount
            float r2 = (float) r2
            goto L_0x01c8
        L_0x01c5:
            r2 = r25
        L_0x01c8:
            r25 = 0
            r5 = r3
            r3 = r25
            r25 = r20
        L_0x01cf:
            if (r3 >= r1) goto L_0x028b
            java.lang.Object r28 = r4.get(r3)
            r0 = r28
            android.support.constraint.solver.widgets.ConstraintWidget r0 = (android.support.constraint.solver.widgets.ConstraintWidget) r0
            r36 = r1
            float[] r1 = r0.mWeight
            r1 = r1[r46]
            r28 = 0
            int r29 = (r1 > r28 ? 1 : (r1 == r28 ? 0 : -1))
            if (r29 >= 0) goto L_0x0213
            r29 = r1
            boolean r1 = r11.mHasComplexMatchWeights
            if (r1 == 0) goto L_0x020a
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r0.mListAnchors
            int r28 = r47 + 1
            r1 = r1[r28]
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            r37 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor[] r4 = r0.mListAnchors
            r4 = r4[r47]
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            r38 = r6
            r6 = 4
            r39 = r7
            r7 = 0
            r10.addEquality(r1, r4, r7, r6)
            r17 = r8
            r7 = 0
            r8 = 6
            goto L_0x027b
        L_0x020a:
            r37 = r4
            r38 = r6
            r39 = r7
            r1 = 1065353216(0x3f800000, float:1.0)
            goto L_0x021b
        L_0x0213:
            r29 = r1
            r37 = r4
            r38 = r6
            r39 = r7
        L_0x021b:
            int r4 = (r1 > r28 ? 1 : (r1 == r28 ? 0 : -1))
            if (r4 != 0) goto L_0x0235
            android.support.constraint.solver.widgets.ConstraintAnchor[] r4 = r0.mListAnchors
            int r6 = r47 + 1
            r4 = r4[r6]
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r6 = r0.mListAnchors
            r6 = r6[r47]
            android.support.constraint.solver.SolverVariable r6 = r6.mSolverVariable
            r17 = r8
            r7 = 0
            r8 = 6
            r10.addEquality(r4, r6, r7, r8)
            goto L_0x027b
        L_0x0235:
            r17 = r8
            r7 = 0
            r8 = 6
            if (r5 == 0) goto L_0x0274
            android.support.constraint.solver.widgets.ConstraintAnchor[] r4 = r5.mListAnchors
            r4 = r4[r47]
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r6 = r5.mListAnchors
            int r24 = r47 + 1
            r6 = r6[r24]
            android.support.constraint.solver.SolverVariable r6 = r6.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r7 = r0.mListAnchors
            r7 = r7[r47]
            android.support.constraint.solver.SolverVariable r7 = r7.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r8 = r0.mListAnchors
            int r28 = r47 + 1
            r8 = r8[r28]
            android.support.constraint.solver.SolverVariable r8 = r8.mSolverVariable
            r41 = r5
            android.support.constraint.solver.ArrayRow r5 = r45.createRow()
            r28 = r5
            r29 = r25
            r30 = r2
            r31 = r1
            r32 = r4
            r33 = r6
            r34 = r7
            r35 = r8
            r28.createRowEqualMatchDimensions(r29, r30, r31, r32, r33, r34, r35)
            r10.addConstraint(r5)
            goto L_0x0276
        L_0x0274:
            r41 = r5
        L_0x0276:
            r4 = r0
            r5 = r1
            r25 = r5
            r5 = r4
        L_0x027b:
            int r3 = r3 + 1
            r8 = r17
            r1 = r36
            r4 = r37
            r6 = r38
            r7 = r39
            r0 = r44
            goto L_0x01cf
        L_0x028b:
            r36 = r1
            r37 = r4
            r41 = r5
            r38 = r6
            r39 = r7
            r17 = r8
            r25 = r2
            goto L_0x02ad
        L_0x029a:
            r36 = r1
            r37 = r4
            r38 = r6
            r39 = r7
            r17 = r8
            goto L_0x02ad
        L_0x02a5:
            r37 = r4
            r38 = r6
            r39 = r7
            r17 = r8
        L_0x02ad:
            if (r14 == 0) goto L_0x0354
            if (r14 == r15) goto L_0x02be
            if (r19 == 0) goto L_0x02b4
            goto L_0x02be
        L_0x02b4:
            r35 = r9
            r30 = r37
            r31 = r38
            r33 = r39
            goto L_0x035c
        L_0x02be:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r12.mListAnchors
            r1 = r1[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r13.mListAnchors
            int r3 = r47 + 1
            r2 = r2[r3]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r12.mListAnchors
            r3 = r3[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x02d9
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r12.mListAnchors
            r3 = r3[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            android.support.constraint.solver.SolverVariable r3 = r3.mSolverVariable
            goto L_0x02da
        L_0x02d9:
            r3 = 0
        L_0x02da:
            r20 = r3
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r13.mListAnchors
            int r4 = r47 + 1
            r3 = r3[r4]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x02f1
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r13.mListAnchors
            int r4 = r47 + 1
            r3 = r3[r4]
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            android.support.constraint.solver.SolverVariable r3 = r3.mSolverVariable
            goto L_0x02f2
        L_0x02f1:
            r3 = 0
        L_0x02f2:
            r24 = r3
            if (r14 != r15) goto L_0x0303
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r14.mListAnchors
            r1 = r3[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r14.mListAnchors
            int r4 = r47 + 1
            r2 = r3[r4]
            r8 = r1
            r7 = r2
            goto L_0x0305
        L_0x0303:
            r8 = r1
            r7 = r2
        L_0x0305:
            if (r20 == 0) goto L_0x0346
            if (r24 == 0) goto L_0x0346
            r1 = 1056964608(0x3f000000, float:0.5)
            if (r46 != 0) goto L_0x0312
            float r1 = r9.mHorizontalBiasPercent
            r26 = r1
            goto L_0x0316
        L_0x0312:
            float r1 = r9.mVerticalBiasPercent
            r26 = r1
        L_0x0316:
            int r27 = r8.getMargin()
            int r28 = r7.getMargin()
            android.support.constraint.solver.SolverVariable r2 = r8.mSolverVariable
            android.support.constraint.solver.SolverVariable r6 = r7.mSolverVariable
            r29 = 5
            r1 = r45
            r3 = r20
            r30 = r37
            r4 = r27
            r5 = r26
            r32 = r6
            r31 = r38
            r6 = r24
            r34 = r7
            r33 = r39
            r7 = r32
            r32 = r8
            r8 = r28
            r35 = r9
            r9 = r29
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0352
        L_0x0346:
            r34 = r7
            r32 = r8
            r35 = r9
            r30 = r37
            r31 = r38
            r33 = r39
        L_0x0352:
            goto L_0x0650
        L_0x0354:
            r35 = r9
            r30 = r37
            r31 = r38
            r33 = r39
        L_0x035c:
            if (r22 == 0) goto L_0x04b2
            if (r14 == 0) goto L_0x04b2
            r1 = r14
            r2 = r14
            int r3 = r11.mWidgetsMatchCount
            if (r3 <= 0) goto L_0x036f
            int r3 = r11.mWidgetsCount
            int r4 = r11.mWidgetsMatchCount
            if (r3 != r4) goto L_0x036f
            r27 = 1
            goto L_0x0371
        L_0x036f:
            r27 = 0
        L_0x0371:
            r20 = r27
            r9 = r1
            r8 = r2
        L_0x0375:
            if (r9 == 0) goto L_0x04aa
            android.support.constraint.solver.widgets.ConstraintWidget[] r1 = r9.mNextChainWidget
            r1 = r1[r46]
            r7 = r1
        L_0x037c:
            if (r7 == 0) goto L_0x038b
            int r1 = r7.getVisibility()
            r5 = 8
            if (r1 != r5) goto L_0x038d
            android.support.constraint.solver.widgets.ConstraintWidget[] r1 = r7.mNextChainWidget
            r7 = r1[r46]
            goto L_0x037c
        L_0x038b:
            r5 = 8
        L_0x038d:
            if (r7 != 0) goto L_0x039c
            if (r9 != r15) goto L_0x0392
            goto L_0x039c
        L_0x0392:
            r39 = r7
            r40 = r8
            r41 = r9
            r0 = 8
            goto L_0x0498
        L_0x039c:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r9.mListAnchors
            r6 = r1[r47]
            android.support.constraint.solver.SolverVariable r4 = r6.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r6.mTarget
            if (r1 == 0) goto L_0x03ab
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r6.mTarget
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            goto L_0x03ac
        L_0x03ab:
            r1 = 0
        L_0x03ac:
            if (r8 == r9) goto L_0x03b9
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r8.mListAnchors
            int r3 = r47 + 1
            r2 = r2[r3]
            android.support.constraint.solver.SolverVariable r1 = r2.mSolverVariable
            r23 = r1
            goto L_0x03d6
        L_0x03b9:
            if (r9 != r14) goto L_0x03d3
            if (r8 != r9) goto L_0x03d3
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r12.mListAnchors
            r2 = r2[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 == 0) goto L_0x03ce
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r12.mListAnchors
            r2 = r2[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x03cf
        L_0x03ce:
            r2 = 0
        L_0x03cf:
            r1 = r2
            r23 = r1
            goto L_0x03d6
        L_0x03d3:
            r23 = r1
        L_0x03d6:
            r1 = 0
            r2 = 0
            r3 = 0
            int r24 = r6.getMargin()
            android.support.constraint.solver.widgets.ConstraintAnchor[] r0 = r9.mListAnchors
            int r27 = r47 + 1
            r0 = r0[r27]
            int r0 = r0.getMargin()
            if (r7 == 0) goto L_0x03fe
            android.support.constraint.solver.widgets.ConstraintAnchor[] r5 = r7.mListAnchors
            r1 = r5[r47]
            android.support.constraint.solver.SolverVariable r2 = r1.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor[] r5 = r9.mListAnchors
            int r28 = r47 + 1
            r5 = r5[r28]
            android.support.constraint.solver.SolverVariable r3 = r5.mSolverVariable
            r28 = r1
            r29 = r2
            r31 = r3
            goto L_0x041a
        L_0x03fe:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r5 = r13.mListAnchors
            int r28 = r47 + 1
            r5 = r5[r28]
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r5.mTarget
            if (r1 == 0) goto L_0x040b
            android.support.constraint.solver.SolverVariable r2 = r1.mSolverVariable
            goto L_0x040c
        L_0x040b:
        L_0x040c:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r5 = r9.mListAnchors
            int r28 = r47 + 1
            r5 = r5[r28]
            android.support.constraint.solver.SolverVariable r3 = r5.mSolverVariable
            r28 = r1
            r29 = r2
            r31 = r3
        L_0x041a:
            if (r28 == 0) goto L_0x0422
            int r1 = r28.getMargin()
            int r0 = r0 + r1
            goto L_0x0423
        L_0x0422:
        L_0x0423:
            if (r8 == 0) goto L_0x0432
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r8.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            int r1 = r1.getMargin()
            int r24 = r24 + r1
            goto L_0x0433
        L_0x0432:
        L_0x0433:
            if (r4 == 0) goto L_0x048a
            if (r23 == 0) goto L_0x048a
            if (r29 == 0) goto L_0x048a
            if (r31 == 0) goto L_0x048a
            r1 = r24
            if (r9 != r14) goto L_0x044a
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r14.mListAnchors
            r2 = r2[r47]
            int r1 = r2.getMargin()
            r32 = r1
            goto L_0x044c
        L_0x044a:
            r32 = r1
        L_0x044c:
            r1 = r0
            if (r9 != r15) goto L_0x045c
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r15.mListAnchors
            int r3 = r47 + 1
            r2 = r2[r3]
            int r1 = r2.getMargin()
            r34 = r1
            goto L_0x045e
        L_0x045c:
            r34 = r1
        L_0x045e:
            r1 = 4
            if (r20 == 0) goto L_0x0465
            r1 = 6
            r36 = r1
            goto L_0x0467
        L_0x0465:
            r36 = r1
        L_0x0467:
            r5 = 1056964608(0x3f000000, float:0.5)
            r1 = r45
            r2 = r4
            r3 = r23
            r37 = r4
            r4 = r32
            r38 = r0
            r0 = 8
            r27 = r6
            r6 = r29
            r39 = r7
            r7 = r31
            r40 = r8
            r8 = r34
            r41 = r9
            r9 = r36
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0498
        L_0x048a:
            r38 = r0
            r37 = r4
            r27 = r6
            r39 = r7
            r40 = r8
            r41 = r9
            r0 = 8
        L_0x0498:
            int r1 = r41.getVisibility()
            if (r1 == r0) goto L_0x04a2
            r1 = r41
            r8 = r1
            goto L_0x04a4
        L_0x04a2:
            r8 = r40
        L_0x04a4:
            r9 = r39
            r23 = r39
            goto L_0x0375
        L_0x04aa:
            r40 = r8
            r41 = r9
            r31 = r41
            goto L_0x0650
        L_0x04b2:
            r0 = 8
            if (r21 == 0) goto L_0x064f
            if (r14 == 0) goto L_0x064f
            r1 = r14
            r2 = r14
            int r3 = r11.mWidgetsMatchCount
            if (r3 <= 0) goto L_0x04c7
            int r3 = r11.mWidgetsCount
            int r4 = r11.mWidgetsMatchCount
            if (r3 != r4) goto L_0x04c7
            r27 = 1
            goto L_0x04c9
        L_0x04c7:
            r27 = 0
        L_0x04c9:
            r24 = r27
            r9 = r1
            r8 = r2
        L_0x04cd:
            if (r9 == 0) goto L_0x05c5
            android.support.constraint.solver.widgets.ConstraintWidget[] r1 = r9.mNextChainWidget
            r1 = r1[r46]
        L_0x04d3:
            if (r1 == 0) goto L_0x04e0
            int r2 = r1.getVisibility()
            if (r2 != r0) goto L_0x04e0
            android.support.constraint.solver.widgets.ConstraintWidget[] r2 = r1.mNextChainWidget
            r1 = r2[r46]
            goto L_0x04d3
        L_0x04e0:
            if (r9 == r14) goto L_0x05ab
            if (r9 == r15) goto L_0x05ab
            if (r1 == 0) goto L_0x05ab
            if (r1 != r15) goto L_0x04ec
            r1 = 0
            r7 = r1
            goto L_0x04ed
        L_0x04ec:
            r7 = r1
        L_0x04ed:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r9.mListAnchors
            r6 = r1[r47]
            android.support.constraint.solver.SolverVariable r5 = r6.mSolverVariable
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r6.mTarget
            if (r1 == 0) goto L_0x04fc
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r6.mTarget
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            goto L_0x04fd
        L_0x04fc:
            r1 = 0
        L_0x04fd:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r8.mListAnchors
            int r3 = r47 + 1
            r2 = r2[r3]
            android.support.constraint.solver.SolverVariable r4 = r2.mSolverVariable
            r1 = 0
            r2 = 0
            r3 = 0
            int r23 = r6.getMargin()
            android.support.constraint.solver.widgets.ConstraintAnchor[] r0 = r9.mListAnchors
            int r28 = r47 + 1
            r0 = r0[r28]
            int r0 = r0.getMargin()
            if (r7 == 0) goto L_0x0533
            r28 = r1
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r7.mListAnchors
            r1 = r1[r47]
            android.support.constraint.solver.SolverVariable r2 = r1.mSolverVariable
            r28 = r2
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x052b
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x052c
        L_0x052b:
            r2 = 0
        L_0x052c:
            r31 = r2
            r29 = r28
            r28 = r1
            goto L_0x0551
        L_0x0533:
            r28 = r1
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r9.mListAnchors
            int r29 = r47 + 1
            r1 = r1[r29]
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 == 0) goto L_0x0542
            android.support.constraint.solver.SolverVariable r2 = r1.mSolverVariable
            goto L_0x0543
        L_0x0542:
        L_0x0543:
            r28 = r1
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r9.mListAnchors
            int r29 = r47 + 1
            r1 = r1[r29]
            android.support.constraint.solver.SolverVariable r1 = r1.mSolverVariable
            r31 = r1
            r29 = r2
        L_0x0551:
            if (r28 == 0) goto L_0x0559
            int r1 = r28.getMargin()
            int r0 = r0 + r1
            goto L_0x055a
        L_0x0559:
        L_0x055a:
            if (r8 == 0) goto L_0x0569
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r8.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            int r1 = r1.getMargin()
            int r23 = r23 + r1
            goto L_0x056a
        L_0x0569:
        L_0x056a:
            r1 = 4
            if (r24 == 0) goto L_0x0571
            r1 = 6
            r32 = r1
            goto L_0x0573
        L_0x0571:
            r32 = r1
        L_0x0573:
            if (r5 == 0) goto L_0x059c
            if (r4 == 0) goto L_0x059c
            if (r29 == 0) goto L_0x059c
            if (r31 == 0) goto L_0x059c
            r34 = 1056964608(0x3f000000, float:0.5)
            r1 = r45
            r2 = r5
            r3 = r4
            r20 = r4
            r4 = r23
            r36 = r5
            r5 = r34
            r34 = r6
            r6 = r29
            r37 = r7
            r7 = r31
            r38 = r8
            r8 = r0
            r39 = r9
            r9 = r32
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x05a8
        L_0x059c:
            r20 = r4
            r36 = r5
            r34 = r6
            r37 = r7
            r38 = r8
            r39 = r9
        L_0x05a8:
            r23 = r37
            goto L_0x05b1
        L_0x05ab:
            r38 = r8
            r39 = r9
            r23 = r1
        L_0x05b1:
            int r0 = r39.getVisibility()
            r1 = 8
            if (r0 == r1) goto L_0x05bd
            r0 = r39
            r8 = r0
            goto L_0x05bf
        L_0x05bd:
            r8 = r38
        L_0x05bf:
            r9 = r23
            r0 = 8
            goto L_0x04cd
        L_0x05c5:
            r38 = r8
            r39 = r9
            android.support.constraint.solver.widgets.ConstraintAnchor[] r0 = r14.mListAnchors
            r0 = r0[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r12.mListAnchors
            r1 = r1[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor r9 = r1.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r15.mListAnchors
            int r2 = r47 + 1
            r8 = r1[r2]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r13.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.widgets.ConstraintAnchor r7 = r1.mTarget
            if (r9 == 0) goto L_0x062e
            if (r14 == r15) goto L_0x05f8
            android.support.constraint.solver.SolverVariable r1 = r0.mSolverVariable
            android.support.constraint.solver.SolverVariable r2 = r9.mSolverVariable
            int r3 = r0.getMargin()
            r6 = 5
            r10.addEquality(r1, r2, r3, r6)
            r42 = r7
            r43 = r8
            r20 = r9
            goto L_0x0634
        L_0x05f8:
            r6 = 5
            if (r7 == 0) goto L_0x0627
            android.support.constraint.solver.SolverVariable r2 = r0.mSolverVariable
            android.support.constraint.solver.SolverVariable r3 = r9.mSolverVariable
            int r4 = r0.getMargin()
            android.support.constraint.solver.SolverVariable r1 = r8.mSolverVariable
            android.support.constraint.solver.SolverVariable r5 = r7.mSolverVariable
            int r27 = r8.getMargin()
            r28 = 5
            r29 = r1
            r1 = r45
            r20 = r5
            r5 = 1056964608(0x3f000000, float:0.5)
            r6 = r29
            r42 = r7
            r7 = r20
            r43 = r8
            r8 = r27
            r20 = r9
            r9 = r28
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0634
        L_0x0627:
            r42 = r7
            r43 = r8
            r20 = r9
            goto L_0x0634
        L_0x062e:
            r42 = r7
            r43 = r8
            r20 = r9
        L_0x0634:
            r1 = r42
            if (r1 == 0) goto L_0x064a
            if (r14 == r15) goto L_0x064a
            r2 = r43
            android.support.constraint.solver.SolverVariable r3 = r2.mSolverVariable
            android.support.constraint.solver.SolverVariable r4 = r1.mSolverVariable
            int r5 = r2.getMargin()
            int r5 = -r5
            r6 = 5
            r10.addEquality(r3, r4, r5, r6)
            goto L_0x064c
        L_0x064a:
            r2 = r43
        L_0x064c:
            r31 = r39
            goto L_0x0650
        L_0x064f:
        L_0x0650:
            if (r22 != 0) goto L_0x0654
            if (r21 == 0) goto L_0x06d4
        L_0x0654:
            if (r14 == 0) goto L_0x06d4
            android.support.constraint.solver.widgets.ConstraintAnchor[] r0 = r14.mListAnchors
            r0 = r0[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r15.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r0.mTarget
            if (r2 == 0) goto L_0x0669
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r0.mTarget
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x066a
        L_0x0669:
            r2 = 0
        L_0x066a:
            r20 = r2
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x0675
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            android.support.constraint.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x0676
        L_0x0675:
            r2 = 0
        L_0x0676:
            if (r13 == r15) goto L_0x068c
            android.support.constraint.solver.widgets.ConstraintAnchor[] r3 = r13.mListAnchors
            int r4 = r47 + 1
            r3 = r3[r4]
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r3.mTarget
            if (r4 == 0) goto L_0x0687
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r3.mTarget
            android.support.constraint.solver.SolverVariable r4 = r4.mSolverVariable
            goto L_0x0688
        L_0x0687:
            r4 = 0
        L_0x0688:
            r2 = r4
            r24 = r2
            goto L_0x068e
        L_0x068c:
            r24 = r2
        L_0x068e:
            if (r14 != r15) goto L_0x069c
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r14.mListAnchors
            r0 = r2[r47]
            android.support.constraint.solver.widgets.ConstraintAnchor[] r2 = r14.mListAnchors
            int r3 = r47 + 1
            r1 = r2[r3]
            r9 = r1
            goto L_0x069d
        L_0x069c:
            r9 = r1
        L_0x069d:
            if (r20 == 0) goto L_0x06d1
            if (r24 == 0) goto L_0x06d1
            r26 = 1056964608(0x3f000000, float:0.5)
            int r27 = r0.getMargin()
            if (r15 != 0) goto L_0x06ac
            r1 = r13
            r15 = r1
            goto L_0x06ad
        L_0x06ac:
        L_0x06ad:
            android.support.constraint.solver.widgets.ConstraintAnchor[] r1 = r15.mListAnchors
            int r2 = r47 + 1
            r1 = r1[r2]
            int r28 = r1.getMargin()
            android.support.constraint.solver.SolverVariable r2 = r0.mSolverVariable
            android.support.constraint.solver.SolverVariable r7 = r9.mSolverVariable
            r29 = 5
            r1 = r45
            r3 = r20
            r4 = r27
            r5 = r26
            r6 = r24
            r8 = r28
            r32 = r9
            r9 = r29
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x06d5
        L_0x06d1:
            r32 = r9
            goto L_0x06d5
        L_0x06d4:
        L_0x06d5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Chain.applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):void");
    }
}
