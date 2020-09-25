package com.gaosi.sharesdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:
 */
class ShareUtils {

    /**
     * bitmap转换为byte数组
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断qq是否可用
     */
    public static boolean isQQClientAvailable(Context context) {
        return isAppInstall(context,"com.tencent.mobileqq");
    }

    /**
     * 判断微信是否可用
     */
    public static boolean isWeixinAvilible(Context context) {
        return isAppInstall(context,"com.tencent.mm");
    }

    private static boolean isAppInstall(Context context, String pkgName){
        final PackageManager packageManager = context.getPackageManager();
        // 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        // 获取所有已安装程序的包信息
        if (pinfo != null) {

            for (int i = 0; i < pinfo.size(); i++) {

                String pn = pinfo.get(i).packageName;

                if (pn.equals(pkgName)) {

                    return true;

                }

            }

        }
        return false;
    }
}
