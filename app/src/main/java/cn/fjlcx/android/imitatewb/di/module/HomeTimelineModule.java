package cn.fjlcx.android.imitatewb.di.module;

import cn.fjlcx.android.imitatewb.mvp.model.HomeTimelineModel;
import cn.fjlcx.android.imitatewb.mvp.presenter.HomeTimelinePresenter;
import cn.fjlcx.android.imitatewb.mvp.view.ViewContract;
import dagger.Module;
import dagger.Provides;

/**
 * @author ling_cx
 * @date 2017/8/10.
 */
@Module
public class HomeTimelineModule {
    private ViewContract.HomeTimelineView mView;

    public HomeTimelineModule(ViewContract.HomeTimelineView view) {
        this.mView = view;
    }
    @Provides
    public HomeTimelinePresenter provideHomeTimelinePresenter(HomeTimelineModel model){
        return new HomeTimelinePresenter(model,mView);
    }

    @Provides
    public HomeTimelineModel provideHomeTimelineModel(){
        return new HomeTimelineModel();
    }

}
