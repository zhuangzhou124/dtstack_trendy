/*
 * ZhuangZhou
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.trendy.udf;


import com.aliyun.odps.udf.UDF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author ZhuangZhou
 * @version $Id: SalesWeeks.java, v 0.1 2019年10月10日 7:41 PM ZhuangZhou Exp $
 */
public class SalesWeeks extends UDF{

    //销售周号
    public int salesWeeksNo;

    public int evaluate(String date) {
        if(date.equals(null) || date.length()==0 || date == ""){
            salesWeeksNo = 0;
            return salesWeeksNo;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sd = sdf.parse(date);

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String sd2 = sdf2.format(sd);
            //今天
            LocalDate today = LocalDate.parse(sd2);
            //当月每个周日集合
            List<LocalDate> weekends = getSundayOfMonth(sd);
            //获取第一个周末是当月第几天
            int days = weekends.get(0).getDayOfMonth();
            //获取本月周数
            int weeks = weekends.size();

            if(days>=5){
                    if(weekends.get(0).compareTo(today)>=0){
                        salesWeeksNo = 1;
                    }else if(weekends.get(0).compareTo(today) < 0 && weekends.get(1).compareTo(today) >= 0){
                        salesWeeksNo = 2;
                    }else if(weekends.get(1).compareTo(today) < 0 && weekends.get(2).compareTo(today) >= 0){
                        salesWeeksNo = 3;
                    }else {
                        salesWeeksNo = 4;
                    }
            }else {
                if (weekends.get(1).compareTo(today) >= 0) {
                    salesWeeksNo = 1;
                } else if (weekends.get(1).compareTo(today) < 0 && weekends.get(2).compareTo(today) >= 0) {
                    salesWeeksNo = 2;
                } else if (weekends.get(2).compareTo(today) < 0 && weekends.get(3).compareTo(today) >= 0) {
                    salesWeeksNo = 3;
                } else {
                    salesWeeksNo = 4;
                }

            }

        }catch (ParseException e){
            e.printStackTrace();
        }

        return salesWeeksNo;
    }




    /**
     * 获取当月每一个周末
     *
     * @param date 日期
     * @return
     */


    public List<LocalDate> getSundayOfMonth(Date date) {

        List<LocalDate> weekends = new ArrayList<LocalDate>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String sd2 = sdf2.format(date);
        LocalDate today = LocalDate.parse(sd2);
        //本月的第一天
        LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
        //本月的最后一天
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        //最后一天的那天是本月的第几周
        int weeks = lastDay.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        int i = 1;
        //第一周的星期天
        LocalDate sunday = firstday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        while (i < weeks) {
            //System.out.println("第" + i + "周的周末是:" + sunday + " ");
            //sunday.minus()
            weekends.add(sunday);
            sunday = sunday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            i++;
        }
        return weekends;
    }




/*
    public static void main(String[] args) throws ParseException {

        System.out.println(evaluate("2020-04-21 12:00:00"));
        System.out.println(evaluate("2020-04-22 12:00:00"));
        System.out.println(evaluate("2020-04-23 12:00:00"));
        System.out.println(evaluate("2020-04-24 12:00:00"));
        System.out.println(evaluate("2020-04-25 12:00:00"));
        System.out.println(evaluate("2020-04-26 12:00:00"));
        System.out.println(evaluate("2020-04-27 12:00:00"));
        System.out.println(evaluate("2020-04-28 12:00:00"));
        System.out.println(evaluate("2020-04-29 12:00:00"));
        System.out.println(evaluate("2020-04-30 12:00:00"));
        System.out.println(evaluate("2020-04-11 12:00:00"));
        System.out.println(evaluate("2020-04-12 12:00:00"));
        System.out.println(evaluate("2020-04-13 12:00:00"));
        System.out.println(evaluate("2020-04-14 12:00:00"));
        System.out.println(evaluate("2020-04-15 12:00:00"));
        System.out.println(evaluate("2020-04-16 12:00:00"));
        System.out.println(evaluate("2020-04-17 12:00:00"));
        System.out.println(evaluate("2020-04-18 12:00:00"));
        System.out.println(evaluate("2020-04-19 12:00:00"));
        System.out.println(evaluate("2020-04-20 12:00:00"));
        System.out.println(evaluate("2020-04-10 12:00:00"));

    }*/


}