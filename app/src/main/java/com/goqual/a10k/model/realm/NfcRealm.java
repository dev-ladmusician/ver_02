package com.goqual.a10k.model.realm;

import com.goqual.a10k.model.entity.Nfc;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class NfcRealm extends RealmObject{

    @Index
    public String tag;
    @Index
    public int _nfcid;
    public int _bsid;
    public Boolean btn1;
    public Boolean btn2;
    public Boolean btn3;
    public String title;
    public String macaddr;
    public int state;
    public int btnCount;
    public boolean mIsDeletable;
    @Ignore
    public boolean isEditing;

    public NfcRealm() {
    }

    public NfcRealm(Nfc nfcItem) {
        tag = nfcItem.getTag();
        _nfcid = nfcItem.get_nfcid();
        _bsid = nfcItem.get_bsid();
        btn1 = nfcItem.getBtn1();
        btn2 = nfcItem.getBtn2();
        btn3 = nfcItem.getBtn3();
        title = nfcItem.getTitle();
        macaddr = nfcItem.getMacaddr();
        state = nfcItem.getState();
        btnCount = nfcItem.getBtncount();
        mIsDeletable = nfcItem.ismIsDeletable();
        isEditing = false;
    }

    public NfcRealm(int _nfcid, int _bsid, Boolean btn1, Boolean btn2, Boolean btn3, String title, String tag, String macaddr, int state, int btnCount, boolean mIsDeletable) {
        this._nfcid = _nfcid;
        this._bsid = _bsid;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.title = title;
        this.tag = tag;
        this.macaddr = macaddr;
        this.state = state;
        this.btnCount = btnCount;
        this.mIsDeletable = mIsDeletable;
    }

    public int get_nfcid() {
        return _nfcid;
    }

    public void set_nfcid(int _nfcid) {
        this._nfcid = _nfcid;
    }

    public int get_bsid() {
        return _bsid;
    }

    public void set_bsid(int _bsid) {
        this._bsid = _bsid;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn1() {
        return btn1;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn1 스위치 동작
     */
    public void setBtn1(Boolean btn1) {
        this.btn1 = btn1;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn2() {
        return btn2;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn2 스위치 동작
     */
    public void setBtn2(Boolean btn2) {
        this.btn2 = btn2;
    }

    /**
     * 설정될 동작의 반대값(켜지면 false == 현재 스테이트)
     * @return 스위치 동작
     */
    public Boolean getBtn3() {
        return btn3;
    }

    /**
     * 하고자 하는 것의 반대값(켜고싶으면 false == 현재 스테이트)
     * @param btn3 스위치 동작
     */
    public void setBtn3(Boolean btn3) {
        this.btn3 = btn3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBtnCount() {
        return btnCount;
    }

    public void setBtnCount(int btnCount) {
        this.btnCount = btnCount;
    }

    public boolean ismIsDeletable() {
        return mIsDeletable;
    }

    public void setmIsDeletable(boolean mIsDeletable) {
        this.mIsDeletable = mIsDeletable;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
