package me.chanchangxing.selector;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.IdRes;

import me.chanchangxing.selector.ui.OnClickBgListener;
import me.chanchangxing.selector.ui.OnMoveListener;
import me.chanchangxing.selector.ui.SelectorFragment;

/**
 * Created by chenchangxing on 2017/12/25.
 */

public class CitySelector {

    private SelectorFragment mFragment;
    private FragmentManager mFm;
    private int mRes;
    private String[] mArgs;
    private OnMoveListener mOnMoveListener;

    public static CitySelector init(Activity activity, @IdRes int res) {
        return new CitySelector(activity, res);
    }

    public CitySelector setCurrentItem(String... args) {
        if (args.length < 1 || args.length > 3) {
            try {
                throw new Throwable("方法参数有误");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        this.mArgs = args;
        return this;
    }

    public CitySelector addListener(OnMoveListener onMoveListener) {
        mOnMoveListener = onMoveListener;
        return this;
    }

    public void show() {
        if ((mFragment = (SelectorFragment) mFm.findFragmentByTag("city_selector")) == null) {
            mFragment = new SelectorFragment();
            mFm.beginTransaction().add(mRes, mFragment, "city_selector").commit();
        }

        mFm.beginTransaction().show(mFragment).commit();

        mFragment.setOnClickBgListener(new OnClickBgListener() {
            @Override
            public void onClickBg() {
                mFm.beginTransaction().hide(mFragment).commit();
            }
        });

        mFragment.setCurrentItem(mArgs);
        mFragment.setOnMoveListener(mOnMoveListener);
    }

    private CitySelector(Activity activity, @IdRes int res) {
        this.mRes = res;
        mFm = activity.getFragmentManager();
    }
}