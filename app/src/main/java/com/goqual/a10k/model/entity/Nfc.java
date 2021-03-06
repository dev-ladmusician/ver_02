package com.goqual.a10k.model.entity;

import com.goqual.a10k.model.realm.NfcRealm;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by hanwool on 2017. 3. 8..
 */

public class Nfc implements BaseRealmWraper<NfcRealm>{
    public String tag;
    public int _nfcid;
    public int _bsid;
    public boolean btn1;
    public boolean btn2;
    public boolean btn3;
    public String title;
    public String macaddr;
    public int state;
    public int btncount;
    public boolean mIsDeletable;
    public boolean isEditing;

    public Nfc() {
        this._nfcid = 0;
    }

    public Nfc(int _bsid, boolean btn1, boolean btn2, boolean btn3, String tag, String title) {
        this._bsid = _bsid;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.tag = tag;
        this.title = title;
    }

    public Nfc(NfcRealm nfcItem) {
        tag = nfcItem.getTag();
        _nfcid = nfcItem.get_nfcid();
        _bsid = nfcItem.get_bsid();
        btn1 = nfcItem.getBtn1();
        btn2 = nfcItem.getBtn2();
        btn3 = nfcItem.getBtn3();
        title = nfcItem.getTitle();
        macaddr = nfcItem.getMacaddr();
        state = nfcItem.getState();
        btncount = nfcItem.getBtnCount();
        mIsDeletable = nfcItem.ismIsDeletable();
        isEditing = false;
    }

    @Override
    public NfcRealm getRealmObject() {
        return new NfcRealm(_nfcid, _bsid, btn1, btn2, btn3, title, tag, macaddr, state, btncount, mIsDeletable);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public boolean getBtn1() {
        return btn1;
    }

    public void setBtn1(boolean btn1) {
        this.btn1 = btn1;
    }

    public boolean getBtn2() {
        return btn2;
    }

    public void setBtn2(boolean btn2) {
        this.btn2 = btn2;
    }

    public boolean getBtn3() {
        return btn3;
    }

    public void setBtn3(boolean btn3) {
        this.btn3 = btn3;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBtncount() {
        return btncount;
    }

    public void setBtncount(int btncount) {
        this.btncount = btncount;
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
