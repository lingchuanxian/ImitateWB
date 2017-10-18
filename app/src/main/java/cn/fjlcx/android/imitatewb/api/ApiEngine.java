package cn.fjlcx.android.imitatewb.api;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.fjlcx.android.imitatewb.global.Contants;
import cn.fjlcx.android.imitatewb.global.WBApplication;
import cn.fjlcx.android.imitatewb.utils.NetUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

	//缓存拦截器，统一缓存60s
	static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			Response response = chain.proceed(request);
			if (NetUtils.isConnected(WBApplication.getInstance())) {
				int maxAge = 60;//缓存失效时间，单位为秒
				return response.newBuilder()
						.removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
						.header("Cache-Control", "public ,max-age=" + maxAge)
						.build();
			}
			return response;
		}
	};

	private ApiEngine() {
		//日志拦截器
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		//缓存
		int size = 1024 * 1024 * 100;
		File cacheFile = new File(WBApplication.getInstance().getCacheDir(), "OkHttpCache");
		Cache cache = new Cache(cacheFile, size);

		Interceptor cacheInterceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				CacheControl.Builder cacheBuilder = new CacheControl.Builder();
				cacheBuilder.maxAge(0, TimeUnit.SECONDS);
				cacheBuilder.maxStale(365,TimeUnit.DAYS);
				CacheControl cacheControl = cacheBuilder.build();

				Request request = chain.request();
				if(!NetUtils.isConnected(WBApplication.getInstance())){
					request = request.newBuilder()
							.cacheControl(cacheControl)
							.build();
				}
				Response originalResponse = chain.proceed(request);
				if (NetUtils.isConnected(WBApplication.getInstance())) {
					int maxAge = 60; // read from cache
					return originalResponse.newBuilder()
							.removeHeader("Pragma")
							.header("Cache-Control", "public ,max-age=" + maxAge)
							.build();
				} else {
					int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
					return originalResponse.newBuilder()
							.removeHeader("Pragma")
							.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
							.build();
				}
			}
		};

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(12, TimeUnit.SECONDS)//设置连接超时
				.readTimeout(12, TimeUnit.SECONDS)//读取超时
				.writeTimeout(12, TimeUnit.SECONDS)//写入超时
				//.addNetworkInterceptor(cacheInterceptor)
				.addInterceptor(loggingInterceptor)//日志拦截器
				.cache(cache)
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
