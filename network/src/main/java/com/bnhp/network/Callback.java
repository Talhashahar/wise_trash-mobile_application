package com.bnhp.network;


import com.bnhp.network.baseresponses.BaseResponse;
import com.bnhp.network.calladaper.errorhandling.TrashWiseExaption;

public interface Callback<T extends BaseResponse> {
    void onSuccess(T t);

    void onError(TrashWiseExaption e);

    void onEmptyResponse(TrashWiseExaption e);
}
