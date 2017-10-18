package cn.fjlcx.android.imitatewb.base;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

    private CompositeDisposable mCompositeDisposable;

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }
    public void unSubscribe() {
        if (mView != null) {
            mView = null;
        }
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }


}
