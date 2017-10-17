package cn.fjlcx.android.imitatewb.api;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * RxJava错误重试
 * @author ling_cx
 * @date 2017/6/9.
 */

public class RetryWithDelay implements
		Func1<Observable<? extends Throwable>, Observable<?>> {

	protected final String TAG = this.getClass().getSimpleName();
	private final int maxRetries;
	private final int retryDelayMillis;
	private int retryCount;

	public RetryWithDelay(int maxRetries, int retryDelayMillis) {
		this.maxRetries = maxRetries;
		this.retryDelayMillis = retryDelayMillis;
	}

	@Override
	public Observable<?> call(Observable<? extends Throwable> attempts) {
		return attempts
				.flatMap(new Func1<Throwable, Observable<?>>() {
					@Override
					public Observable<?> call(Throwable throwable) {
						if (++retryCount <= maxRetries) {
							Log.d(TAG, "call: retry");
							return Observable.timer(retryDelayMillis,
									TimeUnit.MILLISECONDS);
						}
						return Observable.error(throwable);
					}
				});
	}
}