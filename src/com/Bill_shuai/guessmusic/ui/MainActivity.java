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
 * ���ְ�ť
 * 
 * @author Bill_shuai
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements IWordButtonClickListener{

	private long mExitTime;
	//�𰸵�����״̬ 

	private final static int STATUS_ANSWER_RIGHT = 1;

	private final static int STATUS_ANSWER_WORNG = 2;

	private final static int STATUS_ANSWER_LACK = 3;
	//��˸����
	private final static int SPACD_TIMES = 6;
	//���öԻ��������״̬
	private final static int ID_DIALOG_DELETE_WORLD = 1;
	private final static int ID_DIALOG_TIP_ANSWER = 2;
	private final static int ID_DIALOG_LACK_COINS = 3;

	private final static int WORDS_COUNT = 24;
	//��Ƭ��ض���
	private Animation mPanAnim;
	private Animation mBarInAnim;
	private Animation mBarOutAnim;
	//�������ٶ��ȼ���
	private LinearInterpolator mLin = new LinearInterpolator();

	//��Ƭ��ť ��ť
	private ImageButton mImageButton;
	private ImageView mImageViewPan;
	private ImageView mImageViewBar;

	//���ذ�ť
	private ImageButton mImageBackButton;
	
	//�ж϶����Ƿ�Ϊ��
	private boolean isRuning = false;

	//�������ֿ�
	private ArrayList<WordButton> mAllWords;

	private ArrayList<WordButton> mSelWords;

	private WordGirdView mWordGirdView;

	//��ѡ���ֿ�UI����
	private LinearLayout mSelWordUi;

	//��ǰ����
	private Song mCurrentSong;

	//��ǰ������
	private int mCurrentStageIndex = -1;

	//���ؽ���
	private View mPassView;

	//��ǰ��ҵ�����
	private int mCurrentCions = Const.TOTAL_COINS;

	//��ҵ�View
	private TextView mViewCurrentCoins ;
	//��ǰ������
	private TextView mCurrentStagePassView ;
	//��ǰ����������

	private TextView mCurrentSongNamePassView ;

	private TextView mCurrentStageView;
	
	//����
	private FrontiaSocialShare mSocialShare;

	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//��ȡ��Ϸ����
		int[] datas = Util.readData(this);
		mCurrentStageIndex = datas[Const.INDEX_LOAD_DATA_STAGE];
		mCurrentCions = datas[Const.INDEX_LOAD_DATA_COINS];
		
		mImageButton = (ImageButton) findViewById(R.id.btn_start_play);
		mImageViewBar = (ImageView) findViewById(R.id.imageViewBar);
		mImageViewPan = (ImageView) findViewById(R.id.imageViewPan);
		
		//��ʼ���ؼ�
		mPanAnim = AnimationUtils.loadAnimation(this, R.anim.anim_pan);
		mPanAnim.setInterpolator(mLin);
		mWordGirdView = (WordGirdView)findViewById(R.id.gridview);
		mViewCurrentCoins = (TextView)findViewById(R.id.text_bar_coins);
		mViewCurrentCoins.setText(mCurrentCions+"");
		
		mImageBackButton = (ImageButton)findViewById(R.id.btn_bar_back);
	    //��ʼ���ٶȷ���
		Frontia.init(this.getApplicationContext(), Const.MY_APP_KEY);  
		
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), "319137445");
		mSocialShare.setClientId(MediaType.QZONE.toString(), "100358052");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "Ӧ��");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
		mImageContent.setTitle("����ʶ��");
		mImageContent.setContent("��ӭʹ������ʶ�裬�������߸���΢���������ע��������Ϣ��һ�ֿɵ�");
		mImageContent.setLinkUrl("http://weibo.com/houbingshuai");
		mImageContent.setImageUri(Uri.parse("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png"));
		
		//ע�����
		mWordGirdView.registOnWordButtonClick(this);
		mSelWordUi = (LinearLayout)findViewById(R.id.word_select_container);
		
		mImageBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		//�����ļ���
		mPanAnim.setAnimationListener(new AnimationListener() {

			@Override
			//������ʼ��ʱ����ִ��
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			//����������ʱ����ִ��
			public void onAnimationEnd(Animation arg0) {
				mImageViewBar.startAnimation(mBarOutAnim);
			}
		});

		mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bar_in);
		mBarInAnim.setFillAfter(true); //ֻҪô�н�����һֱ����in����
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
		mBarOutAnim.setFillAfter(true); //ֻҪô�н�����һֱ����in����
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

		//paly ��ť����Ӧ

		mImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handlerAnim();
			}
		});

		//��ʼ��ѡ������

		initCurrentStageData();

		//����ɾ����ť
		handleDeleteWord();
		//������ʾ��ť
		handleTipAnswer();
		//����ť
		handleshared();
		
	}
	/*
	 * ��ʼ���Ű�ť������ʵ��
	 */
	public void handlerAnim(){
		if(mImageViewBar != null){
			if (!isRuning) {
				isRuning = true;
				//��ʼ���˽��붯��
				mImageViewBar.startAnimation(mBarInAnim);
				mImageButton.setVisibility(View.INVISIBLE);
				//��������
				MyPlay.palySong(MainActivity.this, 
						mCurrentSong.getSongFileName());
			}
		}
	}
	
	//��ʼ������

	//obtain song information
	private Song loadStageSongInfo(int stageIndex){
		Song song = new Song();

		String[] stage = Const.SONG_INFO[stageIndex];
		song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
		song.setSongName(stage[Const.INDEX_SONG_NAME]);

		return song;
	}
	/**
	 * ���ص�ǰ�ص�����
	 */
	private void initCurrentStageData(){
		//read current song information

		mCurrentSong = loadStageSongInfo(++this.mCurrentStageIndex);
		//��ʼ����ѡ���
		mSelWords = initWordSelect();
		LayoutParams params = new LayoutParams(80, 80);
		//���ԭ���Ĵ�
		mSelWordUi.removeAllViews();
		//�����µĴ𰸿�
		for (int i = 0; i < mSelWords.size(); i++) {
			mSelWordUi.addView(mSelWords.get(i).mButton, params);
		}
		//��ʾ��ǰ�ص�����
		mCurrentStageView = (TextView)findViewById(
				R.id.text_current_stage);

		if(mCurrentStageView != null){
			mCurrentStageView.setText((mCurrentStageIndex + 1)+"");
		};
		//�������
		mAllWords = initAllWord();
		//�������� WordGridView
		mWordGirdView.updataData(mAllWords);

//		//��һ��ʼ�����Ͳ�������
//		handlerAnim();
	}

	//��ʼ����ѡ���ֿ�
	private ArrayList<WordButton> initAllWord(){
		ArrayList<WordButton>  data = new ArrayList<WordButton>();

		//������д�ѡ����
		String[] words = generateWords();

		for (int i = 0; i < WORDS_COUNT; i++) {
			WordButton  button = new WordButton();
			button.mWordString = words[i];
			data.add(button);

		}
		return data;
	}

	//��ʼ����ѡ���ֿ�
	private ArrayList<WordButton> initWordSelect(){
		ArrayList<WordButton>  data = new ArrayList<WordButton>();

		//h������д�ѡ����
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

		//��ȡ��״̬
		int checkResult =  checkAnswer();
		//�ж���
		if(checkResult == STATUS_ANSWER_RIGHT){
			//���ز���ý���
			handlePassEvent();
			handleCoins(getaddCoins());
		}else if(checkResult == STATUS_ANSWER_WORNG){
			//��˸���������û�
			sparkTheWord();
		}else if(checkResult == STATUS_ANSWER_LACK){
			//����ѡ��ʱ��������Ϊ��ɫ
			for (int i = 0; i < mSelWords.size(); i++) {
				mSelWords.get(i).mButton.setTextColor(
						Color.WHITE);
			}
		}
	}

	/**
	 * ������ؼ��¼�
	 */
	private void handlePassEvent(){
		//��ʾ���ؽ���
		mPassView = (LinearLayout)findViewById(R.id.pass_view);
		mPassView.setVisibility(View.VISIBLE);


		//ֹͣδ��ɵĶ���
		mImageViewPan.clearAnimation();
		//ֹͣ���ڲ��ŵ�����
		MyPlay.stopSong(MainActivity.this);
         //���Ž�ҵ�������
		
		MyPlay.palyTone(MainActivity.this, MyPlay.INDEX_STONE_COIN);
		//��ǰ�ص�����
		mCurrentStagePassView = (TextView)findViewById(
				R.id.text_current_stage_pass);

		if(mCurrentStagePassView != null){
			mCurrentStagePassView.setText((mCurrentStageIndex + 1)+"");
		};

		//��ʾ����������
		mCurrentStagePassView = (TextView)findViewById(
				R.id.text_current_song_name_pass);

		if(mCurrentStagePassView != null){
			mCurrentStagePassView.setText(mCurrentSong.getSongName());
		};

		//��һ�ذ�������
		ImageButton btnPassNext = (ImageButton)findViewById(R.id.
				but_next);
	   //�ɹ��������ť
		ImageButton answerRightButton = (ImageButton)findViewById(R.id.but_share);
		btnPassNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(judegAppPassed()){
					//��ȥͨ�ؽ���
					Util.startActivity(MainActivity.this,AppPassView.class);
					 
				}	else{
					//��ȥ��һ��
					mPassView.setVisibility(View.GONE);
                   
					//���عؿ�����
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
	 * �ж��Ƿ�ͨ��
	 * @return
	 */
	private boolean judegAppPassed(){

		return (mCurrentStageIndex == Const.SONG_INFO.length - 1);
	}
	private void clearTheAnswer(WordButton wordButton){
		wordButton.mButton.setText("");
		wordButton.mWordString = "";
		wordButton.mIsVisiable = false;

		//����ѡ��Ŀɼ���
		setButtonVisiable(mAllWords.get(wordButton.mIndex), View.VISIBLE);
	}

	/**
	 * ���ô�
	 *ѡ��𰸿��������
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton){
		for (int i = 0; i < mSelWords.size(); i++) {
			if(mSelWords.get(i).mWordString.length() == 0){
				//���ô����ֿ����ּ��ɼ���
				mSelWords.get(i).mButton.setText(wordButton.mWordString);
				mSelWords.get(i).mIsVisiable = true;
				mSelWords.get(i).mWordString = wordButton.mWordString;

				//��¼����
				mSelWords.get(i).mIndex = wordButton.mIndex;
				//log����


				//���ô�ѡ��ɼ���
				setButtonVisiable(wordButton, View.INVISIBLE);

				break;
			}
		}
	}
	/**
	 * ���ô�ѡ���ֿ��Ƿ�ɼ�
	 * 
	 * @param wordButton
	 * @param isVisiable
	 */
	private void setButtonVisiable(WordButton wordButton, int isVisiable){
		wordButton.mButton.setVisibility(isVisiable);

		wordButton.mIsVisiable = (isVisiable == View.VISIBLE) ? true : false;

	}

	/**
	 * �������д�ѡ����
	 * @return
	 */

	private String[] generateWords(){
		Random random = new Random();
		String[] words = new String[WORDS_COUNT]; 

		//�������
		for (int i = 0; i <mCurrentSong.getSongLength(); i++) {
			words[i] = mCurrentSong.getNameCharacters()[i] + "";
		}
		//��ȡ������ִ浽���鵱��
		for (int i = mCurrentSong.getSongLength() ; i < WORDS_COUNT ; i++) {
			words[i] = getRandomChar() + "";
		}
		//��������˳�����ȴ�����Ԫ����ѡ��һ�����һ��Ԫ�ؽ��н���
		//Ȼ���ڵڶ���֮��ѡ��һ��Ԫ����ڶ���Ԫ�ؽ��н�����ֱ�����һ��Ԫ��Ϊֹ
		//����ȷ��ÿ��Ԫ�س��� �ĸ��ʶ���1/n
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
	 * �����������
	 * @return
	 */

	private char getRandomChar(){
		String str = "";
		int hightPos;//���ֵĸ�λ
		int lowPos;//���ֵĵ�λ

		Random random = new Random();
		hightPos = 176 + Math.abs(random.nextInt(39));//�̶�������λ��  ����������Ƨ�����Բ�Ҫ87
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
	 * �𰸵ļ��
	 * @return
	 */
	private int checkAnswer(){

		//�ȼ�鳤��
		for (int i = 0; i < mSelWords.size(); i++) {
			if(mSelWords.get(i).mWordString.length() == 0){

				return STATUS_ANSWER_LACK;
			}
		}
		//�������鿴�𰸵���ȷ��

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mSelWords.size(); i++) {
			sb.append(mSelWords.get(i).mWordString);
		}
		return (sb.toString().equals(mCurrentSong.getSongName()))?
				STATUS_ANSWER_RIGHT : STATUS_ANSWER_WORNG;
	}
	/**
	 * ��˸����
	 * 
	 * ��������ʱ��
	 */
	private void sparkTheWord(){
		//��ʱ�����
		TimerTask task = new TimerTask(){
			boolean mChange = false;

			int mSpardTimes = 0;
			@Override
			public void run() {
				//UIˢ�±������߳���ʵ��
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(++mSpardTimes > SPACD_TIMES){
							return;
						}
						//ִ����˸�߼���������ֽ������
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
	 * �Զ�ѡ��һ����
	 */
	private void tipAnswer() {		
		boolean tipWord = false;
		for (int i = 0; i < mSelWords.size(); i++) {
			if (mSelWords.get(i).mWordString.length() == 0) {
				// ���ݵ�ǰ�Ĵ𰸿�����ѡ���Ӧ�����ֲ�����
				onWordButtonClick(findIsAnswerWord(i));

				tipWord = true;

				// ���ٽ������
				if (!handleCoins(-getTipCoins())) {
					// ���������������ʾ�Ի���
					showDialog(ID_DIALOG_LACK_COINS);
					return;
				}
				break;
			}
		}

		// û���ҵ��������Ĵ�
		if (!tipWord) {
			// ��˸������ʾ�û�
			sparkTheWord();
		}
	}

	/**
	 * ɾ������
	 */
	private void deleteOneWord() {
		// ���ٽ��
		if (!handleCoins(-getDeleteCoins())) {
			// ��Ҳ�������ʾ��ʾ�Ի���
			showDialog(ID_DIALOG_LACK_COINS);
			return;
		}
        //���ӽ��
		
		// �����������Ӧ��WordButton����Ϊ���ɼ�
		setButtonVisiable(findNotAnswerWord(), View.INVISIBLE);
	}

	/**
	 * �ҵ�һ�����Ǵ𰸵��ļ������ҵ�ǰ�ǿɼ���
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
	 * �ҵ�һ��������
	 *
	 * @param index ��ǰ��Ҫ����𰸿������
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
	 * �ж�ĳ�������Ƿ�Ϊ��
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
	 * ���ӻ����ָ���������
	 * @param data
	 * @return true�����ɹ� false ����ʧ��
	 */
	private boolean handleCoins(int data){
		//�鿴����Ƿ��㹻���Ƿ�ɹ�����
		if(mCurrentCions + data >0){
			mCurrentCions+=data;

			mViewCurrentCoins.setText(mCurrentCions+"");
			return true;
		}else{
			//��Ҳ���
			return false;
		}
	}
	//�������ļ����ȡɾ��������Ҫ�õĽ��
	private int getDeleteCoins(){
		return this.getResources().getInteger(R.integer.pay_delete_word);
	}
	//�������ļ����ȡ��ʾ������Ҫ�õĽ��
	private int getTipCoins(){
		return this.getResources().getInteger(R.integer.pay_tip_answer);
	}
	//�������ļ����ȡ��ʾ������Ҫ�õĽ��
		private int getaddCoins(){
			return this.getResources().getInteger(R.integer.pass_add_coin);
		}
	//�����ɾ������
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
	//�������ʾ����
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
	//����������
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
	//�Զ���AlertDialog�¼�
	//ɾ�������
	private IAlertDialogButtonListener mBtnOkDeleteWordListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// ִ���¼�
			deleteOneWord();
		}
	};
	//����ʾ
	private IAlertDialogButtonListener mBtnOkTipAnswerListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// ִ���¼�
			tipAnswer();
		}
	};
	//��Ҳ���
	private IAlertDialogButtonListener mBtnOkLackCoinsListener = 
			new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// ִ���¼�
			mSocialShare.show(MainActivity.this.getWindow().getDecorView(), 
					mImageContent, FrontiaTheme.DARK,  new ShareListener());
		}
	};
	/**
	 * ��ʾ�Ի���
	 */
	private void showConfirmDialog(int id){
		switch(id){
		case ID_DIALOG_DELETE_WORLD:
			Util.showDialog(MainActivity.this,
					"ȷ������"+getDeleteCoins()+"�����ȥ��һ�������",
					mBtnOkDeleteWordListener);
			break;
		case ID_DIALOG_TIP_ANSWER:
			Util.showDialog(MainActivity.this,
					"ȷ������"+getTipCoins()+"�����ȥ���һ����ʾ��",
					mBtnOkTipAnswerListener);
			break;
		case ID_DIALOG_LACK_COINS:
			Util.showDialog(MainActivity.this,
					"������˼��Ҳ��㣡���뼰ʱ����",
					mBtnOkLackCoinsListener);
			break;
		}
	}
	//�˳�
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
	@Override
	protected void onPause() {
		//������Ϸ����
		Util.saveData(MainActivity.this, mCurrentStageIndex -1,
				mCurrentCions);
		mImageViewPan.clearAnimation();
		mImageViewBar.clearAnimation();
		//�˳���̨�ر�����
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
