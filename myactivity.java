package com.sample.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;

import org.jetbrains.annotations.NotNull;

public class myactivity extends baseapp {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //after succesfully init, all ads is loaded automatically
        //to get banner view :
        getBannerAdView();
        //to load interstitial
        loadinterstitialads();
        //to show interstitial
        showinterstitialad();
        //to load native ad
        loadnativead();
        //to add listener to native ad loader
        setAdNativelistener(new nativeadlistener() {
            @Override
            public void getads(NativeAdView mediumNativeadView, NativeAdView BigNativeAdView) {
        //do your code to add this native ad view to your layout
            }
        });
        //to get native adview manually
        getBigNativeadView();
        getMediumNativeAdView();
        //to load rewarded interstitial ad
        loadRewardedinterstitialAd(new rewardedlistener() {
            @Override
            public void onreward(boolean b) {
                //this will true if succesfully loaded
                if(b){
                    showRewardedInterstitialAD(new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
                            //do your code to reward user after view video
                        }
                    });
                }
            }
        });
        //to show GDPR form
        showform();
    }
}
