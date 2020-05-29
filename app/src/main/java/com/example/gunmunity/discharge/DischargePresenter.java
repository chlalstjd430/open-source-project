package com.example.gunmunity.discharge;

import android.view.View;

import com.example.gunmunity.discharge.DischargeContract;
import com.example.gunmunity.discharge.DischargeModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DischargePresenter implements DischargeContract.Presenter {

    private DischargeContract.View view;
    private DischargeModel model;

    @Override
    public void setView(DischargeContract.View view) {
        this.view = view;
    }

    @Override
    public void createModel() {
        model = new DischargeModel();
    }

    @Override
    public void loaditem() {
        try {
            load_data();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load_data()  throws Exception{
        FileInputStream fis = null;
        BufferedReader buf = null;
        int i;
        String data[] = new String[3];

        fis = ((View)view).getContext().openFileInput("discharge.dat");
        buf = new BufferedReader(new InputStreamReader(fis));
        for(i=0;i<3;i++){
            String str = buf.readLine();
            data[i] = str;
        }

        Setting_data(data);
        view.updateView(model);
    }

    private void Setting_data(String[] data) {
        int enli_year, enli_month, enli_day;
        String[] discharge_day = new String[3];
        final String[] class_flag = {"이병","일병","상병","병장"};
        String _class_text = null;
        String remain_class = null;
        String discharge_year,discharge_month, discharge_date;
        enli_year = Integer.valueOf(discharge_day[0]);
        enli_month = Integer.valueOf(discharge_day[1])-1;
        enli_day = Integer.valueOf(discharge_day[2]);

        Calendar cal_enli = new GregorianCalendar(enli_year,enli_month,enli_day);
        Calendar cal_discharge = new GregorianCalendar(enli_year,enli_month,enli_day);
        Calendar fundament = new GregorianCalendar(2018,Calendar.OCTOBER,1);
        Calendar[] _class = {new GregorianCalendar(enli_year,enli_month,enli_day),new GregorianCalendar(enli_year,enli_month,enli_day),new GregorianCalendar(enli_year,enli_month,enli_day),new GregorianCalendar(enli_year,enli_month,enli_day)};
        switch("army"){
            case "army":
                cal_discharge.add(Calendar.MONTH, 21);
                cal_discharge.add(Calendar.DATE, -1);
                _class[1].add(Calendar.MONTH, 2);
                _class[1].set(Calendar.DATE, 1);
                _class[2].add(Calendar.MONTH, 8);
                _class[2].set(Calendar.DATE, 1);
                _class[3].add(Calendar.MONTH, 14);
                _class[3].set(Calendar.DATE, 1);
                break;
            case "marine":
                cal_discharge.add(Calendar.MONTH, 21);
                cal_discharge.add(Calendar.DATE, -1);
                break;
            case "naby":
                cal_discharge.add(Calendar.MONTH, 23);
                cal_discharge.add(Calendar.DATE, -1);
                break;
            case "airforce":
                cal_discharge.add(Calendar.MONTH, 24);
                cal_discharge.add(Calendar.DATE, -1);
                break;
        }
        Date dischrage = cal_discharge.getTime();
        Date funda = fundament.getTime();
        Date enli = cal_enli.getTime();
        Calendar cur = new GregorianCalendar();
        Date cur1 = cur.getTime();

        long shorten = (dischrage.getTime() - funda.getTime() >= 0) ?(((dischrage.getTime() - funda.getTime()) / 14)/(24*60*60*1000)) + 1: 0;
        cal_discharge.add(Calendar.DATE, (int) (-1 * shorten));
        dischrage = cal_discharge.getTime();
        long allday = (dischrage.getTime()-enli.getTime())/(24*60*60*1000);
        long difference =(cur.getTime().getTime() - enli.getTime())/(24*60*60*1000);
        int dif_month = (int) (cal_discharge.get(Calendar.MONTH) - cur.get(Calendar.MONTH));
        int dif_day = (int) (cal_discharge.get(Calendar.DATE) - cur.get(Calendar.DATE));
        double percentage = (double)difference / (double)allday;


        SimpleDateFormat year_format = new SimpleDateFormat("yyyy");
        SimpleDateFormat month_format = new SimpleDateFormat("MM");
        SimpleDateFormat day_format = new SimpleDateFormat("dd");
        discharge_year = year_format.format(dischrage);
        discharge_month = month_format.format(dischrage);
        discharge_date = day_format.format(dischrage);

        if(dif_day < 0) {
            --dif_month;
            dif_day += cur.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if(dif_month < 0) {
            dif_month += 12;
        }

        for(int i=2;i<5;i++) {

            if(cur.getTime().getTime() < _class[i-1].getTime().getTime()) {
                _class_text = class_flag[i-2] +" "+(Math.abs(_class[i-2].get(Calendar.MONTH)-cur.get(Calendar.MONTH))+1) + "호봉";
                remain_class = ""+(_class[i-1].getTime().getTime() - cur.getTime().getTime())/(24*60*60*1000);
                break;
            }
        }

        if(_class_text == null) {
            _class_text = class_flag[3] +" "+(( _class[3].get(Calendar.MONTH)- cur.get(Calendar.MONTH))+1) + "호봉";
            remain_class = ""+(allday - difference);
        }

        model.setMon_text1(String.format("%02d",dif_month).substring(0,1));
        model.setMon_text2(String.format("%02d",dif_month).substring(1));
        model.setRemain_text1(String.format("%02d",dif_day).substring(0,1));
        model.setRemain_text1(String.format("%02d",dif_day).substring(1));
        model.setDischargeDay(discharge_year+"년 "+discharge_month+"월 "+discharge_date+"일");
        model.set_dischargeDay(discharge_year+"."+discharge_month+"."+discharge_date);
        model.setEnlistmentDay(discharge_day[0]+"."+discharge_day[1]+"."+discharge_day[2]);
        model.setSpecialist(""+percentage);
        model.setService_text(""+difference);
        model.setClass_text(_class_text);
        model.setRemain_service_text(""+(allday - difference));
        model.setCurclass(_class_text.substring(0, 2));
        model.setPromotion_text(remain_class);

    }
}