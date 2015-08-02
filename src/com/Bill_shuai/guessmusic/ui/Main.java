package com.Bill_shuai.guessmusic.ui;

import com.Bill_shuai.guessmusic.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Main extends TabActivity implements OnCheckedChangeListener{
	private TabHost tabHost;
	private RadioGroup radioderGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_navigation);
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1").setContent(new Intent(this,MainActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2").setContent(new Intent(this,Light.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(new Intent(this,Author.class)));
		radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioderGroup.setOnCheckedChangeListener(this);
		radioderGroup.check(R.id.mainTabs_radio_play);//默认第一个按钮
	}
	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
		case R.id.mainTabs_radio_play:
			tabHost.setCurrentTabByTag("1");
			break;
		case R.id.mainTabs_radio_light:
			tabHost.setCurrentTabByTag("2");
			break;
		case R.id.mainTabs_radio_me:
			tabHost.setCurrentTabByTag("3");
			break;
		}
	}
}
