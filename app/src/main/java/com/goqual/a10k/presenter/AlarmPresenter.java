package com.goqual.a10k.presenter;


import com.goqual.a10k.model.entity.Alarm;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface AlarmPresenter {
    void loadItems();
    void updateState(int position);
    void delete(int position);
    void add(Alarm item);
    void update(Alarm item);

    interface View<T> extends BasePresenterView<T>{
        void onSuccessDelete();
        void onFailDelete(int position);
        void onSuccess();
    }
}
