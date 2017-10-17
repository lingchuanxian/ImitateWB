package cn.fjlcx.android.imitatewb.base;

import android.os.Bundle;

/**
 * 基类的View
 * @author ling_cx
 * @date 2017/5/4.
 */
public interface BaseView {

    /**
     * 显示数据
     */
    void showData();
    /**
     * 显示进度条
     */
    void showLoding();
    /**
     * 显示网络异常
     */
    void showNetError();
    /**
     * 显示加载错误
     * @param err 错误内容
     */
    void showErr(String err);
    /**
     * 显示暂无数据
     */
    void showNonData();

    /**
     * activity跳转
     * @param clz
     */
    void startActivity(Class<?> clz);

    /**
     * activity跳转，带参数
     * @param clz
     * @param bundle
     */
    void startActivity(Class<?> clz, Bundle bundle);

    /**
     * 请求异常
     * @param message
     */
    void requestFail(String message);

    /**
     * 请求成功，操作失败
     * @param code
     * @param message
     */
    void operateFail(int code, String message);

}