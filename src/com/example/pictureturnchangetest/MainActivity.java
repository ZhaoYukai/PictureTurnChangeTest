package com.example.pictureturnchangetest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * ���ſ�Android�ͻ���ͼƬ���һ���
 * 
 */
public class MainActivity extends Activity {
	
	private ViewPager mViewPager = null; // android-support-v4�еĻ������
	private List<ImageView> mImageViews = null; // ������ͼƬ����

	private String[] titles; // ͼƬ����
	private int[] imageResId; // ͼƬID
	private List<View> dots; // ͼƬ�������ĵ���Щ��

	private TextView tv_title = null;
	private int currentItem = 0; // ��ǰͼƬ��������

	private ScheduledExecutorService scheduledExecutorService;
	
	
	

	// �л���ǰ��ʾ��ͼƬ
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mViewPager.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
		};
	};
	
	
	

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageResId = new int[] { 
				R.drawable.a, 
				R.drawable.b, 
				R.drawable.c, 
				R.drawable.d, 
				R.drawable.e };
		titles = new String[imageResId.length];
		titles[0] = "���������ף��ҾͲ��ܵ���";
		titles[1] = "�����ֻ��������ٳ������ϸ������˴�ϳ�";
		titles[2] = "���ر�����Ӱ�������";
		titles[3] = "������TV�������";
		titles[4] = "��Ѫ��˿�ķ�ɱ";

		mImageViews = new ArrayList<ImageView>();

		// ��ʼ��ͼƬ��Դ
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews.add(imageView);
		}

		
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		tv_title = (TextView) findViewById(R.id.id_tv_title);
		tv_title.setText(titles[0]);//

		mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
		mViewPager.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
		// ����һ������������ViewPager�е�ҳ��ı�ʱ����
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
		super.onStart();
	}
	
	

	@Override
	protected void onStop() {
		// ��Activity���ɼ���ʱ��ֹͣ�л�
		scheduledExecutorService.shutdown();
		super.onStop();
	}
	
	
	

	/**
	 * �����л�����
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % mImageViews.size();
				handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
			}
		}

	}

	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * ���ViewPagerҳ���������
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(mImageViews.get(position));
			return mImageViews.get(position);
		}

		@Override
		public void destroyItem(View view, int arg1, Object arg2) {
			((ViewPager) view).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
}