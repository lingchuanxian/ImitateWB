package cn.fjlcx.android.imitatewb.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.fjlcx.android.imitatewb.R;
import cn.fjlcx.android.imitatewb.base.BaseActivity;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.bean.HomeSubmit;
import cn.fjlcx.android.imitatewb.di.component.DaggerHomeTimelineComponent;
import cn.fjlcx.android.imitatewb.di.module.AppModule;
import cn.fjlcx.android.imitatewb.di.module.HomeTimelineModule;
import cn.fjlcx.android.imitatewb.global.Contants;
import cn.fjlcx.android.imitatewb.mvp.presenter.HomeTimelinePresenter;
import cn.fjlcx.android.imitatewb.mvp.view.ViewContract;
import cn.fjlcx.android.imitatewb.ui.adapter.HomeResultAdapter;

public class HomeActivity extends BaseActivity<HomeTimelinePresenter> implements ViewContract.HomeTimelineView {
	protected final String TAG = this.getClass().getSimpleName();
	@BindView(R.id.rclv_home)
	RecyclerView mRclvHome;
	private List<HomeResult.StatusesBean> mDatas = new ArrayList<>();
	private HomeResultAdapter mAdapter;
	/**
	 * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
	 */
	private Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
	/**
	 * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
	 */
	private SsoHandler mSsoHandler;

	@Override
	protected int attachLayoutRes() {
		return R.layout.activity_main;
	}

	@Override
	protected void initViews() {
		getToolBar().setTitle("新浪微博")
				.setDisplayHomeAsUpEnabled(false);
		WbSdk.install(this, new AuthInfo(this, Contants.APP_KEY, Contants.REDIRECT_URL, Contants.SCOPE));
		mSsoHandler = new SsoHandler(HomeActivity.this);
		mRclvHome.setLayoutManager(new LinearLayoutManager(mContext));
		mAdapter = new HomeResultAdapter(mDatas);
		mRclvHome.setAdapter(mAdapter);
	}

	@Override
	protected void initData() {
		loadData();
	}

	@Override
	protected void initInject() {
		DaggerHomeTimelineComponent.builder()
				.appModule(new AppModule(this))
				.homeTimelineModule(new HomeTimelineModule(this))
				.build()
				.inject(this);
	}

	@Override
	protected String[] getNeedPermissions() {
		return new String[0];
	}

	@OnClick(R.id.login)
	void onClick(View view) {
		mSsoHandler.authorize(new SelfWbAuthListener());
	}

	public void loadData() {
		HomeSubmit mHomeSubmit = new HomeSubmit();
		//if (mAccessToken.isSessionValid()) {
			//mHomeSubmit.setAccess_token(AccessTokenKeeper.readAccessToken(mContext).getToken());
			mHomeSubmit.setAccess_token("2.00qShHNEyOMuzB1fd9ac194fFABsxD");
			mHomeSubmit.setCount(20);
			mHomeSubmit.setPage(1);
			mPresenter.HomeTimeline(mHomeSubmit);
		//}
	}

	@Override
	public void HomeTimelineViewSuccess(HomeResult data) {
		Log.d(TAG, "HomeTimelineViewSuccess: " + data.toString());
		mAdapter.setNewData(data.getStatuses());
	}

	private class SelfWbAuthListener implements WbAuthListener {
		@Override
		public void onSuccess(final Oauth2AccessToken token) {
			Log.d(TAG, "onSuccess: ");
			HomeActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mAccessToken = token;
					if (mAccessToken.isSessionValid()) {
						// 保存 Token 到 SharedPreferences
						Log.d(TAG, "run: " + mAccessToken);
						AccessTokenKeeper.writeAccessToken(HomeActivity.this, mAccessToken);
						loadData();
					}
				}
			});
		}

		@Override
		public void cancel() {
			Log.d(TAG, "cancel: ");
		}

		@Override
		public void onFailure(WbConnectErrorMessage message) {
			Log.d(TAG, "onFailure: ");
		}
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
