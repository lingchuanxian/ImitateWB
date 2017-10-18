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

	public Observable<HomeResult> home_timeline(HomeSubmit submit){
		/*return ApiEngine.getInstance().getApiService()
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
				.observeOn(AndroidSchedulers.mainThread())
				//.compose(RxHelper.<HttpResult<Object>>handleResult())
				.retryWhen(new RetryWithDelay(3,3000));*/
		return new Repository(WBApplication.getInstance().getCacheDir()).home_timeline(submit,false);
	}
}
