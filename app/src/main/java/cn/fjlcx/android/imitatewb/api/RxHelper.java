package cn.fjlcx.android.imitatewb.api;

import android.util.Log;

import cn.fjlcx.android.imitatewb.bean.HttpResult;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Rxava结果预处理
 * @author ling_cx
 * @date 2017/6/9.
 */

public class RxHelper {
	/**
	 * 获得结果并作判断
	 * @param <T>
	 * @return
	 */
	public static <T> Observable.Transformer<HttpResult<T>, T> handleResult() {
		return new Observable.Transformer<HttpResult<T>, T>() {
			@Override
			public Observable<T> call(Observable<HttpResult<T>> tObservable) {
				return tObservable.flatMap(new Func1<HttpResult<T>, Observable<T>>() {
					@Override
					public Observable<T> call(HttpResult<T> result) {
						Log.d("code", "call: "+result.getReturnCode());
						if (result.getReturnCode() == 0) {
							return createData(result.getDetailInfo());
						} else {
							return Observable.error(new ApiException(result.getReturnCode()));
						}
					}
				}).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
			}
		};
	}

	/**
	 * 创建成功的数据
	 * @param data
	 * @param <T>
	 * @return
	 */
	private static <T> Observable<T> createData(final T data) {
		return Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				try {
					subscriber.onNext(data);
					subscriber.onCompleted();
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}
		});
	}
}
