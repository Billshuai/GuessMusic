package com.Bill_shuai.guessmusic.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Bill_shuai.guessmusic.R;
import com.Bill_shuai.guessmusic.data.Const;
import com.Bill_shuai.guessmusic.model.IAlertDialogButtonListener;
import com.Bill_shuai.guessmusic.model.IWordButtonClickListener;
import com.Bill_shuai.guessmusic.model.Song;
import com.Bill_shuai.guessmusic.model.WordButton;
import com.Bill_shuai.guessmusic.myui.WordGirdView;
import com.Bill_shuai.guessmusic.util.MyPlay;
import com.Bill_shuai.guessmusic.util.Util;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;

/*
 * 文字按钮
 * 
 * @author Bill_shuai
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements IWordButtonClickListener{

	private long mExitTime;
	//答案的三种状态 

	private final static int STATUS_ANSWER_RIGHT = 1;

	private final static int STATUS_ANSWER_WORNG = 2;

	private final static int STATUS_ANSWER_LACK = 3;
	//闪烁次数
	private final static int SPACD_TIMES = 6;
	//调用对话框的三种状态
	private final static int ID_DIALOG_DELETE_WORLD = 1;
	private final static int ID_DIALOG_TIP_ANSWER = 2;
	private final static int ID_DIALOG_LACK_COINS = 3;

	private final static int WORDS_COUNT = 24;
	//唱片相关动画
	private Animation mPanAnim;
	private Animation mBarInAnim;
	private Animation mBarOutAnim;
	//以线性速度匀加速
	private LinearInterpolator mLin = new LinearInterpolator();

	//盘片按钮 按钮
	private ImageButton mImageButton;
	private ImageView mImageViewPan;
	private ImageView mImageViewBar;

	//返回按钮
	private ImageButton mImageBackButton;
	
	//判断动画是否为空
	private boolean isRuning = false;

	//定义文字框
	private ArrayList<WordButton> mAllWords;

	private ArrayList<WordButton> mSelWords;

	private WordGirdView mWordGirdView;

	//已选文字框UI容器
	private LinearLayout mSelWordUi;

	//当前歌曲
	private Song mCurrentSong;

	//当前关索引
	private int mCurrentStageIndex = -1;

	//过关界面
	private View mPassView;

	//当前金币的数量
	private int mCurrentCions = Const.TOTAL_COINS;

	//金币的View
	private TextView mViewCurrentCoins ;
	//当前关索引
	private TextView mCurrentStagePassView ;
	//当前歌曲的名称

	private TextView mCurrentSongNamePassView ;

	private TextView mCurrentStageView;
	
	//分享
	private FrontiaSocialShare mSocialShare;

	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//读取游戏数据
		int[] datas = Util.readData(this);
		mCurrentStageIndex = datas[Const.INDEX_LOAD_DATA_STAGE];
		mCurrentCions = datas[Const.INDEX_LOAD_DATA_COINS];
		
		mImageButton = (ImageButton) findViewById(R.id.btn_start_play);
		mImageViewBar = (ImageView) findViewById(R.id.imageViewBar);
		mImageViewPan = (ImageView) findViewById(R.id.imageViewPan);
		
		//初始化控件
		mPanAnim = AnimationUtils.loadAnimation(this, R.anim.anim_pan);
		mPanAnim.setInterpolator(mLin);
		mWordGirdView = (WordGirdView)findViewById(R.id.gridview);
		mViewCurrentCoins = (TextView)findViewById(R.id.text_bar_coins);
		mViewCurrentCoins.setText(mCurrentCions+"");
		
		mImageBackButton = (ImageButton)findViewById(R.id.btn_bar_back);
	    //初始化百度分享
		Frontia.init(this.getApplicationContext(), Const.MY_APP_KEY);  
		
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), "319137445");
		mSocialShare.setClientId(MediaType.QZONE.toString(), "100358052");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "应用");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
		mImageContent.setTitle("闻曲识歌");
		mImageContent.setContent("欢迎使用闻曲识歌，这是作者个人微博，敬请关注，最新消息，一手可得");
		mImageContent.setLinkUrl("http://weibo.com/houbingshuai");
		mImageContent.setImageUri(Uri.parse("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png"));
		
		//注册监听
		mWordGirdView.registOnWordButtonClick(this);
		mSelWordUi = (LinearLayout)findViewById(R.id.word_select_container);
		
		mImageBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		//动画的监听
		mPanAnim.setAnimationListener(new AnimationListener() {

			@Override
			//动画开始的时候动作执行
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			//动画结束的时候动作执行
			public void onAnimationEnd(Animation arg0) {
				mImageViewBar.startAnimation(mBarOutAnim);
			}
		});

		mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bar_in);
		mBarInAnim.setFillAfter(true); //只要么有结束就一直保持in动画
		mBarInAnim.setInterpolator(mLin);
		mBarInAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				mImageViewPan.startAnimation(mPanAnim);
			}
		});

		mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bar_out);
		mBarOutAnim.setFillAfter(true); //只要么有结束就一直保持in动画
		mBarOutAnim.setInterpolator(mLin);
		mBarOutAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				isRuning = false;
				mImageButton.setVisibility(View.VISIBLE);
			}
		});

		//paly 按钮的响应

		mImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handlerAnim();
			}
		});

		//初始化选择数据

		initCurrentStageData();

		//处理删除按钮
		handleDeleteWord();
		//处理提示按钮
		handleTipAnswer();
		//分享按钮
		handleshared();
		
	}
	/*
	 * 开始播放按钮动画的实现
	 */
	public void handlerAnim(){
		if(mImageViewBar != null){
			if (!isRuning) {
				isRuning = true;
				//开始播杆进入动画
				mImageViewBar.startAnimation(mBarInAnim);
				mImageButton.setVisibility(View.INVISIBLE);
				//播放音乐
				MyPlay.palySong(MainActivity.this, 
						mCurrentSong.getSongFileName());
			}
		}
	}
	
	//初始化数据

	//obtain song information
	private Song loadStageSongInfo(int stageIndex){
		Song song = new Song();

		String[] stage = Const.SONG_INFO[stageIndex];
		song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
		song.setSongName(stage[Const.INDEX_SONG_NAME]);

		return song;
	}
	/**
	 * 加载当前关的数据
	 */
	private void initCurrentStageData(){
		//read current song information

		mCurrentSong = loadStageSongInfo(++this.mCurrentStageIndex);
		//初始化已选择框
		mSelWords = initWordSelect();
		LayoutParams params = new LayoutParams(80, 80);
		//清空原来的答案
		mSelWordUi.removeAllViews();
		//增加新的答案框
		for (int i = 0; i < mSelWords.size(); i++) {
			mSelWordUi.addView(mSelWords.get(i).mButton, params);
		}
		//显示当前关的索引
		mCurrentStageView = (TextView)findViewById(
				R.id.text_current_stage);

		if(mCurrentStageView != null){
			mCurrentStageView.setText((mCurrentStageIndex + 1)+"");
		};
		//获得数据
		mAllWords = initAllWord();
		//跟新数据 WordGridView
		mWordGirdView.updataData(mAllWords);

//		//第一开始进来就播放音乐
//		handlerAnim();
	}

	//初始化待选文字框
	private ArrayList<WordButton> initAllWord(){
		ArrayList<WordButton>  data = new ArrayList<WordButton>();

		//获得所有待选文字
		String[] words = generateWords();

		for (int i = 0; i < WORDS_COUNT; i++) {
			WordButton  button = new WordButton();
			button.mWordString = words[i];
			data.add(button);

		}
		return data;
	}

	//初始化已选文字框
	private ArrayList<WordButton> initWordSelect(){
		ArrayList<WordButton>  data = new ArrayList<WordButton>();

		//h获得所有待选文字
		for (int i = 0; i < mCurrentSong.getSongLength(); i++) {

			View v = Util.getView(MainActivity.this, R.layout.self_ui_button_view);
			final WordButton  button = new WordButton();
			button.mButton = (Button)v.findViewById(R.id.item_button);
			button.mButton.setTextColor(Color.WHITE);
			button.mButton.setText("");
			button.mIsVisiable=false;
			button.mButton.setBackgroundResource(R.drawable.game_wordblank);
			data.add(button);
			button.mButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clearTheAnswer(button)	;					
				}
			});

		}
		return data;
	}
	@Override
	public void onWordButtonClick(WordButton wordButton) {
		//	Toast.makeText(MainActivity.this, wordButton.mIndex+"", Toast.LENGTH_SHORT).show();

		setSelectWord( wordButton);

		//获取答案状态
		int checkResult =  checkAnswer();
		//判读答案
		if(checkResult == STATUS_ANSWER_RIGHT){
			//过关并获得奖励
			handlePassEvent();
			handleCoins(getaddCoins());
		}else if(checkResult == STATUS_ANSWER_WORNG){
			//闪烁文字提醒用户
			sparkTheWord();
		}else if(checkResult == STATUS_ANSWER_LACK){
			//重新选择时设置文字为白色
			for (int i = 0; i < mSelWords.size(); i++) {
				mSelWords.get(i).mButton.setTextColor(
						Color.WHITE);
			}
		}
	}

	/**
	 * 处理过关及事件
	 */
	private void handlePassEvent(){
		//显示过关界面
		mPassView = (LinearLayout)findViewById(R.id.pass_view);
		mPassView.setVisibility(View.VISIBLE);


		//停止未完成的动画
		mImageViewPan.clearAnimation();
		//停止正在播放的声音
		MyPlay.stopSong(MainActivity.this);
         //播放金币掉落声音
		
		MyPlay.palyTone(MainActivity.this, MyPlay.INDEX_STONE_COIN);
		//当前关的索引
		mCurrentStagePassView = (TextView)findViewById(
				R.id.text_current_stage_pass);

		if(mCurrentStagePassView != null){
			mCurrentStagePassView.setText((mCurrentStageIndex + 1)+"");
		};

		//显示歌曲的名称
		mCurrentStagePassView = (TextView)findViewById(
				R.id.text_current_song_name_pass);

		if(mCurrentStagePassView != null){
			mCurrentStagePassView.setText(mCurrentSong.getSongName());
		};

		//下一关按键处理
		ImageButton btnPassNext = (ImageButton)findViewById(R.id.
				but_next);
	   //成功界面分享按钮
		ImageButton answerRightButton = (ImageButton)findViewById(R.id.but_share);
		btnPassNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(judegAppPassed()){
					//进去通关界面
					Util.startActivity(MainActivity.this,AppPassView.class);
					 
				}	else{
					//进去下一关
					mPassView.setVisibility(View.GONE);
                   
					//加载关卡数据
					initCurrentStageData();
				}			
			}
		});
		answerRightButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mSocialShare.show(MainActivity.this.getWindow().getDecorView(), 
						mImageContent, FrontiaTheme.DARK,  new ShareListener());
			}
		});
	}
	/**
	 * 判断是否通关
	 * @return
	 */
	private boolean judegAppPassed(){

		return (mCurrentStageIndex == Const.SONG_INFO.length - 1);
	}
	private void clearTheAnswer(WordButton wordButton){
		wordButton.mButton.setText("");
		wordButton.mWordString = "";
		wordButton.mIsVisiable = false;

		//设置选框的可见性
		setButtonVisiable(mAllWords.get(wordButton.mIndex), View.VISIBLE);
	}

	/**
	 * 设置答案
	 *选择答案框出现文字
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton){
		for (int i = 0; i < mSelWords.size(); i++) {
			if(mSelWords.get(i).mWordString.length() == 0){
				//设置答案文字框文字及可见性
				mSelWords.get(i).mButton.setText(wordButton.mWordString);
				mSelWords.get(i).mIsVisiable = true;
				mSelWords.get(i).mWordString = wordButton.mWordString;

				//记录索引
				mSelWords.get(i).mIndex = wordButton.mIndex;
				//log……


				//设置待选框可见性
				setButtonVisiable(wordButton, View.INVISIBLE);

				break;
			}
		}
	}
	/**
	 * 设置待选文字框是否可见
	 * 
	 * @param wordButton
	 * @param isVisiable
	 */
	private void setButtonVisiable(WordButton wordButton, int isVisiable){
		wordButton.mButton.setVisibility(isVisiable);

		wordButton.mIsVisiable = (isVisiable == View.VISIBLE) ? true : false;

	}

	/**
	 * 生成所有待选文字
	 * @return
	 */

	private String[] generateWords(){
		Random random = new Random();
		String[] words = new String[WORDS_COUNT]; 

		//存入歌名
		for (int i = 0; i <mCurrentSong.getSongLength(); i++) {
			words[i] = mCurrentSong.getNameCharacters()[i] + "";
		}
		//获取随机文字存到数组当中
		for (int i = mCurrentSong.getSongLength() ; i < WORDS_COUNT ; i++) {
			words[i] = getRandomChar() + "";
		}
		//打乱文字顺序：首先从所有元素中选择一个与第一个元素进行交换
		//然后在第二个之后选择一个元素与第二个元素进行交换，直到最后一个元素为止
		//这样确保每个元素出现 的概率都是1/n
		for (int i = WORDS_COUNT-1; i >=0; i--) {

			int index = random.nextInt(i+1);
			String buf = words[index];
			words[index] = words[i];
			words[i] = buf;
		}

		return words;
	}
	/**
	 * 
	 * 生成随机汉字
	 * @return
	 */

	private char getRandomChar(){
		String str = "";
		int hightPos;//汉字的高位
		int lowPos;//汉字的低位

		Random random = new Random();
		hightPos = 176 + Math.abs(random.nextInt(39));//固定的数加位置  避免生成生僻字所以不要87
		lowPos = 161 + Math.abs(random.nextInt(93));

		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();

		try {
			str = new String (b , "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.charAt(0);
	}
	/**
	 * 答案的检查
	 * @return
	 */
	private int checkAnswer(){

		//先检查长度
		for (int i = 0; i < mSelWords.size(); i++) {
			if(mSelWords.get(i).mWordString.length() == 0){

				return STATUS_ANSWER_LACK;
			}
		}
		//答案完整查看答案的正确性

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mSelWords.size(); i++) {
			sb.append(mSelWords.get(i).mWordString);
		}
		return (sb.toString().equals(mCurrentSong.getSongName()))?
				STATUS_ANSWER_RIGHT : STATUS_ANSWER_WORNG;
	}
	/**
	 * 闪烁文字
	 * 
	 * 技术：定时器
	 */
	private void sparkTheWord(){
		//定时器相关
		TimerTask task = new TimerTask(){
			boolean mChange = false;

			int mSpardTimes = 0;
			@Override
			public void run() {
				//UI刷新必须在线程中实现
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(++mSpardTimes > SPACD_TIMES){
							return;
						}
						//执行闪烁逻辑：红白文字交替出现
						for (int i = 0; i < mSelWords.size(); i++) {
							mSelWords.get(i).mButton.setTextColor(
									mChange?Color.RED:Color.WHITE);
						}
						mChange = !mChange;
					}

				});

			}}; 

			Timer timer = new Timer();	
			timer.schedule(task, 1, 150);
	}


	/**
	 * 自动选择一个答案
	 */
	private void tipAnswer() {		
		boolean tipWord = false;
		for (int i = 0; i < mSelWords.size(); i++) {
			if (mSelWords.get(i).mWordString.length() == 0) {
				// 根据当前的答案框条件选择对应的文字并填入
				onWordButtonClick(findIsAnswerWord(i));

				tipWord = true;

				// 减少金币数量
				if (!handleCoins(-getTipCoins())) {
					// 金币数量不够，显示对话框
					showDialog(ID_DIALOG_LACK_COINS);
					return;
				}
				break;
			}
		}

		// 没有找到可以填充的答案
		if (!tipWord) {
			// 闪烁文字提示用户
			sparkTheWord();
		}
	}

	/**
	 * 删除文字
	 */
	private void deleteOneWord() {
		// 减少金币
		if (!handleCoins(-getDeleteCoins())) {
			// 金币不够，显示提示对话框
			showDialog(ID_DIALOG_LACK_COINS);
			return;
		}
        //增加金币
		
		// 将这个索引对应的WordButton设置为不可见
		setButtonVisiable(findNotAnswerWord(), View.INVISIBLE);
	}

	/**
	 * 找到一个不是答案的文件，并且当前是可见的
	 *
	 * @return
	 */
	private WordButton findNotAnswerWord() {
		Random random = new Random();
		WordButton buf = null;

		while(true) {
			int index = random.nextInt(WORDS_COUNT);

			buf = mAllWords.get(index);

			if (buf.mIsVisiable && !isTheAnswerWord(buf)) {
				return buf;
			}
		}
	}
	/**
	 * 找到一个答案文字
	 *
	 * @param index 当前需要填入答案框的索引
	 * @return
	 */
	private WordButton findIsAnswerWord(int index) {
		WordButton buf = null;

		for (int i = 0; i < WORDS_COUNT; i++) {
			buf = mAllWords.get(i);

			if (buf.mWordString.equals("" + mCurrentSong.getNameCharacters()[index])) {
				return buf;
			}
		}

		return null;
	}

	/**
	 * 判断某个文字是否为答案
	 *
	 * @param word
	 * @return
	 */
	private boolean isTheAnswerWord(WordButton word) {
		boolean result = false;

		for (int i = 0; i < mCurrentSong.getSongLength(); i++) {
			if (word.mWordString.equals
					("" + mCurrentSong.getNameCharacters()[i])) {
				result = true;

				break;
			}
		}

		return result;
	}
	/**
	 * 增加或减少指定金币数量
	 * @param data
	 * @return true增减成功 false 增减失败
	 */
	private boolean handleCoins(int data){
		//查看金币是否足够，是否可供减少
		if(mCurrentCions + data >0){
			mCurrentCions+=data;

			mViewCurrentCoins.setText(mCurrentCions+"");
			return true;
		}else{
			//金币不够
			return false;
		}
	}
	//从配置文件里读取删除操作索要用的金币
	private int getDeleteCoins(){
		return this.getResources().getInteger(R.integer.pay_delete_word);
	}
	//从配置文件里读取提示操作索要用的金币
	private int getTipCoins(){
		return this.getResources().getInteger(R.integer.pay_tip_answer);
	}
	//从配置文件里读取提示操作索要用的金币
		private int getaddCoins(){
			return this.getResources().getInteger(R.integer.pass_add_coin);
		}
	//处理答案删除操作
	private void handleDeleteWord(){
		ImageButton button = (ImageButton)
				findViewById(R.id.btn_delete_word);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showConfirmDialog(ID_DIALOG_DELETE_WORLD);
			}
		});
	}
	//处理答案提示操作
	private void handleTipAnswer(){
		ImageButton button = (ImageButton)
				findViewById(R.id.btn_tip_answer);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showConfirmDialog(ID_DIALOG_TIP_ANSWER);	
			}
		});
	}
	//处理分享操作
		private void handleshared(){
			ImageButton button = (ImageButton)
					findViewById(R.id.btn_shared);
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mSocialShare.show(MainActivity.this.getWindow().getDecorView(), 
							mImageContent, FrontiaTheme.DARK,  new ShareListener());
				}
			});
		}
	//自定义AlertDialog事件
	//删除错误答案
	private IAlertDialogButtonListener mBtnOkDeleteWordListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// 执行事件
			deleteOneWord();
		}
	};
	//答案提示
	private IAlertDialogButtonListener mBtnOkTipAnswerListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// 执行事件
			tipAnswer();
		}
	};
	//金币不足
	private IAlertDialogButtonListener mBtnOkLackCoinsListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// 执行事件
			mSocialShare.show(MainActivity.this.getWindow().getDecorView(), 
					mImageContent, FrontiaTheme.DARK,  new ShareListener());
		}
	};
	/**
	 * 显示对话框
	 */
	private void showConfirmDialog(int id){
		switch(id){
		case ID_DIALOG_DELETE_WORLD:
			Util.showDialog(MainActivity.this,
					"确定花掉"+getDeleteCoins()+"个金币去除一个错误答案",
					mBtnOkDeleteWordListener);
			break;
		case ID_DIALOG_TIP_ANSWER:
			Util.showDialog(MainActivity.this,
					"确定花掉"+getTipCoins()+"个金币去获得一个提示答案",
					mBtnOkTipAnswerListener);
			break;
		case ID_DIALOG_LACK_COINS:
			Util.showDialog(MainActivity.this,
					"不好意思金币不足！！请及时购买",
					mBtnOkLackCoinsListener);
			break;
		}
	}
	//退出
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
	@Override
	protected void onPause() {
		//保存游戏数据
		Util.saveData(MainActivity.this, mCurrentStageIndex -1,
				mCurrentCions);
		mImageViewPan.clearAnimation();
		mImageViewBar.clearAnimation();
		//退出后台关闭音乐
		MyPlay.stopSong(MainActivity.this);
		super.onPause();
	}
	private class ShareListener implements FrontiaSocialShareListener {
		@Override
		public void onSuccess() {
			Log.d("Test","share success");
		}
		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test","share errCode "+errCode);
		}
		@Override
		public void onCancel() {
			Log.d("Test","cancel ");
		}
	}
}
