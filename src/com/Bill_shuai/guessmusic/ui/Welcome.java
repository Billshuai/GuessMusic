package com.Bill_shuai.guessmusic.ui;


import com.Bill_shuai.guessmusic.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * ��һ����ӭ���� 
 * */

public class Welcome extends Activity{
	
	/**
	 * ��дActivity�е�onCreate�ķ�����
	 * �÷�������Activity����ʱ��ϵͳ���ã���һ��Activity�������ڵĿ�ʼ��
	 * @param savedInstanceState������Activity��״̬�ġ�
	 *        Bundle���͵�������Map���͵��������ƣ�������key-value����ʽ�洢���ݵ�
	 * @return
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Window win = getWindow();//���ص�ǰActivity��Window����,Window���и�����Android���ڵĻ������Ժͻ�������
		//����״̬��
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر�����
		this.setContentView(R.layout.welcome);//���ò�����Դ
		welcome();//��ӭ����
	}
	
	
	/**
	 * ��ӭ����,2���Ӻ��л�
	 * @param 
	 * @return
	 */
	public void welcome() {
		new Thread(new Runnable() {//�����߳�
			public void run() {//ʵ��Runnable��run���������߳���
				try {
					Thread.sleep(1000);//��ӭ������ͣ2����
					Message m = new Message();//����Message����
					logHandler.sendMessage(m);//����Ϣ�ŵ���Ϣ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();//�����߳�
	}
	
	//ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н��У����Ƚ��ȳ�
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			welcome1();//��ʾlogo����
		}
	};
	/**
	 * ��ʾlogo����
	 * @param 
	 * @return
	 */
	public void welcome1() {		
		Intent it=new Intent();//ʵ����Intent
		
		it.setClass(Welcome.this, Main.class);//����Class
		startActivity(it);//����Activity
		overridePendingTransition(R.anim.fade, R.anim.hold); 
		//�ж��ǲ��ǵ�һ�ΰ�װ
//		SharedPreferences sharedPreferences=getSharedPreferences("config", Activity.MODE_PRIVATE);
//		String flag = sharedPreferences.getString("flag", "");
//		if("".equals(flag))
//		{
//			it.setClass(Welcome.this, StartInterface.class);//����Class
//			Editor editor=sharedPreferences.edit();
//			editor.putString("flag", "flag");
//			editor.commit();	
//			startActivity(it);
//			overridePendingTransition(R.anim.fade, R.anim.hold); 
//		}else {
//			it.setClass(Welcome.this, MainActivity.class);//����Class
//			startActivity(it);//����Activity
//			overridePendingTransition(R.anim.fade, R.anim.hold); 
//		}
			
    	Welcome.this.finish();//����Welcome Activity
	}
	
	/** 
	 * ���̰��������Ǵ����÷���
	 * @param keyCode�������µļ�ֵ�������� 
	 *        event�������¼��Ķ���
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==4 ){//���¡����ء�����
			android.os.Process.killProcess(android.os.Process.myPid());//�ó�����ȫ�˳�Ӧ��
		}
		return super.onKeyDown(keyCode, event);
	}
}
