package me.chanchangxing.selector.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.chanchangxing.picker.WheelView.OnWheelChangedListener;
import me.chanchangxing.picker.WheelView.WheelView;
import me.chanchangxing.picker.WheelView.adapters.ArrayWheelAdapter;
import me.chanchangxing.selector.R;
import me.chanchangxing.selector.db.Bean;
import me.chanchangxing.selector.db.DB;

public class SelectorFragment extends Fragment {

    public static final int PROVINCE = 0;
    public static final int CITY = 1;
    public static final int DISTRICT = 2;

    private WheelView mProvince;
    private WheelView mCity;
    private WheelView mDistrict;

    private View mBackground;

    private List<Bean> mProvinceBeanList;
    private List<Bean> mCityBeanList;
    private List<Bean> mDistrictBeanList;

    private Bean mCurrentProvinceBean;
    private Bean mCurrentCityBean;
    private Bean mCurrentDistrictBean;

    private DB mDB;

    private OnMoveListener onMoveListener;
    private OnClickBgListener onClickBgListener;

    private String mProvinceId = "110000";
    private String mCityId = "110100";
    private String mDistrictId = "110101";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selector, container, false);

        mProvince = view.findViewById(R.id.province);
        mCity = view.findViewById(R.id.city);
        mDistrict = view.findViewById(R.id.district);
        mBackground = view.findViewById(R.id.background);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBgListener.onClickBg();
            }
        });

        mDB = new DB(getActivity());

        setProvinceAdapter(mProvinceId);
        setCityAdapter(mProvinceId, mCityId);
        setDistrictAdapter(mCityId, mDistrictId);

        setListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDB.close();
    }

    public void setCurrentItem(String... ids) {
        if (ids.length == 1) {
            mProvinceId = ids[0];
        }

        if (ids.length == 2) {
            mProvinceId = ids[0];
            mCityId = ids[1];
        }

        if (ids.length == 3) {
            mProvinceId = ids[0];
            mCityId = ids[1];
            mDistrictId = ids[2];
        }
    }

    public void setOnClickBgListener(OnClickBgListener onClickBgListener) {
        this.onClickBgListener = onClickBgListener;
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    private void setListener() {
        mProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mCurrentProvinceBean = mProvinceBeanList.get(newValue);
                String provinceId = mCurrentProvinceBean.getId();
                setCityAdapter(provinceId, 0);

                mCurrentCityBean = mDB.getFirstCity(provinceId);
                String cityId = mCurrentCityBean.getId();
                setDistrictAdapter(cityId, 0);

                mCurrentDistrictBean = mDistrictBeanList == null || mDistrictBeanList.isEmpty() ? null : mDistrictBeanList.get(0);

                addMoveListener();

            }
        });

        mCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mCurrentCityBean = mCityBeanList.get(newValue);
                String cityId = mCurrentCityBean.getId();
                setDistrictAdapter(cityId);

                mCurrentDistrictBean = mDistrictBeanList == null || mDistrictBeanList.isEmpty() ? null : mDistrictBeanList.get(0);

                addMoveListener();
            }
        });

        mDistrict.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mCurrentDistrictBean = mDistrictBeanList == null || mDistrictBeanList.isEmpty() ? null : mDistrictBeanList.get(newValue);

                addMoveListener();
            }
        });
    }

    private void addMoveListener() {
        if (onMoveListener != null) {
            onMoveListener.onMove(mCurrentProvinceBean, mCurrentCityBean, mCurrentDistrictBean);
        }
    }

    private void setCityAdapter(String provinceId, String cityId) {
        setCityAdapter(provinceId);
        mCity.setCurrentItem(findCurrentItem(mCityBeanList, cityId));
    }

    private void setCityAdapter(String provinceId, int currentItem) {
        setCityAdapter(provinceId);
        mCity.setCurrentItem(currentItem);
    }

    private void setDistrictAdapter(String cityId, String districtId) {
        setDistrictAdapter(cityId);
        mDistrict.setCurrentItem(findCurrentItem(mDistrictBeanList, districtId), true);
    }

    private void setDistrictAdapter(String cityId, int currentItem) {
        setDistrictAdapter(cityId);
        mDistrict.setCurrentItem(currentItem);
    }

    private void setProvinceAdapter(String provinceId) {
        mProvinceBeanList = mDB.getProvinceBean();
        ArrayWheelAdapter<String> provinceAdapter = new ArrayWheelAdapter<>(getActivity(), mDB.getProvinceFullName());
        mProvince.setViewAdapter(provinceAdapter);

        mProvince.setCurrentItem(findCurrentItem(mProvinceBeanList, provinceId));
    }

    private void setCityAdapter(String provinceId) {
        mCityBeanList = mDB.getCityBean(provinceId);

        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(getActivity(), mDB.getCityFullName(provinceId));
        mCity.setViewAdapter(adapter);
    }

    private void setDistrictAdapter(String cityId) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(getActivity(), mDB.getDistrictFullName(cityId));
        mDistrict.setViewAdapter(adapter);

        mDistrictBeanList = mDB.getDistrictBean(cityId);
    }

    private int findCurrentItem(List<Bean> list, String id) {
        int index = list.size() - 1;
        for (; !id.equals(list.get(index).getId()); index--) {
            if (index == 0) break;
        }

        return index;
    }
}
