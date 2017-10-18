package cn.fjlcx.android.imitatewb.cache;

import java.util.concurrent.TimeUnit;

import cn.fjlcx.android.imitatewb.bean.HomeResult;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/18
 */

public interface Providers {
	/**
	 * LifeCache设置缓存过期时间. 如果没有设置@LifeCache , 数据将被永久缓存理除非你使用了 EvictProvider,EvictDynamicKey or EvictDynamicKeyGroup .
	 * @param oRepos
	 * @param access_token 驱逐与一个特定的键使用EvictDynamicKey相关的数据。比如分页，排序或筛选要求
	 * @param evictDynamicKey   可以明确地清理指定的数据 DynamicKey.
	 * @return
	 */
	@LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
	Observable<HomeResult> home_timeline(Observable<HomeResult> oRepos, DynamicKey access_token, EvictDynamicKey evictDynamicKey);

}
