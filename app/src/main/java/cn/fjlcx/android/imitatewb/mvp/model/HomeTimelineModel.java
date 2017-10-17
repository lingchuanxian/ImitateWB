package cn.fjlcx.android.imitatewb.mvp.model;


import cn.fjlcx.android.imitatewb.api.ApiEngine;
import cn.fjlcx.android.imitatewb.api.RetryWithDelay;
import cn.fjlcx.android.imitatewb.base.BaseModel;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import cn.fjlcx.android.imitatewb.di.scope.ActivityScope;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ling_cx
 * @date 2017/8/8.
 */
@ActivityScope
public class HomeTimelineModel implements BaseModel {

	public Observable<HomeResult> home_timeline(HomeSubmit submit){
		return ApiEngine.getInstance().getApiService()
				.home_timeline(submit.getAccess_token(),
						submit.getSince_id(),
						submit.getMax_id(),
						submit.getCount(),
						submit.getPage(),
						submit.getBase_app(),
						submit.getFeature(),
						submit.getTrim_user())
				.subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(AndroidSchedulers.mainThread())
				//.compose(RxHelper.<HttpResult<Object>>handleResult())
				.retryWhen(new RetryWithDelay(3,3000));
	}
}
