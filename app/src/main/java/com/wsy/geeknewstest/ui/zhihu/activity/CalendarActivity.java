package com.wsy.geeknewstest.ui.zhihu.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.util.DateUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2016/11/23.
 */

public class CalendarActivity extends SimpleActivity {

    @BindView(R.id.view_calender)
    MaterialCalendarView mCalender;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    CalendarDay mDate;

    @Override
    protected int getLayout() {
        return R.layout.activity_calender;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "选择日期");
        mCalender.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2015, 5, 20))
                .setMaximumDate(CalendarDay.from(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        //发生更改时
        mCalender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mDate = date;
            }
        });
    }

    @OnClick(R.id.tv_calender_enter)
    void chooseDate() {
        RxBus.getDefault().post(mDate);
        finish();
    }

}
