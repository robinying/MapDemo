package com.robin.commonUi.customview.textview;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import androidx.annotation.IntRange;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AutoCompleteTextViewExtend extends AppCompatAutoCompleteTextView {

    private Set<String> set = new LinkedHashSet<>();
    private List<String> list = new ArrayList<>();
    private ArrayAdapter adapter;

    private SharedPreferences sp;
    private int saveCount = 10;

    public AutoCompleteTextViewExtend(Context context) {
        super(context);
        init(context);
    }

    public AutoCompleteTextViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCompleteTextViewExtend(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        sp = context.getSharedPreferences(getId() + "", Context.MODE_PRIVATE);
        set.addAll(sp.getStringSet(getId() + "", set));
        list.addAll(set);
        setAdapter(adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list));
    }

    //******************************************对外开放接口***************************************************

    /**
     * 获得已保存的历史记录
     *
     * @return
     */
    public List<String> getRecordDatas() {
        return list;
    }

    /**
     * 设置保存的条数，默认10条
     *
     * @param count
     */
    public void setSaveCount(@IntRange(from = 0) int count) {
        this.saveCount = count;
    }

    /**
     * 保存记录
     *
     * @param record
     * @return
     */
    public boolean saveRecord(String record) {
        if (!TextUtils.isEmpty(record)) {
            int oldSize = set.size();
            set.add(record);
            int newSize = set.size();
            if (newSize != oldSize && set.size() > saveCount) {
                set.remove(set.toArray()[0]);
            }
            list.clear();
            list.addAll(set);
            setAdapter(adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list));
            SharedPreferences.Editor editor = sp.edit();
            editor.putStringSet(getId() + "", set);
            editor.commit();
            return true;
        }
        return false;
    }

    /**
     * 保存当前记录，即保存当前显示文本内容
     *
     * @return
     */
    public boolean saveRecord() {
        String item = getText().toString();
        return saveRecord(item);
    }

    /**
     * 删除指定历史记录
     *
     * @param record
     */
    public boolean deleteRecord(String record) {
        if (TextUtils.isEmpty(record)) {
            return false;
        }
        if (set.contains(record)) {
            set.remove(record);
            list.clear();
            list.addAll(set);
            setAdapter(adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list));
            SharedPreferences.Editor editor = sp.edit();
            editor.putStringSet(getId() + "", set);
            editor.commit();
            return true;

        }
        return false;
    }

    /**
     * 清除保存记录
     */
    public void clearAllRecord() {
        list.clear();
        set.clear();
        setAdapter(adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list));
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getId() + "");
        editor.commit();
    }

}
