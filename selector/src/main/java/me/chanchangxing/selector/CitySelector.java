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

    private SelectorFragment fragment;
    private FragmentManager fm;
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

    public void build() {
        if ((fragment = (SelectorFragment) fm.findFragmentByTag("city_selector")) == null) {
            fragment = new SelectorFragment();
            fm.beginTransaction().add(mRes, fragment, "city_selector").commit();
        }

        fm.beginTransaction().show(fragment).commit();

        fragment.setOnClickBgListener(new OnClickBgListener() {
            @Override
            public void onClickBg() {
                fm.beginTransaction().hide(fragment).commit();
            }
        });

        fragment.setCurrentItem(mArgs);
        fragment.setOnMoveListener(mOnMoveListener);
    }

    private CitySelector(Activity activity, @IdRes int res) {
        this.mRes = res;

        fm = activity.getFragmentManager();
    }
}