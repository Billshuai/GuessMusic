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
		// ȫ�����ã����ش�������װ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ������Ļ��ʾ�ޱ��⣬����������Ҫ���úã��������ٴα�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.light_layout);
		playSounds(R.raw.turnon); // ������������
		b = (ImageButton) findViewById(R.id.button01);
		Animation ani = AnimationUtils.loadAnimation(Light.this,// �������Ķ���Ч��
				R.anim.tween_ani);
		b.startAnimation(ani);

		b.setOnClickListener(new View.OnClickListener() { // ImageButton�ļ�����

			@Override
			public void onClick(View arg0) {
				if (!getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_FLASH)) {
					Toast.makeText(Light.this, "��ǰ�豸û�������", Toast.LENGTH_LONG)
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
	private void playSounds(int sid) { // ������������

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		mMediaPlayer = MediaPlayer.create(Light.this, sid);
		/* ׼������ */
		// mMediaPlayer.prepare();
		/* ��ʼ���� */
		mMediaPlayer.start();
	}
	private void openlight() {
		try {
			playSounds(R.raw.click);// �����ť������
			ca = Camera.open(); // �������
			pa = ca.getParameters();
			pa.setFlashMode(Parameters.FLASH_MODE_TORCH);// ѡ���ֵ�Ͳģʽ
			ca.setParameters(pa);
			ca.autoFocus(new Camera.AutoFocusCallback() { // �Զ��Խ������ӳɹ���
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
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
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
