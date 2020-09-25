package com.gaosi.sharesdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:分享管理类
 */
public class GsShareSdkManager {
    private final String TAG = "GsShareSdkManager";
    private static GsShareSdkManager instance;
    private IWXAPI iwxapi;
    private final String APP_ID = "wx1ac6eeb1d35759d5";
    private final String APP_SECRET = "2d6e8bf6c6bcd235df3832163f14df08";
    private IGsShareCallbackListener listener;
    private GsShareSdkManager() {
    }

    public static GsShareSdkManager getInstance() {
        if (instance == null) {
            synchronized (GsShareSdkManager.class) {
                if (instance == null) {
                    instance = new GsShareSdkManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化分享相关的sdk
     * @param context
     */
    public void initShareSDK(Context context){
        initWxSDK(context);
    }

    /**
     * 向微信终端注册应用id
     * @param context
     */
    private void initWxSDK(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    /**===============文字类型分享==================**/


    /**===============webPage分享==================**/
    /**
     * web页分享对外统一方法，根据GS_SHARE_MEDIA自行调用对应渠道的分享逻辑。
     * @param targetUrl
     * @param title
     * @param description
     * @param thumb
     * @param type
     * @param listener
     */
    public void shareWebPage(String targetUrl,String title,String description,Bitmap thumb,
                             GS_SHARE_MEDIA type,IGsShareCallbackListener listener){
        this.listener = listener;
        switch (type){
            case WEIXIN:
                //微信聊天分享
                wxShareWebPage(targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneSession);
                break;
            case WEIXIN_CIRCLE:
                //微信朋友圈分享
                wxShareWebPage(targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
    }
    /**
     * 微信 网页类型分享
     */
    private void wxShareWebPage(String targetUrl,String title,String description,Bitmap thumbBmp,int scene) {
       if (iwxapi == null){
           Log.e(TAG,"IWXAPI 未初始化");
           return;
       }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = targetUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (thumbBmp!=null&&!thumbBmp.isRecycled()) {
            Bitmap scaleThumbBmp = Bitmap.createScaledBitmap(thumbBmp, 150, 150, true);
            thumbBmp.recycle();
            msg.thumbData = ShareUtils.bmpToByteArray(scaleThumbBmp, true);
            msg.setThumbImage(thumbBmp);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }


    /**
     * 微信分享回调处理
     * @param resp
     */
    public void wxShareCallback(BaseResp resp) {
        Log.d(TAG, "[wxShareCallback] ++++++++");
        if (resp != null&&listener!=null) {
            Log.d(TAG, "[wxShareCallback] rspCode:" + resp.errCode + "  ,rspMsg:" + resp.errStr
                    + "  ,transaction:" + resp.transaction);
            int code = resp.errCode;
            switch (code) {
                /**
                 * 注意：微信为减少"强制分享至不同群"的情况，在微信客户端6.7.2版本之后
                 * 用户从app中分享消息给微信好友、朋友圈时，开发者将无法获知用户是否分享完成。
                 * 原先的cancel事件和success事件统一为success事件。
                 */
                case BaseResp.ErrCode.ERR_OK:
                    Log.d(TAG, "[wxShareCallback] 分享成功！");
                    listener.onShareSuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Log.d(TAG, "[wxShareCallback] 分享取消！");
                    listener.onShareCancel();
                    break;
                default:
                    Log.d(TAG, "[wxShareCallback] 分享错误：" + resp.errStr);
                    listener.onShareError(resp.errStr);
                    break;
            }
        } else if (resp == null){
            Log.d(TAG, "[wxShareCallback] resp is null");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
