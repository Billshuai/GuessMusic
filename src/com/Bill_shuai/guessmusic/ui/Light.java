package com.Bill_shuai.guessmusic.ui;

import com.Bill_shuai.guessmusic.R;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Light extends Activity {
	private MediaPlayer mMediaPlayer;
	ImageButton b = null;
	private Camera ca = null;
	private Parameters pa = null;
	public static boolean state = true;
	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏设置，隐藏窗口所有装饰
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.light_layout);
		playSounds(R.raw.turnon); // 进入界面的音乐
		b = (ImageButton) findViewById(R.id.button01);
		Animation ani = AnimationUtils.loadAnimation(Light.this,// 进入界面的动画效果
				R.anim.tween_ani);
		b.startAnimation(ani);

		b.setOnClickListener(new View.OnClickListener() { // ImageButton的监听器

			@Override
			public void onClick(View arg0) {
				if (!getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_FLASH)) {
					Toast.makeText(Light.this, "当前设备没有闪光灯", Toast.LENGTH_LONG)
							.show();
					return;
				}
				// TODO Auto-generated method stub
				else if (state) {
					openlight();
				} else {
					closelight();
				}
			}
		});
	}
	private void playSounds(int sid) { // 播放声音代码

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		mMediaPlayer = MediaPlayer.create(Light.this, sid);
		/* 准备播放 */
		// mMediaPlayer.prepare();
		/* 开始播放 */
		mMediaPlayer.start();
	}
	private void openlight() {
		try {
			playSounds(R.raw.click);// 点击按钮的声音
			ca = Camera.open(); // 打开照相机
			pa = ca.getParameters();
			pa.setFlashMode(Parameters.FLASH_MODE_TORCH);// 选择手电筒模式
			ca.setParameters(pa);
			ca.autoFocus(new Camera.AutoFocusCallback() { // 自动对焦，增加成功率
				public void onAutoFocus(boolean success, Camera camera) {
				}
			});
			ca.startPreview();
			state = false;
			b.setBackground(getResources().getDrawable(R.drawable.on));
		} catch (Exception e) {
		}
	}
	private void closelight() {
		
			if(ca != null){
			pa = ca.getParameters();
			pa.setFlashMode(Parameters.FLASH_MODE_OFF);
			ca.setParameters(pa);
			ca.stopPreview();
			ca.release();
			ca = null;
			b.setBackground(getResources().getDrawable(R.drawable.off));
			state = true;
		}	
	}
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
	public void onPause() {
		super.onPause();
		closelight();
	}
}
