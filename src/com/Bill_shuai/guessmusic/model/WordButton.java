package com.Bill_shuai.guessmusic.model;

import android.widget.Button;

/*
 * 文字按钮
 * 
 * @author Bill_shuai
 */

public class WordButton {
	
	
	//一个字体按钮所应该有的属性
	public int mIndex;
	public boolean mIsVisiable;
	public String mWordString;
	
	public Button mButton;	
    public  WordButton(){
    	mIsVisiable = true;
    	mWordString = "";	
    }
}
