package com.goqual.a10k.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class Switch implements Cloneable{
    public int _connectionid;
    public int _bsid;
    public int btncount;
    public int seq;
    public Integer _absenceid;
    public Integer start_hour;
    public Integer start_min;
    public Integer end_hour;
    public Integer end_min;
    public boolean btn1;
    public boolean btn2;
    public boolean btn3;
    public String hw;
    public String fw;
    public String title;
    public String macaddr;
    public boolean isavailable;
    public boolean isadmin;
    public boolean mIsStateView;

    public String WIFI_SSID = "";
    public String WIFI_BSSID = "";
    public String WIFI_PASSWORD = "";
    public String WIFI_CAPABILITY = "";

    public Switch() {
    }

    public Switch(String title, int btncount, boolean btn1, boolean btn2, boolean btn3) {
        this.title = title;
        this.btncount = btncount;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
    }

    public CharSequence getStartTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, start_hour);
        calendar.set(Calendar.MINUTE, start_min);
        calendar.setTimeZone(TimeZone.getDefault());
        stringBuilder.append("ON ");
        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
        return stringBuilder;
    }

    public CharSequence getEndTimeString() {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, end_hour);
        calendar.set(Calendar.MINUTE, end_min);
        calendar.setTimeZone(TimeZone.getDefault());
        stringBuilder.append("OFF ");
        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
        return stringBuilder;
    }

    // BS
    private int AP_SERVER_PORT = 5050;

    public int get_connectionid() {
        return _connectionid;
    }

    public void set_connectionid(int _connectionid) {
        this._connectionid = _connectionid;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    public int getBtnCount() {
        return btncount;
    }

    public void setBtnCount(int btnCount) {
        this.btncount = btnCount;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isBtn1() {
        return btn1;
    }

    public void setBtn1(boolean btn1) {
        this.btn1 = btn1;
    }

    public boolean isBtn2() {
        return btn2;
    }

    public void setBtn2(boolean btn2) {
        this.btn2 = btn2;
    }

    public boolean isBtn3() {
        return btn3;
    }

    public void setBtn3(boolean btn3) {
        this.btn3 = btn3;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }

    public String getFw() {
        return fw;
    }

    public void setFw(String fw) {
        this.fw = fw;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public boolean isavailable() {
        return isavailable;
    }

    public void setIsavailable(boolean isavailable) {
        this.isavailable = isavailable;
    }

    public Integer get_absenceid() {
        return _absenceid;
    }

    public void set_absenceid(Integer _absenceid) {
        this._absenceid = _absenceid;
    }

    public boolean ismIsStateView() {
        return mIsStateView;
    }

    public void setmIsStateView(boolean mIsStateView) {
        this.mIsStateView = mIsStateView;
    }

    public String getWIFI_SSID() {
        return WIFI_SSID;
    }

    public void setWIFI_SSID(String WIFI_SSID) {
        this.WIFI_SSID = WIFI_SSID;
    }

    public String getWIFI_BSSID() {
        return WIFI_BSSID;
    }

    public void setWIFI_BSSID(String WIFI_BSSID) {
        this.WIFI_BSSID = WIFI_BSSID;
    }

    public String getWIFI_PASSWORD() {
        return WIFI_PASSWORD;
    }

    public void setWIFI_PASSWORD(String WIFI_PASSWORD) {
        this.WIFI_PASSWORD = WIFI_PASSWORD;
    }

    public String getWIFI_CAPABILITY() {
        return WIFI_CAPABILITY;
    }

    public void setWIFI_CAPABILITY(String WIFI_CAPABILITY) {
        this.WIFI_CAPABILITY = WIFI_CAPABILITY;
    }

    public int getAP_SERVER_PORT() {
        return AP_SERVER_PORT;
    }

    public void setAP_SERVER_PORT(int AP_SERVER_PORT) {
        this.AP_SERVER_PORT = AP_SERVER_PORT;
    }

    public int getBtncount() {
        return btncount;
    }

    public void setBtncount(int btncount) {
        this.btncount = btncount;
    }

    public boolean isadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    @Override
    public Switch clone() throws CloneNotSupportedException {
        Switch newSwitch = (Switch) super.clone();

        newSwitch.set_connectionid(this.get_connectionid());
        newSwitch.set_bsid(this.get_bsid());
        newSwitch.setBtnCount(this.getBtnCount());
        newSwitch.setSeq(this.getSeq());
        newSwitch.setBtn1(this.isBtn1());
        newSwitch.setBtn2(this.isBtn2());
        newSwitch.setBtn3(this.isBtn3());
        newSwitch.set_absenceid(this.get_absenceid());
        newSwitch.setHw(this.getHw());
        newSwitch.setFw(this.getFw());
        newSwitch.setTitle(this.getTitle());
        newSwitch.setMacaddr(this.getMacaddr());
        newSwitch.setIsavailable(this.isavailable());
        newSwitch.setmIsStateView(this.ismIsStateView());
        newSwitch.setWIFI_SSID(this.getWIFI_SSID());
        newSwitch.setWIFI_BSSID(this.getWIFI_BSSID());
        newSwitch.setWIFI_PASSWORD(this.getWIFI_PASSWORD());
        newSwitch.setWIFI_CAPABILITY(this.getWIFI_CAPABILITY());
        newSwitch.setIsadmin(this.isadmin());
        return newSwitch;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
