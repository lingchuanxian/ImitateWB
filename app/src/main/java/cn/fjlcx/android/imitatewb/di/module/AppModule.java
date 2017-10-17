package cn.fjlcx.android.imitatewb.di.module;

import android.content.Context;

import javax.inject.Singleton;

import cn.fjlcx.android.imitatewb.api.ApiEngine;
import cn.fjlcx.android.imitatewb.api.ApiService;
import dagger.Module;
import dagger.Provides;

/**
 * @author ling_cx
 * @date 2017/5/4.
 */
@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }


    @Provides
    @Singleton
    public ApiService provideApiService() {
        return ApiEngine.getInstance().getApiService();
    }
}
