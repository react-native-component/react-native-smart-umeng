package com.reactnativecomponent.umeng;


import android.Manifest;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;


import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class UmengShareModule extends ReactContextBaseJavaModule {
    ReactApplicationContext context;
    static final int SUCCESS=1;
    static final int CANCEL=0;
    static final int ERROR=-1;


    /**
     * 分享回调
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("test",share_media.name());
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(context, "成功了", Toast.LENGTH_LONG).show();


            WritableMap params = Arguments.createMap();
            params.putString("action",SUCCESS+"");
            params.putString("media",platform.toString());
            sendEvent(context, "onShareResult", params);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(context, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            WritableMap params = Arguments.createMap();
            params.putString("action",ERROR+"");
            params.putString("media",platform.toString());
            params.putString("msg",t.getMessage());
            sendEvent(context, "onShareResult", params);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(context, "取消了", Toast.LENGTH_LONG).show();
            WritableMap params = Arguments.createMap();
            params.putString("action",CANCEL+"");
            params.putString("media",platform.toString());
            sendEvent(context, "onShareResult", params);

        }
    };
    /**
     * 授权回调
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        Log.i("test",share_media.name());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(context, "Authorize succeed", Toast.LENGTH_SHORT).show();
            WritableMap params = Arguments.createMap();
            params.putString("action",action+"");
            params.putString("media",platform.toString());
            Iterator it=data.keySet().iterator();
            while(it.hasNext()){
                String key;
                String value;
                key=it.next().toString();
                value=data.get(key);
                params.putString(key,value);
            }
            sendEvent(context, "onAuthorizeResult", params);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(context, "Authorize fail", Toast.LENGTH_SHORT).show();
            WritableMap params = Arguments.createMap();
            params.putString("action",action+"");
            params.putString("media",platform.toString());
            sendEvent(context, "onAuthorizeResult", params);

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(context, "Authorize cancel", Toast.LENGTH_SHORT).show();
            WritableMap params = Arguments.createMap();
            params.putString("action",action+"");
            params.putString("media",platform.toString());
            sendEvent(context, "onAuthorizeResult", params);
        }
    };

    public UmengShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;




    }

    @Override
    public String getName() {
        return "UmengShareModule";
    }
    /**
     * 分享
     *
     */
    @ReactMethod
    public void creatShare() {
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(context.getCurrentActivity(),mPermissionList,123);
        }



//        Config.REDIRECT_URL = "http://sns.whalecloud.com";

    }


    /**
     * 分享
     *
     * @param readableMap
     */
    @ReactMethod
    public void shareWebPage(ReadableMap readableMap) {
        final String share_media = readableMap.hasKey("share_media") ? readableMap.getString("share_media") : "";
        final String imageurl = readableMap.hasKey("imageurl") ? readableMap.getString("imageurl") : "";
        final String fileurl = readableMap.hasKey("fileurl") ? readableMap.getString("fileurl") : "";
        final String musicurl = readableMap.hasKey("musicurl") ? readableMap.getString("musicurl") : "";
        final String videourl = readableMap.hasKey("videourl") ? readableMap.getString("videourl") : "";
        final String title = readableMap.hasKey("title") ? readableMap.getString("title") : "";
        final String text = readableMap.hasKey("text") ? readableMap.getString("text") : "";
        final String targetUrl = readableMap.hasKey("targetUrl") ? readableMap.getString("targetUrl") : "";
        final String description = readableMap.hasKey("description") ? readableMap.getString("description") : "";
        final String thumb = readableMap.hasKey("thumb") ? readableMap.getString("thumb") : "";

        /**
         * ＝＝＝＝＝＝＝＝＝＝图片分享＝＝＝＝＝＝＝＝＝＝＝＝
         */
        final UMImage image;
        if (!imageurl.isEmpty() && fileurl.isEmpty()) {
            //fileurl为空，imageurl有值
            image = new UMImage(context.getCurrentActivity(), imageurl);//网络图片

        } else if ((imageurl.isEmpty() && !fileurl.isEmpty()) || (!imageurl.isEmpty() && !fileurl.isEmpty())) {
            //fileurl有值或都有值，传本地图片
            File file = new File(fileurl);
            image = new UMImage(context.getCurrentActivity(), file);//本地图片

        } else {
            //都为空
            image = null;
        }
        /**
         * ＝＝＝＝＝＝＝＝＝＝视频分享＝＝＝＝＝＝＝＝＝＝＝＝
         */
        final UMVideo video;
        if (!videourl.isEmpty()) {
            video = new UMVideo(videourl);
            video.setTitle(title);//视频的标题
            if (!thumb.isEmpty()) {
                UMImage thumbImag = new UMImage(context.getCurrentActivity(), thumb);
                video.setThumb(thumbImag);//视频的缩略图
            }
            video.setDescription(description);//视频的描述
        } else {
            video = null;
        }
        /**
         * ＝＝＝＝＝＝＝＝＝＝音乐分享＝＝＝＝＝＝＝＝＝＝＝＝
         */
        final UMusic music;
        if (!musicurl.isEmpty()) {
            music = new UMusic(musicurl);
            music.setTitle(title);//视频的标题
            if (!thumb.isEmpty()) {
                UMImage thumbImag = new UMImage(context.getCurrentActivity(), thumb);
                music.setThumb(thumbImag);//视频的缩略图
            }
            music.setDescription(description);//视频的描述
        } else {
            music = null;
        }


        getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                ShareAction shareAction = new ShareAction(context.getCurrentActivity());
                UMWeb  web=null;



                //分享链接
                if (!targetUrl.isEmpty()) {
//                    shareAction.withTargetUrl(targetUrl);
                    web = new UMWeb(targetUrl);

                    if (!title.isEmpty() ) {
                    web.setTitle(title);
                    }


                    if (image != null) {
                        web.setThumb(image);  //缩略图
                    }

                    if (!text.isEmpty()) {
                        web.setDescription(text);//描述
                    }

                    SHARE_MEDIA media= SHARE_MEDIA.convertToEmun(share_media);
                    shareAction.setCallback(shareListener)//回调
                            .setPlatform(media)//平台
                            .withMedia(web)//链接
                            .share();//分享

                }else{
                    if (image != null) {
                        shareAction.withMedia(image);//内容

                    } else if (video != null) {
                        shareAction.withMedia(video);//内容

                    } else if (music != null) {
                        shareAction.withMedia(music);//内容
                    }
                    //标题
                    if (!title.isEmpty() && videourl.isEmpty() && musicurl.isEmpty()) {
//                    shareAction.withTitle(title);
                    }
                    //文字
                    if (!text.isEmpty()) {
                        shareAction.withText(text);
                    }

                    SHARE_MEDIA media= SHARE_MEDIA.convertToEmun(share_media);

                    shareAction.setCallback(shareListener)//回调
                            .setPlatform(media)//平台
                            .share();//分享

                }




            }
        });
    }

    /**
     * 授权
     */
    @ReactMethod
    public void authorize(ReadableMap readableMap) {

        final String share_media = readableMap.hasKey("share_media") ? readableMap.getString("share_media") : "";

        getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                UMShareAPI  mShareAPI = UMShareAPI.get( context.getCurrentActivity() );
                mShareAPI.doOauthVerify(context.getCurrentActivity(), SHARE_MEDIA.convertToEmun(share_media), umAuthListener);
            }
        });

    }

    /**
     * 获取用户信息
     */
    @ReactMethod
    public void getInfo(ReadableMap readableMap) {

        final String share_media = readableMap.hasKey("share_media") ? readableMap.getString("share_media") : "";

        getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                UMShareAPI  mShareAPI = UMShareAPI.get( context.getCurrentActivity() );
                mShareAPI.getPlatformInfo(context.getCurrentActivity(), SHARE_MEDIA.convertToEmun(share_media), umAuthListener);

            }
        });

    }



    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(RCTNativeAppEventEmitter.class)
                .emit(eventName, params);
    }


    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        return Collections.unmodifiableMap(new HashMap<String, Object>() {
            {
                put("SHARE_MEDIA", getUMedia());
            }

            private Map<String, Object> getUMedia() {
                return Collections.unmodifiableMap(new HashMap<String, Object>() {
                    {
                        put("weixin", SHARE_MEDIA.WEIXIN.toString());
                        put("weixincircle", SHARE_MEDIA.WEIXIN_CIRCLE.toString());
                        put("weixinfavorite", SHARE_MEDIA.WEIXIN_FAVORITE.toString());
                        put("alipay", SHARE_MEDIA.ALIPAY.toString());
                        put("email", SHARE_MEDIA.EMAIL.toString());
                        put("qq", SHARE_MEDIA.QQ.toString());
                        put("sms", SHARE_MEDIA.SMS.toString());
                        put("qzone", SHARE_MEDIA.QZONE.toString());
                        put("sina", SHARE_MEDIA.SINA.toString());

                    }


                });
            }
        });
    }


    /**
     * 重新组装成map
     *
     * @param readableMap
     * @param map
     */
/*
    private void rebuildReadableMap(ReadableMap readableMap, HashMap<String, String> map) {
        ReadableMapKeySetIterator interator = readableMap.keySetIterator();
        while (interator.hasNextKey()) {
            String key = interator.nextKey();
            ReadableType type = readableMap.getType(key);
            if (type.equals(ReadableType.Null)) {
            } else if (type.equals(ReadableType.Boolean)) {
                map.put(key, readableMap.getBoolean(key) + "");
            } else if (type.equals(ReadableType.Number)) {
                map.put(key, readableMap.getDouble(key) + "");
            } else if (type.equals(ReadableType.String)) {
                map.put(key, readableMap.getString(key));
            }
        }
    }
*/


}
