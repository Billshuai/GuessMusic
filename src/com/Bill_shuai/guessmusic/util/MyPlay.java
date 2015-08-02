package com.Bill_shuai.guessmusic.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
 * 音乐播放类
 * 
 * @author Bill_shuai
 *
 */
public class MyPlay {
	//音效的索引
	public static final int INDEX_STONE_ENTER  = 0;
	public static final int INDEX_STONE_CANCEL = 1;
	public static final int INDEX_STONE_COIN   = 2;
	// 音效的文件名称
	private static final String[] SONG_NAMES =
		{"enter.mp3","cancel.mp3","coin.mp3"};
	//音效
	private static MediaPlayer[] mToneMediaPalyer = new MediaPlayer[SONG_NAMES.length] ;


	//歌曲播放
	private static MediaPlayer mMusicMediaPalyer;
	/**
	 * 播放音效
	 * @param context
	 * @param index
	 */

	public static void palyTone(Context context,int index){
    
		//加载声音
		AssetManager assetManager = context.getAssets();
		if(mToneMediaPalyer[index] == null){
			mToneMediaPalyer[index] = new MediaPlayer(); 
			
			//设置数据源  只需要调用一次
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
		//需呀调用多次
		mToneMediaPalyer[index].start();
	}
	
	
	/**
	 * 歌曲播放方法
	 * @param context
	 * @param fileName
	 */
	public static void palySong(Context context,String fileName){
		if(mMusicMediaPalyer == null){
			mMusicMediaPalyer = new MediaPlayer();
		}

		//强制重置
		mMusicMediaPalyer.reset();

		//加载声音文件
		AssetManager assetManager = context.getAssets();
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
			mMusicMediaPalyer.setDataSource(fileDescriptor.getFileDescriptor(),
					fileDescriptor.getStartOffset(), 
					fileDescriptor.getLength());

			mMusicMediaPalyer.prepare();

			//声音播放
			mMusicMediaPalyer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 声音停止
	 * @param context
	 */

	public static void stopSong(Context context){
		if(mMusicMediaPalyer != null){
			mMusicMediaPalyer.stop();
		}
	}

}
