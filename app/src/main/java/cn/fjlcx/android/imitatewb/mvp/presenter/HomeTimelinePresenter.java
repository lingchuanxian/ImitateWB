package cn.fjlcx.android.imitatewb.mvp.presenter;

import android.util.Log;

import javax.inject.Inject;

import cn.fjlcx.android.imitatewb.base.BasePresenter;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import cn.fjlcx.android.imitatewb.di.scope.ActivityScope;
import cn.fjlcx.android.imitatewb.mvp.model.HomeTimelineModel;
import cn.fjlcx.android.imitatewb.mvp.view.ViewContract;
import rx.Subscriber;
import rx.Subscription;

/**
 * 公共的Operate类
 * @author ling_cx
 * @date 2017/8/4.
 */
@ActivityScope
public class HomeTimelinePresenter extends BasePresenter<HomeTimelineModel,ViewContract.HomeTimelineView> {
	private Subscription subscribe;
	@Inject
	public HomeTimelinePresenter(HomeTimelineModel model, ViewContract.HomeTimelineView view) {
		this.mModel = model;
		this.mView = view;
	}

	public void HomeTimeline(HomeSubmit mSubmitParams){
		subscribe = mModel.home_timeline(mSubmitParams)
				.subscribe(new Subscriber<HomeResult>() {
					@Override
					public void onCompleted() {
					}
					@Override
					public void onError(Throwable e) {
						mView.requestFail(e.getMessage());
					}

					@Override
					public void onNext(HomeResult result) {
						Log.d(TAG, "onNext: "+result);
						mView.HomeTimelineViewSuccess(result);
					}
				});
		addSubscribe(subscribe);
	}
}