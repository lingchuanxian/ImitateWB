package cn.fjlcx.android.imitatewb.mvp.view;

import cn.fjlcx.android.imitatewb.base.BaseView;
import cn.fjlcx.android.imitatewb.bean.HomeResult;


/**
 * Created by lcx on 2017/6/5.
 */

public interface ViewContract {
	interface HomeTimelineView extends BaseView {
		void HomeTimelineViewSuccess(HomeResult data);
	}
}
