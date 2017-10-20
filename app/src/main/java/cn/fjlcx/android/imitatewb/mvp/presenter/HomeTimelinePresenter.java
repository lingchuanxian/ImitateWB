package cn.fjlcx.android.imitatewb.mvp.presenter;

import android.util.Log;

import javax.inject.Inject;

import cn.fjlcx.android.imitatewb.base.BasePresenter;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import cn.fjlcx.android.imitatewb.di.scope.ActivityScope;
import cn.fjlcx.android.imitatewb.mvp.model.HomeTimelineModel;
import cn.fjlcx.android.imitatewb.mvp.view.ViewContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 公共的Operate类
 * @author ling_cx
 * @date 2017/8/4.
 */
@ActivityScope
public class HomeTimelinePresenter extends BasePresenter<HomeTimelineModel,ViewContract.HomeTimelineView> {
	private Disposable mDisposable;
	@Inject
	public HomeTimelinePresenter(HomeTimelineModel model, ViewContract.HomeTimelineView view) {
		this.mModel = model;
		this.mView = view;
	}

	public void HomeTimeline(HomeSubmit mSubmitParams,boolean update){
		mModel.home_timeline(mSubmitParams,update)
				.subscribe(new Observer<HomeResult>() {
					@Override
					public void onError(Throwable e) {
						mView.requestFail(e.getMessage());
					}

					@Override
					public void onComplete() {

					}

					@Override
					public void onSubscribe(@NonNull Disposable d) {
						mDisposable = d;
					}

					@Override
					public void onNext(HomeResult result) {
						Log.d(TAG, "onNext: "+result);
						mView.HomeTimelineViewSuccess(result);
					}
				});
		addSubscribe(mDisposable);
	}
}