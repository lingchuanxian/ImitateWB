package cn.fjlcx.android.imitatewb.di.component;

import javax.inject.Singleton;

import cn.fjlcx.android.imitatewb.ui.activity.HomeActivity;
import cn.fjlcx.android.imitatewb.di.module.AppModule;
import cn.fjlcx.android.imitatewb.di.module.HomeTimelineModule;
import dagger.Component;

/**
 * @author ling_cx
 * @date 2017/5/4.
 */
@Singleton
@Component(modules = HomeTimelineModule.class,dependencies = AppModule.class)
public interface HomeTimelineComponent {
   void inject(HomeActivity activity);
}
