package com.goqual.a10k.model.remote.service;

import android.content.Context;

import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.RetrofitManager;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class SwitchService {
    private SwitchApi mSwitchApi = null;

    public SwitchService(Context ctx) {
        mSwitchApi =
                RetrofitManager.getInstance()
                        .getRetrofitBuilder(ctx).create(SwitchApi.class);
    }

    public SwitchApi getSwitchApi() {
        return mSwitchApi;
    }

    public interface SwitchApi {
        @GET("switch/{macaddr}")
        Observable<ResultDTO<Switch>> get(
                @Path("macaddr") String macaddr
        );

        @GET("switch/bsid/{_bsid}")
        Observable<ResultDTO<Switch>> getSwitchByBsid(
                @Path("_bsid") int _bsid
        );

        @GET("connection")
        Observable<ResultDTO<List<Switch>>> gets(
        );

        @GET("connection/{page}")
        Observable<PagenationWrapper<Switch>> gets(
                @Path("page") int page
        );

        @FormUrlEncoded
        @HTTP(path = "connection", method = "DELETE", hasBody = true)
        Observable<ResultDTO<Switch>> delete(
                @Field("connectionId") int connectionId
        );

        @FormUrlEncoded
        @PUT("connection")
        Observable<ResultDTO<Switch>> rename(
                @Field("connectionId") int connectionId,
                @Field("title") String title
        );

        @FormUrlEncoded
        @PUT("connection")
        Observable<ResultDTO<Switch>> changeSeq(
                @Field("connectionIds") String connectionIds,
                @Field("connectionSeqs") String connectionSeqs
        );

        @FormUrlEncoded
        @POST("connection")
        Observable<ResultDTO<Switch>> add(
                @Field("switchMacAddr") String macAddr,
                @Field("title") String title,
                @Field("count") int count
        );
    }
}
