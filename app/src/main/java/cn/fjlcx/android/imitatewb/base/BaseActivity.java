package cn.fjlcx.android.imitatewb.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fjlcx.android.imitatewb.R;
import cn.fjlcx.android.imitatewb.global.WBApplication;
import cn.fjlcx.android.imitatewb.utils.AppUtil;
import cn.fjlcx.android.imitatewb.utils.NetUtils;
import cn.fjlcx.android.imitatewb.utils.ToastUtil;
import cn.fjlcx.android.imitatewb.widget.ToolBarSet;
import cn.fjlcx.android.stateframelayout.StateFrameLayout;
import cn.fjlcx.android.stateframelayout.StateOption;


/**
 * 基类的Activity
 *
 * @author ling_cx
 * @date 2017/5/4.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, ActivityCompat.OnRequestPermissionsResultCallback {
	protected final String TAG = this.getClass().getSimpleName();
	@Nullable
	@BindView(R.id.sfl_state)
	StateFrameLayout mStateFrameLayout;
	private Toolbar mToolbar;
	@Inject
	protected P mPresenter;

	public Context mContext;
	private ToolBarSet mToolBarSet;
	public WBApplication app;

	public Gson gson =  new Gson();
	public MaterialDialog progress;

	/**
	 * 需要进行检测的权限数组
	 */
	protected String[] mNeedPermissions = {};
	private static final int PERMISSION_REQUEST_CODE = 0;
	private boolean isNeedCheckPermission = true; //判断是否需要检测，防止无限弹框申请权限

	public LinearLayout linear_bar;//沉浸式状态栏填充布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(attachLayoutRes());
		initState();
		ButterKnife.bind(this);
		mNeedPermissions = getNeedPermissions();
		checkPermissions(mNeedPermissions);
		init();
		WBApplication.getInstance().getActivityManager().pushActivity(this);
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		View view = getLayoutInflater().inflate(R.layout.base_layout, null);
		super.setContentView(view);
		initDefaultView(layoutResID);
	}

	private void initDefaultView(int layoutResId) {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		FrameLayout container = (FrameLayout) findViewById(R.id.container);
		View childView = LayoutInflater.from(this).inflate(layoutResId, null);
		container.addView(childView, 0);
	}

	private void init() {
		mContext = this;
		mToolBarSet = new ToolBarSet(mToolbar, this);
		app = (WBApplication) getApplication();
		progress = new MaterialDialog.Builder(mContext)
				.content(getResources().getString(R.string.loading))
				.progress(true, 0)
				.cancelable(false)
				.build();
		initInject();
		initViews();
		if (NetUtils.isConnected(mContext)) {
			initData();
		} else {
			showNetError();
		}
	}


	/**
	 * 绑定布局文件
	 *
	 * @return 布局文件ID
	 */
	@LayoutRes
	protected abstract int attachLayoutRes();

	/**
	 * 初始化视图控件
	 */
	protected abstract void initViews();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 初始化dagger2
	 */
	protected abstract void initInject();

	/**
	 * 获取所需的危险权限进行请求
	 */
	protected abstract String[] getNeedPermissions();

	/**
	 * 获取Toolbar对象
	 *
	 * @return
	 */
	public ToolBarSet getToolBar() {
		if (mToolBarSet == null) {
			mToolBarSet = new ToolBarSet(mToolbar, this);
		}
		return mToolBarSet;
	}

	@Override
	public void showData() {
		if (mStateFrameLayout != null) {
			mStateFrameLayout.showState(new StateOption()
					.setState(StateOption.State.datas));
		}
	}

	@Override
	public void showLoding() {
		if (mStateFrameLayout != null) {
			mStateFrameLayout.showState(new StateOption()
					.setState(StateOption.State.loading)
					.setMessage("正在加载")
					.setMessageSize(14)
					.setMessageColor(R.color.state_text_color)
			);
		}
	}

	@Override
	public void showNetError() {
		if (mStateFrameLayout != null) {
			mStateFrameLayout.showState(new StateOption()
					.setState(StateOption.State.offline)
					.setShowImage(true)
					.setImageRes(R.mipmap.ic_net_error)
					.setMessage(getResources().getString(R.string.state_offline))
					.setMessageSize(14)
					.setMessageColor(R.color.state_text_color)
			);
		}
	}

	@Override
	public void showErr(String err) {
		if (mStateFrameLayout != null) {
			mStateFrameLayout.showState(new StateOption()
					.setState(StateOption.State.error)
					.setShowImage(true)
					.setImageRes(R.mipmap.ic_error)
					.setMessage(err)
					.setMessageSize(14)
					.setMessageColor(R.color.state_text_color)
			);
		}
	}

	@Override
	public void showNonData() {
		if (mStateFrameLayout != null) {
			mStateFrameLayout.showState(new StateOption()
					.setState(StateOption.State.empty)
					.setShowImage(true)
					.setImageRes(R.mipmap.ic_empty)
					.setMessage("暂无数据")
					.setMessageSize(14)
					.setMessageColor(R.color.state_text_color)
			);
		}
	}


	/**
	 * 设置activity之间的切换动画
	 */
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		//overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
	}

	@Override
	public void finish() {
		super.finish();
		//overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
	}

	/**
	 * 页面跳转
	 *
	 * @param clz
	 */
	@Override
	public void startActivity(Class<?> clz) {
		startActivity(new Intent(BaseActivity.this, clz));
	}

	/**
	 * 携带数据的页面跳转
	 *
	 * @param clz
	 * @param bundle
	 */
	@Override
	public void startActivity(Class<?> clz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * 动态的设置状态栏  实现沉浸式状态栏
	 */
	private void initState() {
		//当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			linear_bar = (LinearLayout) findViewById(R.id.status_bar_content);
			linear_bar.setVisibility(View.VISIBLE);
			//获取到状态栏的高度
			int statusHeight = getStatusBarHeight();
			//动态的设置隐藏布局的高度
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
			params.height = statusHeight;
			linear_bar.setLayoutParams(params);
		}
	}

	/**
	 * 通过反射的方式获取状态栏高度
	 *
	 * @return
	 */
	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 请求失败异常处理
	 * @param message
	 */
	@Override
	public void requestFail(final String message) {
		Log.d(TAG, "requestFail: " + message);
		progress.dismiss();
		showErr(message);
	}

	/**
	 * 操作失败异常处理
	 * @param code
	 * @param message
	 */
	@Override
	public void operateFail(int code, final String message) {
		progress.dismiss();
		ToastUtil.show(mContext, message);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				finish();
			}
		}, 1000);
	}

	/**
	 * 校验权限是否已获得
	 *
	 * @param permissions
	 */
	private void checkPermissions(String... permissions) {
		List<String> needRequestPermissionList = findDeniedPermissions(permissions);
		if (null != needRequestPermissionList
				&& needRequestPermissionList.size() > 0) {
			ActivityCompat.requestPermissions(this,
					needRequestPermissionList.toArray(
							new String[needRequestPermissionList.size()]),
					PERMISSION_REQUEST_CODE);
		}
	}

	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissionList = new ArrayList<>();
		for (String perm : permissions) {
			if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
					|| ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
				needRequestPermissionList.add(perm);
			}
		}
		return needRequestPermissionList;
	}

	/**
	 * 检测是否需要的权限都已经授权
	 *
	 * @param grantResults
	 * @return
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 请求权限的结果
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (!verifyPermissions(grantResults)) {
				showMissingPermissionDialog();
				isNeedCheckPermission = false;
			}
		}
	}

	/**
	 * 显示提示信息
	 */
	private void showMissingPermissionDialog() {
		new MaterialDialog.Builder(this)
				.title("提示信息")
				.content("缺少必要权限，应用将不能正常使用。<br>\\r请点击\\\"去设置\\\"-\\\"权限\\\"-打开所需权限。")
				.positiveText("去设置")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						AppUtil.startAppSettings(mContext);
					}
				})
				.negativeText("拒绝")
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						//app.finishAllActivity();
						finish();
					}
				})
				.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 重新唤醒后重新判断是否拥有权限
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (isNeedCheckPermission) {
			checkPermissions(mNeedPermissions);
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy: BaseOnDestory");
		super.onDestroy();
		progress.dismiss();
		if (mPresenter != null) {
			mPresenter.unSubscribe();
		}
		app.getActivityManager().popActivity(this);
	}
}
