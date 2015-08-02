package com.Bill_shuai.guessmusic.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
 * ���ֲ�����
 * 
 * @author Bill_shuai
 *
 */
public class MyPlay {
	//��Ч������
	public static final int INDEX_STONE_ENTER  = 0;
	public static final int INDEX_STONE_CANCEL = 1;
	public static final int INDEX_STONE_COIN   = 2;
	// ��Ч���ļ�����
	private static final String[] SONG_NAMES =
		{"enter.mp3","cancel.mp3","coin.mp3"};
	//��Ч
	private static MediaPlayer[] mToneMediaPalyer = new MediaPlayer[SONG_NAMES.length] ;


	//��������
	private static MediaPlayer mMusicMediaPalyer;
	/**
	 * ������Ч
	 * @param context
	 * @param index
	 */

	public static void palyTone(Context context,int index){
    
		//��������
		AssetManager assetManager = context.getAssets();
		if(mToneMediaPalyer[index] == null){
			mToneMediaPalyer[index] = new MediaPlayer(); 
			
			//��������Դ  ֻ��Ҫ����һ��
			try {
				AssetFileDescriptor fileDescriptor = assetManager.openFd(
						SONG_NAMES[index]);
				mToneMediaPalyer[index].setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(), 
						fileDescriptor.getLength());
				mToneMediaPalyer[index].prepare();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//��ѽ���ö��
		mToneMediaPalyer[index].start();
	}
	
	
	/**
	 * �������ŷ���
	 * @param context
	 * @param fileName
	 */
	public static void palySong(Context context,String fileName){
		if(mMusicMediaPalyer == null){
			mMusicMediaPalyer = new MediaPlayer();
		}

		//ǿ������
		mMusicMediaPalyer.reset();

		//���������ļ�
		AssetManager assetManager = context.getAssets();
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
			mMusicMediaPalyer.setDataSource(fileDescriptor.getFileDescriptor(),
					fileDescriptor.getStartOffset(), 
					fileDescriptor.getLength());

			mMusicMediaPalyer.prepare();

			//��������
			mMusicMediaPalyer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ����ֹͣ
	 * @param context
	 */

	public static void stopSong(Context context){
		if(mMusicMediaPalyer != null){
			mMusicMediaPalyer.stop();
		}
	}

}
