package gaosi.com.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.gaosi.sharesdk.GS_SHARE_MEDIA;
import com.gaosi.sharesdk.GsShareSdkManager;
import com.gaosi.sharesdk.IGsShareCallbackListener;

/**
 * @author wangyasheng
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GsShareSdkManager.getInstance().initShareSDK(this);
        findViewById(R.id.btn_share_webpage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.send_img);
                GsShareSdkManager.getInstance().shareWebPage("", "", "", bitmap,
                        GS_SHARE_MEDIA.WEIXIN, new IGsShareCallbackListener() {
                            @Override
                            public void onShareSuccess() {

                            }

                            @Override
                            public void onShareCancel() {

                            }

                            @Override
                            public void onShareError(String errorMsg) {

                            }
                        });
            }
        });
    }
}