package com.Bill_shuai.guessmusic.model;

import android.widget.Button;

/*
 * ���ְ�ť
 * 
 * @author Bill_shuai
 */

public class WordButton {
	
	
	//һ�����尴ť��Ӧ���е�����
	public int mIndex;
	public boolean mIsVisiable;
	public String mWordString;
	
	public Button mButton;	
    public  WordButton(){
    	mIsVisiable = true;
    	mWordString = "";	
    }
}
