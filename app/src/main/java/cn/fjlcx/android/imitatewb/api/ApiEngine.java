package cn.fjlcx.android.imitatewb.api;

import java.util.concurrent.TimeUnit;

import cn.fjlcx.android.imitatewb.global.Contants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author ling_cx
 * @date 2017/6/7.
 */

public class ApiEngine {
	private volatile static ApiEngine apiEngine;
	private Retrofit retrofit;

	private ApiEngine() {
		//日志拦截器
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		//缓存
		/*int size = 1024 * 1024 * 100;
		File cacheFile = new File(App.getContext().getCacheDir(), "OkHttpCache");
		Cache cache = new Cache(cacheFile, size);*/

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(12, TimeUnit.SECONDS)
				.writeTimeout(12, TimeUnit.SECONDS)
				.addInterceptor(loggingInterceptor)
				//.cache(cache)
				.build();

		retrofit = new Retrofit.Builder()
				.baseUrl(Contants.IP)
				.client(client)
				.addConverterFactory(ScalarsConverterFactory.create())//增加返回值为String的支持
				.addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//增加返回值为Oservable<T>的支持
				.build();
	}

	public static ApiEngine getInstance() {
		if (apiEngine == null) {
			synchronized (ApiEngine.class) {
				if (apiEngine == null) {
					apiEngine = new ApiEngine();
				}
			}
		}
		return apiEngine;
	}

	public ApiService getApiService() {
		return retrofit.create(ApiService.class);
	}
}
