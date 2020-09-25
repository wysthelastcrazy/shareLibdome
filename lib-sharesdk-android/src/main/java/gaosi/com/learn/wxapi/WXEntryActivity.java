package gaosi.com.learn.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gaosi.sharesdk.GsShareSdkManager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.Nullable;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx1ac6eeb1d35759d5");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG,"[onReq]+++++++");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG,"[onResp]+++++++");
        GsShareSdkManager.getInstance().wxShareCallback(baseResp);
        finish();
    }
}
