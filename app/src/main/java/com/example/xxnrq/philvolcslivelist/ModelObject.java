package com.example.xxnrq.philvolcslivelist;

/**
 * Created by Royce on 2/1/2017.
 */
public enum ModelObject {

    tip1(R.string.slide1, R.layout.tip_before),
    tip2(R.string.slide2, R.layout.tip_before1),
    tip3(R.string.slide3, R.layout.tip_before2),
    tip4(R.string.slide4, R.layout.tip_before3),
    tip5(R.string.slide5, R.layout.tip_during),
    tip6(R.string.slide6, R.layout.tip_during1),
    tip7(R.string.slide7, R.layout.tip_during2),
    tip8(R.string.slide8, R.layout.tip_during3),
    tip9(R.string.slide9, R.layout.tip_after),
    tip10(R.string.slide10,R.layout.tip_after1),
    tip11(R.string.slide11,R.layout.tip_after2),
    tip12(R.string.slide12,R.layout.tip_after2);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
