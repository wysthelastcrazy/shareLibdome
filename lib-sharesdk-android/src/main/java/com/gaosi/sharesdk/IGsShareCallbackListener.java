package com.gaosi.sharesdk;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:
 */
public interface IGsShareCallbackListener {
    /**
     * 分享成功
     */
    void onShareSuccess();

    /**
     * 取消分享
     */
    void onShareCancel();
    /**
     * 分享失败
     * @param errorMsg
     */
    void onShareError(String errorMsg);
}
