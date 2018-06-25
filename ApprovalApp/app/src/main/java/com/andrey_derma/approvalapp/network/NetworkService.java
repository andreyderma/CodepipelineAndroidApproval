package com.andrey_derma.approvalapp.network;

import com.andrey_derma.approvalapp.model.ApprovalRequest;
import com.andrey_derma.approvalapp.model.ApprovalResponse;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetworkService {
    @POST("production/deployecs")
    Flowable<ApprovalResponse> setApprovalRequestData(@Body ApprovalRequest approvalRequest);
}
