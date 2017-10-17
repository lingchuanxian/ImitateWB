package cn.fjlcx.android.imitatewb.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/13
 */

public class SnackbarUtils {
	private SnackbarUtils()
	{
        /* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void showShort(View view, String msg){
		Snackbar.make(view,msg, Snackbar.LENGTH_SHORT).show();
	}

	public static void showLong(View view, String msg){
		Snackbar.make(view,msg, Snackbar.LENGTH_LONG).show();
	}

}
