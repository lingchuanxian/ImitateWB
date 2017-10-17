package cn.fjlcx.android.imitatewb.global;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import cn.fjlcx.android.imitatewb.di.component.AppComponent;
import cn.fjlcx.android.imitatewb.di.component.DaggerAppComponent;
import cn.fjlcx.android.imitatewb.di.module.AppModule;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/17
 */

public class WBApplication extends Application {
	private static WBApplication instance;
	private ActivityManager activityManager = null; // activity管理类
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		if (LeakCanary.isInAnalyzerProcess(this)) {
			return;
		}
		activityManager = cn.fjlcx.android.imitatewb.global.ActivityManager.getInstance();

	}

	public static WBApplication getInstance() {
		return instance;
	}

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	public AppComponent getAppComponent(){
		return DaggerAppComponent.builder()
				.appModule(new AppModule(this))
				.build();
	}

	public void finishAllActivity(){
		activityManager.finishAllActivity();
	}
}
