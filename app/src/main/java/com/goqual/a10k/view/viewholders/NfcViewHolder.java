package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemNfcBinding;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.customviews.CustomViewBtnStates;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class NfcViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemNfcBinding binding;

    private int mPosition;
    private CustomViewBtnStates mBtnStates;
    private OnRecyclerItemClickListener mListener;

    public NfcViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, Nfc item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setItemNfc(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
