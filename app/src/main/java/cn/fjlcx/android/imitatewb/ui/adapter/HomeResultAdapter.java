package cn.fjlcx.android.imitatewb.ui.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.fjlcx.android.imitatewb.R;
import cn.fjlcx.android.imitatewb.bean.HomeResult;
import cn.fjlcx.android.imitatewb.utils.StringUtil;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/17
 */

public class HomeResultAdapter extends BaseQuickAdapter<HomeResult.StatusesBean,BaseViewHolder> {
	public HomeResultAdapter(@Nullable List<HomeResult.StatusesBean> data) {
		super(R.layout.layout_item_home, data);
	}
	@Override
	protected void convert(BaseViewHolder helper, HomeResult.StatusesBean item) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH-mm");
        Date date = new Date(item.getCreated_at());
        Date currentDate = new Date();
        String showDateString = "";

        long from = date.getTime();
        long to = currentDate.getTime();
        int hours = (int) ((to - from)/(1000 * 60 * 60));
        int minutes = (int) ((to - from)/(1000 * 60));
        if(hours <= 12){
            if(hours > 0){
                showDateString = hours+"小时前";
            }else{
                if(minutes < 0){
                    minutes = 1;
                }
                showDateString = minutes+"分钟前";
            }
        }else if(date.getYear() == currentDate.getYear()){
            showDateString = sdf2.format(date);
        }


		helper.setText(R.id.tv_username,item.getUser().getName())
				.setText(R.id.tv_date,""+showDateString+"   来自  "+ Html.fromHtml(item.getSource()))
				.setText(R.id.tv_content,item.getText())
		.setText(R.id.tv_forward,""+item.getReposts_count())
				.setText(R.id.tv_comment,""+item.getComments_count())
				.setText(R.id.tv_fabulous,""+item.getAttitudes_count());
		Glide.with(mContext).load(item.getUser().getProfile_image_url()).error(mContext.getResources()
				.getDrawable(R.mipmap.ic_login)).into((ImageView) helper.getView(R.id.imgv_head));
		if(!StringUtil.isNullOrEmpty(item.getOriginal_pic())){
			Glide.with(mContext).load(item.getOriginal_pic()).into((ImageView) helper.getView(R.id.imgv_cover));
		}else{
			helper.setVisible(R.id.imgv_cover,false);
		}

	}
}
