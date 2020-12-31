package com.an2t.android.bouponapp.services;

import com.an2t.android.bouponapp.field_area.model.MainAreaName;
import com.an2t.android.bouponapp.forgot_password.model.ChangePassword;
import com.an2t.android.bouponapp.forgot_password.model.ForgetPassword;
import com.an2t.android.bouponapp.forgot_password.model.OTPModel;
import com.an2t.android.bouponapp.login.model.Login;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.an2t.android.bouponapp.main.model.RechargeHistory;
import com.an2t.android.bouponapp.main.model.VersionCodeRes;
import com.an2t.android.bouponapp.my_coupon.model.CouponCall;
import com.an2t.android.bouponapp.my_coupon.model.ReadBranchGiftData;
import com.an2t.android.bouponapp.my_coupon.model.RedeemCoupon;
import com.an2t.android.bouponapp.my_coupon.model.SendGift;
import com.an2t.android.bouponapp.my_coupon.model.ShortenUrl;
import com.an2t.android.bouponapp.my_coupon.model.TransferCouponRes;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.recharge.model.InitiateRecharge;
import com.an2t.android.bouponapp.register.model.Register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by anantawasthy on 5/19/17.
 */

public interface ServicesAPI {

    public static final String AUTH_KEY = "Authorization";
//
    @POST("mobileLoginPhoneNumber")
    Call<Login.LoginRes> sendLoginCredentials(@Body Login login);
//
    @POST("mobileRegister")
    Call<Register.RegisterRes> sendRegisterData(@Body Register register);
//
    @POST("mobileVerifyPhoneNumber")
    Call<OTPModel.OTPModelRes> getOTPRes(@Body OTPModel otpModel);
//
    @POST("mobileForgotPassword")
    Call<ForgetPassword.ForgetPasswrdRes> forgetPasswordPageOne(@Body ForgetPassword forgetPasswrdRes);
//
    @POST("mobileResetPassword")
    Call<ChangePassword.ChangePasswordRes> forgetPasswordPageTwo(@Body ChangePassword changePassword);


    @POST("initiaterecharge")
    Call<InitiateRecharge.InitiateRechargeRes> getRechargeRes(@Body InitiateRecharge initiateRecharge, @Header(AUTH_KEY) String auth_token);

    @POST("home/freedeals")
    Call<FreeDealsRes> getAllFreeDealsForRecharge(@Header(AUTH_KEY) String auth_token);

    @POST("home/getHomeData")
    Call<GetSpotlightImages> getAllImages(@Header(AUTH_KEY) String auth_token);

    @POST("rechargehome")
    Call<RechargeHistory> getHistory(@Header(AUTH_KEY) String authToken, @Body RechargeHistory.RechargeHistorySend sendData);

    @POST("usercoupons")
    Call<CouponCall.CouponRes> getAllCoupons(@Header(AUTH_KEY) String authToken, @Body CouponCall couponCall);

    @POST("redeemcoupon")
    Call<RedeemCoupon.RedeemCouponRes> setRedeemsion(@Header(AUTH_KEY) String authToken, @Body RedeemCoupon redeemCoupon);

    @POST("areanames")
    Call<MainAreaName> getMainAreaNames(@Header(AUTH_KEY) String authToken);


    @POST("branchshorturl")
    Call<ShortenUrl.ShortenUrlRes> getShortUrlForServer(@Header(AUTH_KEY) String authToken, @Body ShortenUrl shortenUrl);

    @POST("sharecoupon")
    Call<SendGift.SendGiftRes> getShortUrlForServer(@Header(AUTH_KEY) String authToken,@Body SendGift sendGift);

    @POST("mobileappversion")
    Call<VersionCodeRes> getVersionCode(@Header(AUTH_KEY) String authToken);

    @GET("url")
    Call<ReadBranchGiftData> getBranchGiftData(@Query("url") String url , @Query("branch_key") String branch_key);

    @GET("giftmycouponmobile/")
    Call<TransferCouponRes> getTransferRes(@Header(AUTH_KEY) String auth ,@Query("token") String token);
}
