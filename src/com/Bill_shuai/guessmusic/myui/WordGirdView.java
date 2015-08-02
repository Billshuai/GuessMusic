package com.Bill_shuai.guessmusic.myui;

import java.util.ArrayList;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.Bill_shuai.guessmusic.R;
import com.Bill_shuai.guessmusic.model.IWordButtonClickListener;
import com.Bill_shuai.guessmusic.model.WordButton;
import com.Bill_shuai.guessmusic.util.Util;

public class WordGirdView extends GridView {

   private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
   private MyGirdAdapter mMyGirdAdapter;	
   private Context mContext;	
   private Animation mScaleAnim;
   
   private IWordButtonClickListener mIWordButtonClickListener;
	//��ΪҪ�ӵ����ֹ��������棬����Ҫ�õڶ������췽��
	public WordGirdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mMyGirdAdapter = new MyGirdAdapter();
		
		this.setAdapter(mMyGirdAdapter);
	}

	public void updataData(ArrayList<WordButton> list){
		
		mArrayList = list;
		
		
		
		//������������Դ   ˢ��
		setAdapter(mMyGirdAdapter);
	}
	
	
	class  MyGirdAdapter extends BaseAdapter {
		
		public int getCount(){
			return mArrayList.size();
		}
		public Object getItem(int pos){
			return mArrayList.get(pos);
		}
	    public long getItemId(int pos){
			return pos;
		}
	    
	    //��ȡ����İ�ť
	    public View getView(int pos ,View v,ViewGroup p){
	    	final WordButton holder;
	    	if(v == null){
	    		v = Util.getView(mContext, R.layout.self_ui_button_view);
	    		holder = mArrayList.get(pos);
	    		//���ض���
	    		mScaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.scale);
	    		//�����ӳ�ʱ��
	    		mScaleAnim.setStartOffset(pos*100);
	    		
	    		
	    		holder.mIndex = pos;
	    		holder.mButton = (Button)v.findViewById(R.id.item_button);
	    		holder.mButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						mIWordButtonClickListener.onWordButtonClick(holder);
					}
				});
	    		v.setTag(holder);
	    	}else{
	    		holder = (WordButton)v.getTag();
	    	}
	    	
	    	//button��ʾ��ֵ
	    	holder.mButton.setText(holder.mWordString);
	    	//���Ŷ���
	    	v.startAnimation(mScaleAnim);
	    	return v;
	    }
	};
	/**
	 * ע������ӿ�
	 * 
	 * @param listener
	 */
	public void registOnWordButtonClick(IWordButtonClickListener listener){
		
		mIWordButtonClickListener = listener;
	}

}
