package cn.fjlcx.android.imitatewb.base;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * MVP框架的简单封装 P处理层
 * @author ling_cx
 * @date 2017/5/4.
 */
public abstract class BasePresenter<M extends BaseModel,V extends BaseView> {
    protected final String TAG = this.getClass().getSimpleName();
    @Inject
    public M mModel;
    public V mView;

    private CompositeSubscription mCompositeSubscription;

    public void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
    public void unSubscribe() {
        if (mView != null) {
            mView = null;
        }
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }


}
