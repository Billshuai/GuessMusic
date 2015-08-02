package com.Bill_shuai.guessmusic.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.Bill_shuai.guessmusic.R;
import com.Bill_shuai.guessmusic.data.Const;
import com.Bill_shuai.guessmusic.model.IAlertDialogButtonListener;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Util {

	private static AlertDialog mAlertDialog;

	public static View getView(Context context,int layoutId){
		LayoutInflater inflater = (LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(layoutId, null);

		return layout; 



	}
	/**
	 * ������ת
	 * @param context
	 * @param desti
	 */
	public static void startActivity(Context context,Class desti){
		Intent intent = new Intent();
		intent.setClass(context, desti);
		context.startActivity(intent);

		//�رյ�ǰ��activity
		((Activity) context).finish();
	} 
	/**
	 * ��ʾ�Զ���Ի���
	 * @param context
	 * @param message
	 * @param listener
	 */
	public static void showDialog(final Context context,
			String message,
			final IAlertDialogButtonListener listener){

		View dialogView = null;

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialogView = getView(context, R.layout.dialog_view);

		ImageButton btnOkView  = (ImageButton)dialogView.findViewById(
				R.id.btn_dialog_ok);
		ImageButton btnCancelView  = (ImageButton)dialogView.findViewById(
				R.id.btn_dialog_cancel);
		TextView textMessageView = (TextView)dialogView.findViewById(
				R.id.text_dialog_message);
		textMessageView.setText(message);
		btnOkView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//�رնԻ���
				if(mAlertDialog != null){
					mAlertDialog.cancel();
				}
				//�¼��ص�
				if (listener != null) {
					listener.onClick();
				}
				//������Ч
				MyPlay.palyTone(context, MyPlay.INDEX_STONE_ENTER);
			}
		});
       btnCancelView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//�رնԻ���
				if(mAlertDialog != null){
					mAlertDialog.cancel();
				}
				//������Ч
				MyPlay.palyTone(context, MyPlay.INDEX_STONE_CANCEL);
			}
		});
       //Ϊdialog����view
       builder.setView(dialogView);
       mAlertDialog = builder.create();
       //��ʾ�Ի���
       mAlertDialog.show();
	}
	/**
	 * ��Ϸ��������  �洢Ŀ¼��data/data/com.Bill_shuai.guessmusic/filter
	 * @param context
	 * @param stageIndex
	 * @param coins
	 */
	public static void saveData(Context context,
			int stageIndex,int coins ){
		FileOutputStream fos = null;
		
		try {
			fos = context.openFileOutput(Const.FILE_NAME_SAVE_DATA
					, Context.MODE_PRIVATE);
			//�ܵ�
			DataOutputStream dos = new DataOutputStream(fos);
			
			dos.writeInt(stageIndex);
			dos.writeInt(coins);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fos != null){
				fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * ��Ϸ��ȡ����
	 * @param context
	 * @return
	 */
	 
	 
	public static int[] readData(Context context ){
		FileInputStream fis = null;
		
		int[] datas = {-1 , Const.TOTAL_COINS};
		
		try {
			fis = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
			
			//�ܵ�
			DataInputStream dis = new DataInputStream(fis);
			datas[Const.INDEX_LOAD_DATA_STAGE] = dis.readInt();
			datas[Const.INDEX_LOAD_DATA_COINS] = dis.readInt();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return datas;
	}
	
	 public boolean delete(File SDPATH,String fileName) {
	    	
	    	//SDPATHĿ¼·����fileName�ļ���
	    	SDPATH = Environment.getExternalStorageDirectory();
	        File file = new File(SDPATH + "/" + Const.FILE_NAME_SAVE_DATA);  
	        if (file == null || !file.exists() || file.isDirectory()){  
	            return false;  
	        }
	        file.delete();
	        
	        return true;
	    }  
}
