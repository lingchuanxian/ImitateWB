package cn.fjlcx.android.imitatewb.mvp.model;


import cn.fjlcx.android.imitatewb.base.BaseModel;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import cn.fjlcx.android.imitatewb.cache.Repository;
import cn.fjlcx.android.imitatewb.di.scope.ActivityScope;
import cn.fjlcx.android.imitatewb.global.WBApplication;
import io.reactivex.Observable;

/**
 * @author ling_cx
 * @date 2017/8/8.
 */
@ActivityScope
public class HomeTimelineModel implements BaseModel {

	public Observable<HomeResult> home_timeline(HomeSubmit submit,boolean update){
		return new Repository(WBApplication.getInstance().getCacheDir()).home_timeline(submit,update);
	}
}
