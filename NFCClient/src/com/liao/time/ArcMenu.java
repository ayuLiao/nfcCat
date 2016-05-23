package com.liao.time;

import com.liao.nfcclient.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;

public class ArcMenu extends ViewGroup implements OnClickListener{

	private static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM = 1;
	private static final int POS_RIGHT_TOP = 2;
	private static final int POS_RIGHT_BOTTOM = 3;
	
	//菜单位置
	private Position mPosition = Position.RIGHT_BOTTOM;//位置，默认为right_bottom
	
	//定义菜单位置，枚举类型
	public enum Position{
		LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	}
	
	private int mRadius;//菜单半径
	
	//菜单状态，打开还是关闭
	private Status mCurrentStatus = Status.CLOSE;
	//菜单状态
	public enum Status{
		OPEN,CLOSE
	}
	//菜单主按钮
	private View mCButton;
	
	private OnMenuItemClikListener mMenuItemClikListener;
	//点击子菜单单项回调
	//创建一个接口可以让用户回调
	public interface OnMenuItemClikListener{
		void onClick(View view,int pos);//在其中创建一个方法，用于mCButton和mRadius
	}
	
	public void setOnMenuItemClikListener(OnMenuItemClikListener mMenuItemClikListener){
		this.mMenuItemClikListener = mMenuItemClikListener;
	}
	
	/**
	 * 第一个构造函数调用第二个构造函数，this(context,null)
	 * 第二个构造函数调用第三个构造函数，将全部值传入第三个构造函数中
	 * 这是为了防止用户通过代码new我们的ArcMenu
	 * @param context
	 */
	public ArcMenu(Context context) {
		this(context,null);
	}
	
	public ArcMenu(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	

	public ArcMenu(Context context, AttributeSet attrs, int i) {
		super(context,attrs,i);
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, 
				getResources().getDisplayMetrics());//创建是mradius的默认值
		
		//获取自定义的属性值
		//位置的赋值
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ArcMenu,i,0);
		
		int pos = a.getInt(R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);
		switch(pos){
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
			
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
			
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
			
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		}
		
		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, 
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));
		a.recycle();//释放TypeArray,避免影响下一次使用
		
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();//得到childview的数量
		for(int i = 0;i < count; i++){
			//getChildAt(i)取到相应的i的childview
			//测量child,这样就有了宽度和高度
			measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
			
		}
	}
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed)//如果发生改变
		{
			layoutCButton();
			
			int count = getChildCount();
			
			for(int i = 0; i < count - 1; i++){
				View child = getChildAt(i+1);
				child.setVisibility(View.GONE);//子菜单一开始是隐藏的
				//count-2是因为count表示菜单个数，包括主菜单
				int cl = (int) (mRadius * Math.sin(Math.PI/2/(count-2)*i));
				int ct = (int) (mRadius * Math.cos(Math.PI/2/(count-2)*i));
				
				
				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();
				//如果菜单位置在底部左下、右下
				if(mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM){
					ct = getMeasuredHeight() - ct - cHeight;
				}
				//如果菜单位置在右上，右下
				if(mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM){
					cl = getMeasuredWidth() - cl - cWidth;
				}
				
				//加载位置
				child.layout(cl, ct, cl+cWidth, ct+cHeight);
			}
		}
	}

	private void layoutCButton() {
		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);
		
		int l = 0; //表示宽度
		int t = 0; //表示高度
		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();
		
		switch(mPosition){
		//l,t计算错误，就会导致图片无法显示出来
		case LEFT_TOP:
			l=0;
			t=0;
			break;
			
		case LEFT_BOTTOM:
			l=0;
			t=getMeasuredHeight() - height;
			break;
			
		case RIGHT_TOP:
			l = getMeasuredWidth() - width;
			t =0 ;
			break;
			
		case RIGHT_BOTTOM:
			t = getMeasuredHeight() - height;
			l=getMeasuredWidth() - width;
			break;
		}
		
		//按钮位置确定，l,t为按钮顶部，l+width和 t+width为按钮底部
		mCButton.layout(l, t, l+width, t+width);
	}

	@Override
	public void onClick(View v) {
		rotateCButton(v,0f,360f,300);//主菜单旋转实现的方法,300为时间
		toggleMenu(300);
	}

	private void rotateCButton(View v, float start, float end, int duration) {
		//参数一二为开始结束，后面的参数	以图片中心旋转
		RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(duration);
		anim.setDuration(duration);//??
		anim.setFillAfter(true);//转完后，停下来
		v.startAnimation(anim);//加载动画到view
	}

	public void toggleMenu(int duration) {
		//为menuItem添加平移动画和旋转动画
		int count = getChildCount();
		for (int i = 0; i < count-1; i++) {
			final View childView = getChildAt(i+1);
			/**
			 * 无论子菜单关闭还是打开，都要显示出来，显示出这个过程，
			 * 在利用动画监听器来监听，进行相应的隐藏
			 */
			childView.setVisibility(View.VISIBLE);
			/**
			 * 为了使按钮可以点击，我们使用的传统动画，如果移动了原来的位置，按钮就不可点击了
			 * 所以按钮的起始位置不能改变，这里我们就利用x,y轴的负值来产生弹出的假象
			 * 因为按钮本来就在那
			 */
			//子菜单的起始位置就是负的cl和ct,当然只是对左上是这样的，四个不同的角，x,y变化不一样
			int cl = (int) (mRadius * Math.sin(Math.PI/2/(count -2)*i));
			int ct = (int) (mRadius * Math.cos(Math.PI/2/(count-2)*i));
			
			int xflag = 1;
			int yflag = 1;
			
			//左上左下，子菜单其实是收回的，也就是x,y变化是负值
			if(mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM)
			{
				xflag = -1;
			}
			
			//同理
			if(mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP)
			{
				yflag = -1;
			}
			
			AnimationSet animset = new AnimationSet(true);
			
			Animation tranAnim = null;//平移动画
			//判断子菜单是关闭还是打开，使用不同x，y值的平移动画
			//当前是关闭的，to open
			if(mCurrentStatus == Status.CLOSE)
			{
				//参数依次为，开始，结束，开始，结束的移动值
				tranAnim = new TranslateAnimation(xflag*cl, 0, yflag*ct, 0);
				childView.setClickable(true);//可以点击了
				childView.setFocusable(true);//获得焦点
			}else{//当前是打开的, to close
				tranAnim = new TranslateAnimation(0, xflag*cl, 0, yflag*ct);
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			
			tranAnim.setFillAfter(true);//平移后，停下来
			tranAnim.setDuration(duration);//时间，用传入的值
			//设置动画监听事件，用于当动画结束时，将图标隐藏
			tranAnim.setStartOffset((i*100)/count);//利用i的不同，得到不同的平移速度
			
			tranAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					//如果动画结束，也就是子菜单要关闭了，将其隐藏
					if(mCurrentStatus == Status.CLOSE)
					{
						childView.setVisibility(View.GONE);
					}
				}
			});
			
			
			//旋转动画
			RotateAnimation rotateAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnim.setDuration(duration);//时间设置
			rotateAnim.setFillAfter(true);//动画播完后，停止
			
			//AnimationSet animset = new AnimationSet(true); animset来自这里
			//先增加旋转，再增加移动,不同顺序会有不同效果
			animset.addAnimation(rotateAnim);
			animset.addAnimation(tranAnim);
			
			childView.startAnimation(animset);//加载动画到childView视图中
			
			//点击，子菜单放大动画
			final int pos = i+1;//每个子菜单
			
			childView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(mMenuItemClikListener!= null)//我们写的一个回调方法，若不为空，进行回调
					{
						mMenuItemClikListener.onClick(childView, pos);
					}
					
					/**
					 * 实现动画
					 * 将pos传入，因为pos是放大图片（被点击了），其他图片变小
					 */
					
					menuItemAnim(pos-1);
					changeStatus();//切换菜单状态
					//每次执行后都要切换菜单状态
				}
			});
		}
		//要在for循环外
		changeStatus();//切换菜单状态
		//每次执行后都要切换菜单状态
	}
	/**
	 * 添加menuItem的点击动画
	 * @param pos
	 */
	private void menuItemAnim(int pos){
		for (int i = 0; i < getChildCount()-1; i++) {//getChildCount包括主菜单
			View childView = getChildAt(i+1);
			if(i == pos)//选中的view
			{
				childView.startAnimation(scaleBigAnim(300));
			}else{
				childView.startAnimation(scaleSmallAnim(300));
			}
			//因为点击了，子菜单消失，则这些属性都为false了
			childView.setClickable(false);
			childView.setFocusable(false);
		}
	}

		/**
		 * 为当前被点击的item设置变大和透明度降低的动画
		 * @param duration
		 * @return
		 */

		private Animation scaleBigAnim(int duration){
			//多个动画就要有AnimationSet，来同过add来添加动画，再加载进childview
			AnimationSet animationSet = new AnimationSet(true);
			//参数一到四指放大4倍
			ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, 
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			
			AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);//透密度
			
			animationSet.addAnimation(scaleAnim);
			animationSet.addAnimation(alphaAnim);
			
			animationSet.setDuration(duration);
			animationSet.setFillAfter(true);
			
			return animationSet;
		}
		
		private Animation scaleSmallAnim(int duration){

			//多个动画就要有AnimationSet，来同过add来添加动画，再加载进childview
				AnimationSet animationSet = new AnimationSet(true);
				//参数一到四指放大4倍
				ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				
				AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);//透密度	
				animationSet.addAnimation(scaleAnim);
				animationSet.addAnimation(alphaAnim);
				
				animationSet.setDuration(duration);
				animationSet.setFillAfter(true);
				
				return animationSet;
		}
		
		//切换菜单状态
		private void changeStatus() {
			mCurrentStatus = (mCurrentStatus == Status.CLOSE?Status.OPEN:Status.CLOSE);
			
		}
		
		//状态的判断，用于MainActivity.java中的listView下滑时判断，在将弹出的收缩回去
		public boolean isOpen()
		{
			return mCurrentStatus == Status.OPEN;
		}
		

		
}
