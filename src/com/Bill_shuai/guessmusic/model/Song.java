package com.Bill_shuai.guessmusic.model;

public class Song {
	//��������
	private String mSongName;
	
	//�����ļ�����
	private String mSongFileName;
	
	//�����ļ�����
    private int mSongLength;
    
    public char[] getNameCharacters(){
    	return mSongName.toCharArray();
    }
    
	public String getSongName() {
		return mSongName;
	}
	public void setSongName(String songName) {
		this.mSongName = songName;
		
		this.mSongLength = songName.length();
	}
	public String getSongFileName() {
		return mSongFileName;
	}
	public void setSongFileName(String songFileName) {
		this.mSongFileName = songFileName;
	}
	public int getSongLength() {
		return mSongLength;
	}
	
}
