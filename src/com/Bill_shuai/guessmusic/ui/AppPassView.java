package com.Bill_shuai.guessmusic.ui;


import com.Bill_shuai.guessmusic.R;
import com.Bill_shuai.guessmusic.util.MyPlay;
import com.Bill_shuai.guessmusic.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * 通关界面
 * @author Bill_shuai
 *
 */

public class AppPassView extends Activity{
	private long mExitTime;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_pass_v);

	}
	
	//退出
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Object mHelperUtils;
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();

				} else {
					finish();
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		
		protected void onPause() {
			super.onPause();
		}
}
