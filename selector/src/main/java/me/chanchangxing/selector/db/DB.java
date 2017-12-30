package me.chanchangxing.selector.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchangxing on 2017/12/24.
 */

public class DB extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase db;
    private static final String mName = "city_select";
    private static final int mVersion = 1;

    private static final String ID = "_id";
    private static final String PARENT_ID = "ParentId";
    private static final String FULL_NAME = "FullName";
    private static final String LEVEL = "Level";

    public DB(Context context) {
        super(context, mName, null, mVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        executeAssetsSQL(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Bean> getProvinceBean() {
        String sql = "select * from district where Level = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{"1"});

        List<Bean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(getBean(cursor));
        }

        cursor.close();
        return list;
    }

    public List<Bean> getCityBean(String provinceId) {
        String sql = "select * from district where ParentId = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{provinceId});

        List<Bean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(getBean(cursor));
        }
        cursor.close();
        return list;
    }

    public List<Bean> getDistrictBean(String cityId) {
        String sql = "select * from district where ParentId = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{cityId});

        List<Bean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(getBean(cursor));
        }
        cursor.close();
        return list;
    }

    public String[] getProvinceFullName() {
        String sql = "select FullName from district where Level = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{"1"});

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow(FULL_NAME)));
        }
        cursor.close();
        return list.toArray(new String[]{});
    }

    public String[] getCityFullName(String provinceId) {
        String sql = "select FullName from district where ParentId = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{provinceId});

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow(FULL_NAME)));
        }
        cursor.close();
        return list.toArray(new String[]{});
    }

    public String[] getDistrictFullName(String cityId) {
        String sql = "select FullName from district where ParentId = ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{cityId});

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndexOrThrow(FULL_NAME)));
        }
        cursor.close();
        return list.toArray(new String[]{});
    }

    public Bean getFirstCity(String provinceId) {
        String sql = "select * from district where ParentId = ? limit 0,1";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{provinceId});

        List<Bean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(getBean(cursor));
        }
        return list.get(0);
    }

    private Bean getBean(Cursor cursor) {
        Bean bean = new Bean();
        bean.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
        bean.setParentId(cursor.getString(cursor.getColumnIndexOrThrow(PARENT_ID)));
        bean.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(FULL_NAME)));
        bean.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(LEVEL)));
        return bean;
    }

    public int getProvinceWithId(String id) {
        String sql = "select _id from district where Level = ?";
//        Cursor cursor = this.getReadableDatabase().rawQuery(sql, new String[]{"1"});
//        int index = 0;
//        while (cursor.moveToNext()) {
//            Log.e("city", cursor.getString(cursor.getColumnIndex(ID)));
//            if (id.equals(cursor.getString(cursor.getColumnIndex(ID)))) {
//                cursor.close();
//                return index;
//            }
//            index++;
//        }

//        cursor.close();
        return -1;
    }

    private void executeAssetsSQL(SQLiteDatabase db) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            context.getAssets().open("district.sql")));

            String buffer = "";
            String str;
            while ((str = br.readLine()) != null) {
                buffer += str;
                if (str.trim().endsWith(";")) {
                    db.execSQL(buffer.toString());
                    buffer = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
