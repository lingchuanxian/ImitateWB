package cn.fjlcx.android.imitatewb.cache;

import java.io.File;

import cn.fjlcx.android.imitatewb.api.ApiEngine;
import cn.fjlcx.android.imitatewb.api.RetryWithDelay;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/18
 */

public class Repository {
	private final Providers providers;

	public Repository(File cacheDir) {
		providers = new RxCache.Builder()
				.persistence(cacheDir, new GsonSpeaker())
				.using(Providers.class);
	}

	public Observable<HomeResult> home_timeline(HomeSubmit submit, final boolean update) {
		return providers.home_timeline(
				ApiEngine.getInstance().getApiService()
						.home_timeline(submit.getAccess_token(), submit.getSince_id(), submit.getMax_id(), submit.getCount(), submit.getPage(), submit.getBase_app(), submit.getFeature(), submit.getTrim_user())
						.subscribeOn(Schedulers.io())
						.unsubscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.retryWhen(new RetryWithDelay(3,3000)),
				new DynamicKeyGroup(submit.getAccess_token(),submit.getPage()), new EvictDynamicKey(update));
	}
}
