package com.android.base.widgets.wheelview.use;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.base.R;
import com.android.base.widgets.wheelview.LoopItemSelectedListener;
import com.android.base.widgets.wheelview.LoopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeSelectorUtils {

    private PopupWindow birthPop;
    private View popView;
    private View blankView;
    private boolean outsideTouchable = false;


    private int minStartYear;
    private int minStartMonth;
    private int minStartDay;
    private int minStartHour;
    private int minStartMinute;

    private int defaultStartYear;
    private int defaultStartMonth;
    private int defaultStartDay;
    private int defaultStartHour;
    private int defaultStartMinute;
    private int currentYear;

    private int endYear = 0;

    private TextView tv_cancle;
    private LoopView yearLoopView;
    private LoopView dayLoopView;
    private LoopView monthLoopView;
    private LoopView hourLoopView;
    private LoopView minuteLoopView;
    private TextView tv_sure;
    private ArrayList<String> yearList;
    private ArrayList<String> monthList;
    private ArrayList<String> dayList;
    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private int minInterval = 5;

    private Context context;
    private String resultFormat = "yyyy-MM-dd HH:mm:ss";

    public TimeSelectorUtils(Context context, YMDCallBack ymdCallBack) {
        this(context, "yyyy-MM-dd HH:mm:ss", new Date(), ymdCallBack);
    }



    public TimeSelectorUtils(Context context, String resultFormat, YMDCallBack ymdCallBack) {
        this(context, resultFormat, new Date(), ymdCallBack);
    }

    public TimeSelectorUtils(Context context, Date defaultDate, YMDCallBack ymdCallBack) {
        this(context, "yyyy-MM-dd HH:mm:ss", defaultDate, ymdCallBack);
    }

    public TimeSelectorUtils(Context context, String resultFormat, Date defaultDate, YMDCallBack ymdCallBack) {
        this(context, new Date(), defaultDate, resultFormat, ymdCallBack);
    }

    public TimeSelectorUtils(Context context, Date minDate, Date defaultDate, String resultFormat, YMDCallBack ymdCallBack) {
        this(context, minDate, defaultDate, 0, resultFormat, ymdCallBack);
    }

    public TimeSelectorUtils(Context context, Date minDate, Date defaultDate, int endYear, String resultFormat, YMDCallBack ymdCallBack) {
        this(context, minDate, defaultDate, endYear, resultFormat, 5, ymdCallBack);
    }


    public TimeSelectorUtils(Context context, Date minDate, Date defaultDate, int endYear, String resultFormat, int minInterval, YMDCallBack ymdCallBack) {
        this.context = context;
        this.resultFormat = resultFormat;
        this.ymdCallBack = ymdCallBack;
        this.minInterval = minInterval;
        Calendar calendar = Calendar.getInstance();
        if (endYear == 0) {
            this.endYear = calendar.get(Calendar.YEAR) + 1;
        } else {
            this.endYear = endYear;
        }
        calendar.setTime(minDate);
        minStartYear = calendar.get(Calendar.YEAR);
        minStartMonth = calendar.get(Calendar.MONTH);
        minStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        minStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        minStartMinute = calendar.get(Calendar.MINUTE);
        calendar.setTime(defaultDate);
        currentYear = defaultStartYear = calendar.get(Calendar.YEAR);
        defaultStartMonth = calendar.get(Calendar.MONTH);
        defaultStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        defaultStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        defaultStartMinute = calendar.get(Calendar.MINUTE);
        initYmdPop();
    }

    private void initYmdPop() {
        popView = LayoutInflater.from(context).inflate(R.layout.pop_ymdpicker, null);
        blankView = popView.findViewById(R.id.view_cancle);
        tv_cancle = popView.findViewById(R.id.tv_cancle);
        yearLoopView = popView.findViewById(R.id.loop_year);
        monthLoopView = popView.findViewById(R.id.loop_month);
        dayLoopView = popView.findViewById(R.id.loop_day);
        hourLoopView = popView.findViewById(R.id.loop_hour);
        minuteLoopView = popView.findViewById(R.id.loop_minute);
        tv_sure = popView.findViewById(R.id.tv_sure);

        yearList = new ArrayList<>();
        monthList = new ArrayList<>();
        dayList = new ArrayList<>();
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();
        
        for (int i = minStartYear; i <= endYear; i++) {
            yearList.add(i + "");
        }
        yearLoopView.setItems(yearList);

        
        for (int i = minStartMonth; i <= 11; i++) {
            monthList.add((i < 9 ? "0" : "") + (i + 1) + "Month");
        }
        monthLoopView.setItems(monthList);
        setDaysList(currentYear, minStartMonth);

        
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hourList.add("0" + i + "hours");
            } else {
                hourList.add(i + "hours");
            }
        }
        hourLoopView.setItems(hourList);

        
        for (int i = 0; i < 60; i++) {
            if (i % minInterval == 0) {
                if (i < 10) {
                    minuteList.add("0" + i + "Minutes");
                } else {
                    minuteList.add(i + "Minutes");
                }
            }
        }
        minuteLoopView.setItems(minuteList);

        yearLoopView.setListener(new LoopItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int currentMonth;
                currentYear = minStartYear + index;
                monthList.clear();
                

                for (int i = index == 0 ? minStartMonth : 0; i <= 11; i++) {
                    monthList.add((i < 9 ? "0" : "") + (i + 1) + "Month");
                }
                monthLoopView.setItems(monthList);
                currentMonth = minStartMonth + monthLoopView.getSelectedItem();
                setDaysList(currentYear, currentMonth);
            }
        });
        monthLoopView.setListener(new LoopItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int currentMonth;
                if (currentYear == minStartYear) {
                    currentMonth = minStartMonth + index;
                } else {
                    currentMonth = index;
                }
                setDaysList(currentYear, currentMonth);
            }
        });

        yearLoopView.setInitPosition(yearList.indexOf(defaultStartYear + ""));
        monthLoopView.setInitPosition(monthList.indexOf((defaultStartMonth < 9 ? "0" : "") + (defaultStartMonth + 1) + "Month"));
        dayLoopView.setInitPosition(dayList.indexOf((defaultStartDay < 10 ? "0" : "") + defaultStartDay + "Day"));
        hourLoopView.setInitPosition(hourList.indexOf((defaultStartHour < 10 ? "0" : "") + defaultStartHour + "Hour"));
        minuteLoopView.setInitPosition(minuteList.indexOf((defaultStartMinute < 10 ? "0" : "") + defaultStartMinute + "Minute"));

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDay = yearList.get(yearLoopView.getSelectedItem()) + "Year"
                        + monthList.get(monthLoopView.getSelectedItem())
                        + dayList.get(dayLoopView.getSelectedItem())
                        + hourList.get(hourLoopView.getSelectedItem())
                        + minuteList.get(minuteLoopView.getSelectedItem());
                if (null != ymdCallBack) {
                    try {
                        birthDay = birthDay.replace("Year", "-")
                                .replace("Month", "-")
                                .replace("Day", " ")
                                .replace("Hour", ":")
                                .replace("Minutes", ":");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date = sdf.parse(birthDay);
                        SimpleDateFormat sdf1 = new SimpleDateFormat(resultFormat);
                        ymdCallBack.setDate(birthPop, sdf1.format(date));
                    } catch (Exception e) {
                        e.printStackTrace();
                        ymdCallBack.setDate(birthPop, e.getMessage());
                    }
                }
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthPop.dismiss();
                ymdCallBack.onCancel();
            }
        });
        blankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outsideTouchable)
                    birthPop.dismiss();
            }
        });
    }

    public void setOutsideTouchable(boolean outsideTouchable) {
        this.outsideTouchable = outsideTouchable;
    }

    public PopupWindow getYmdPop() {
        birthPop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        birthPop.setBackgroundDrawable(new BitmapDrawable());
        birthPop.setFocusable(true);
        birthPop.setOutsideTouchable(true);
        birthPop.setAnimationStyle(R.style.PopWDInAndOutStyle);
        birthPop.update();
        return birthPop;
    }


    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {

            if (year % 100 == 0 && year % 400 != 0) {
                return false;
            }

            else {
                return true;
            }
        } else {
            return false;
        }
    }


    private int getDays(boolean isLeapYear, int month) {
        int days = 0;
        switch (month) {
            case 0:
                days = 31;
                break;
            case 1:
                if (isLeapYear) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            case 2:
                days = 31;
                break;
            case 3:
                days = 30;
                break;
            case 4:
                days = 31;
                break;
            case 5:
                days = 30;
                break;
            case 6:
                days = 31;
                break;
            case 7:
                days = 31;
                break;
            case 8:
                days = 30;
                break;
            case 9:
                days = 31;
                break;
            case 10:
                days = 30;
                break;
            case 11:
                days = 31;
                break;
        }
        return days;
    }


    private void setDaysList(int currentYear, int currentMonth) {
        ArrayList<String> cacheList = new ArrayList<>();
        int totalDays = 0;
        if (isLeapYear(currentYear)) {
            totalDays = getDays(true, currentMonth);
        } else {
            totalDays = getDays(false, currentMonth);
        }
        for (int i = (minStartYear == currentYear && minStartMonth == currentMonth) ? minStartDay : 1; i <= totalDays; i++) {
            cacheList.add((i < 9 ? "0" : "") + i + "Day");
        }
        dayList.clear();
        dayList.addAll(cacheList);
        dayLoopView.setItems(dayList);
    }

    private YMDCallBack ymdCallBack;

    public interface YMDCallBack {
        void setDate(PopupWindow popupWindow, String date);
        void onCancel();
    }


}
