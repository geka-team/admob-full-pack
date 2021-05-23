package com.sample.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.sample.myapplication.databinding.AdUnifiedBinding;
import com.sample.myapplication.databinding.MediumadnatBinding;

import org.jetbrains.annotations.NotNull;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class baseapp extends AppCompatActivity {
    private permissioninterface pi;
    private resultinterface rs;
    private final String TAG=baseapp.class.getSimpleName();
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;
    private boolean isSafe=true;
    private boolean pending=false;
    private AppOpenManager openad;
    private RequestConfiguration rca;
    private InterstitialAd mInterstitialAd;
    private AdUnifiedBinding adUnifiedBinding;
    private NativeAd nativeAd;
    private NativeAdView exitad;
    private MediumadnatBinding mediumadnatBinding;
    private TextView primaryView;
    private TextView secondaryView;
    private RatingBar ratingBar;
    private ImageView iconView;
    private TextView callToActionView;
    private NativeAdView nativeAdView;
    private AdView adView;
    private AdRequest adRequest1;
    private final String adbannerid="ca-app-pub-3940256099942544/6300978111";//replace with your admob banner ad space id
    private final String adinterstitialid="ca-app-pub-3940256099942544/1033173712";////replace with your admob interstitial ad space id
    private final String adnativeid="ca-app-pub-3940256099942544/2247696110";//replace with your admob native ad space id
    private final String adopenid="ca-app-pub-3940256099942544/3419835294";//replace with your admob open ad space id
    private final String adinterstitialrewarded="ca-app-pub-3940256099942544/5354046379";////replace with your admob interstitial rewarded ad space id
    private RewardedInterstitialAd rewardedInterstitialAd;

    public void startactivityresult(resultinterface r,Intent i){
         rs=r;
         activityResultLauncher.launch(i);
     }
     public void startpermissionresult(permissioninterface o,String manifest){
         pi=o;
         permissionLauncher.launch(manifest);
     }
    private final ActivityResultLauncher<Intent> activityResultLauncher = this.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(rs!=null){
                        rs.onclosed(result);
                    }
                }
            });
    private final ActivityResultLauncher<String> permissionLauncher =
            this.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted-> {
                if(pi!=null){
                    pi.onaccept(isGranted);
                }
            });
    public InterstitialAd getmInterstitialAd(){
        return mInterstitialAd;
    }
    public void showinterstitialad() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    public void showRewardedInterstitialAD(OnUserEarnedRewardListener listener){
        rewardedInterstitialAd.show(/* Activity */ baseapp.this,/*
    OnUserEarnedRewardListener */ listener);
    }

    public void loadRewardedinterstitialAd(rewardedlistener listener) {
        RewardedInterstitialAd.load(this,adinterstitialrewarded,
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NotNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {

                            }

                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {

                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {

                            }
                        });
                        listener.onreward(true);

                    }
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        listener.onreward(false);
                        Toast.makeText(baseapp.this,"Error ads failed to load",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void loadinterstitialads(){
        if(getadrequest()!=null&&mInterstitialAd==null){
            InterstitialAd.load(this,adinterstitialid, getadrequest(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd inters) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = inters;
                    listeninters();
                    Log.i(TAG, "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });
        }
    }
    private void listeninters(){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });
    }

    private void setNativeAd(NativeAd nativeAd, TextView primaryView, TextView secondaryView, RatingBar ratingBar, TextView callToActionView, ImageView iconView, NativeAdView nativeAdView) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        String headline = nativeAd.getHeadline();
        String cta = nativeAd.getCallToAction();
        Double starRating = nativeAd.getStarRating();
        NativeAd.Image icon = nativeAd.getIcon();
        String secondaryText;
        nativeAdView.setCallToActionView(callToActionView);
        nativeAdView.setHeadlineView(primaryView);
        secondaryView.setVisibility(VISIBLE);
        if (adHasOnlyStore(nativeAd)) {
            nativeAdView.setStoreView(secondaryView);
            secondaryText = store;
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView.setAdvertiserView(secondaryView);
            secondaryText = advertiser;
        } else {
            secondaryText = "";
        }
        primaryView.setText(headline);
        callToActionView.setText(cta);

        if (starRating != null) {
            if(starRating >0){
                secondaryView.setVisibility(GONE);
                ratingBar.setEnabled(true);
                ratingBar.setVisibility(VISIBLE);
                ratingBar.setMax(5);
                ratingBar.setRating(nativeAd.getStarRating().floatValue());
                nativeAdView.setStarRatingView(ratingBar);
            }
        } else {
            secondaryView.setText(secondaryText);
            secondaryView.setVisibility(VISIBLE);
            ratingBar.setVisibility(GONE);
        }

        if (icon != null) {
            iconView.setVisibility(VISIBLE);
            iconView.setImageDrawable(icon.getDrawable());
            iconView.setClipToOutline(true);
        } else {
            iconView.setVisibility(GONE);
        }
        nativeAdView.setNativeAd(nativeAd);
    }
    private boolean adHasOnlyStore(NativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser);
    }

    private void populateUnifiedNativeAdView(NativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adUnifiedBinding.adMedia;
        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                }
            }
            @Override
            public void onChildViewRemoved(View parent, View child) {}
        });
        adView.setMediaView(mediaView);
        // Set other ad assets.
        adView.setHeadlineView(adUnifiedBinding.adHeadline);
        adView.setBodyView(adUnifiedBinding.adBody);
        adView.setCallToActionView(adUnifiedBinding.adCallToAction);
        adView.setIconView(adUnifiedBinding.adAppIcon);
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            adUnifiedBinding.adAppIcon.setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setPriceView(adUnifiedBinding.adPrice);
        RatingBar rb=adUnifiedBinding.adStars;
        adView.setStarRatingView(rb);
        adView.setStoreView(adUnifiedBinding.adStore);
        adView.setAdvertiserView(adUnifiedBinding.adAdvertiser);

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getAdChoicesInfo() != null){
            com.google.android.gms.ads.nativead.AdChoicesView choicesView = new com.google.android.gms.ads.nativead.AdChoicesView(adView.getContext());
            adView.setAdChoicesView(choicesView);
        }


        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        Double starrating=nativeAd.getStarRating();
        if (starrating==null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            if(starrating>0){
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }else {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            }
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
    }

    public void loadnativead(){
        builder.forNativeAd(nativeAds -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = nativeAds;
            exitad = adUnifiedBinding.unified;
            populateUnifiedNativeAdView(exitad);

            nativeAdView = mediumadnatBinding.nativeAdView;
            primaryView = mediumadnatBinding.primary;
            secondaryView = mediumadnatBinding.secondary;
            ratingBar = mediumadnatBinding.ratingBar;
            ratingBar.setEnabled(false);
            callToActionView = mediumadnatBinding.cta;
            iconView = mediumadnatBinding.icon;
            setNativeAd(nativeAds, primaryView, secondaryView, ratingBar, callToActionView, iconView, nativeAdView);
        });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError errorCode) {
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(sessi!=null){
                    sessi.getads(getMediumNativeAdView(),getBigNativeadView());
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        }).build();
        if (getadrequest() != null) {
            adLoader.loadAd(getadrequest());
        }
    }
    public AdRequest getadrequest(){
        return adRequest1;
    }

    private void initads(){
        adRequest1=new AdRequest.Builder().build();
        MobileAds.setRequestConfiguration(rca);
        MobileAds.initialize(this, initializationStatus -> {
            loadbannerad();
            loadnativead();
            loadinterstitialads();
            openad.fetchAd();
        });
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void requestmessage(){
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings
                        .DebugGeography
                        .DEBUG_GEOGRAPHY_EEA)
                //.addTestDeviceHashedId(ac.gettd()).addTestDeviceHashedId(ac.gettd2())
                .build();
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                //.setConsentDebugSettings(debugSettings)
                .build();
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                () -> {
                    // The consent information state was updated.
                    // You are now ready to check if a form is available.
                    if (consentInformation.isConsentFormAvailable()) {
                        if(isSafe){
                            loadForm();
                        }else {
                            pending=true;
                        }

                    }else {
                        initads();
                    }
                },
                formError -> {
                    // Handle the error.
                    Log.i("error form",formError.getMessage());
                });
    }

    private void loadForm() {
        UserMessagingPlatform.loadConsentForm(
                this,
                consentForms -> {
                    consentForm = consentForms;
                    if(consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED||consentInformation.getConsentStatus()== ConsentInformation.ConsentStatus.UNKNOWN) {
                        consentForm.show(
                                this,
                                formError -> {
                                    // Handle dismissal by reloading form.
                                    if(isSafe){
                                        loadForm();
                                    }else {
                                        pending=true;
                                    }

                                });

                    }else if(consentInformation.getConsentStatus()== ConsentInformation.ConsentStatus.NOT_REQUIRED){
                        // Log.d("status","not required");
                        pending=false;
                        initads();
                    }
                    else if(consentInformation.getConsentStatus()== ConsentInformation.ConsentStatus.OBTAINED){
                        //   Log.d("status","obtained");
                        initads();
                        pending=false;
                    }
                },
                formError -> {
                    // Handle the error
                    //loadForm();
                }
        );
    }

    public void showform() {
        if(consentForm!=null){
            consentForm.show(
                    this,
                    formError -> {
                        // Handle dismissal by reloading form.
                        loadForm();
                    });
        }
    }

    private AdLoader.Builder builder;

    private void adspace(){
        adUnifiedBinding= AdUnifiedBinding.inflate(getLayoutInflater());
        mediumadnatBinding = MediumadnatBinding.inflate(getLayoutInflater());
        rca= MobileAds.getRequestConfiguration().toBuilder().build();
        builder = new AdLoader.Builder(this, adnativeid);
        openad=new AppOpenManager(this,adopenid);
        adView=new AdView(this);
        adView.setAdUnitId(adbannerid);
        adView.setAdSize(getAdSize());
    }

    public NativeAdView getBigNativeadView() {
        return exitad;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public NativeAdView getMediumNativeAdView() {
        return nativeAdView;
    }

    private void loadbannerad(){
        adView.loadAd(getadrequest());
    }

    public AdView getBannerAdView() {
        return adView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        adspace();
       requestmessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSafe=true;
        if(adView!=null){
            adView.resume();
        }
        if(pending){
            loadForm();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        openad.showAdIfAvailable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSafe=false;
        if(adView!=null){
            adView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isSafe=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSafe=false;
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        if(adView!=null){
            adView.destroy();
        }

    }

    public nativeadlistener sessi;
    public void setAdNativelistener(nativeadlistener ad){
        sessi=ad;
    }

}
