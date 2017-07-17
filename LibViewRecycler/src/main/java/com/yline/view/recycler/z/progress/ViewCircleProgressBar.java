package com.yline.view.recycler.z.progress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.yline.view.recycler.R;

/**
 * SwipeRefreshLayout 动态加载布局 环形
 *
 * @author yline 2017/5/23 -- 10:34
 * @version 1.0.0
 */
public class ViewCircleProgressBar extends ImageView
{
	private static final int shadow_color = 0x1E000000;

	private static final float shadow_x_offset = 0f;

	private static final float shadow_y_offset = 1.75f;

	private static final float shadow_radius = 3.5f;

	private static final int shadow_elevation = 4;

	private Animation.AnimationListener animatorListener;

	public static final int text_size = 9; // dp

	private final static int text_percentage_max = 100;

	// text
	private boolean isTextDraw;

	private int textPercentage;

	private int textSize;

	private Paint textPaint;

	// 背景圆 disc
	private static final int disc_color = 0xFFFAFAFA;

	private ViewMaterialProgressDrawable discDrawable;

	private int discShadowRadius;

	public ViewCircleProgressBar(Context context)
	{
		this(context, null);
	}

	public ViewCircleProgressBar(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ViewCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		// text
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewRecyclerCircleProgressBar, defStyleAttr, 0);
		final float density = context.getResources().getDisplayMetrics().density;

		int textVisible = a.getInt(R.styleable.ViewRecyclerCircleProgressBar_bar_text_visibility, 1);
		if (textVisible != 1)
		{
			isTextDraw = true;
		}
		textSize = a.getDimensionPixelOffset(R.styleable.ViewRecyclerCircleProgressBar_bar_text_size, (int) (density * text_size));
		int textColor = a.getColor(R.styleable.ViewRecyclerCircleProgressBar_bar_text_color, Color.BLACK);
		textPercentage = a.getInt(R.styleable.ViewRecyclerCircleProgressBar_bar_text_progress, 0);
		a.recycle();

		textPaint = new Paint();
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);

		init(context);
	}

	private void init(Context context)
	{
		final float density = context.getResources().getDisplayMetrics().density;
		final int shadowXOffset = (int) (density * shadow_x_offset);
		final int shadowYOffset = (int) (density * shadow_y_offset);

		discShadowRadius = (int) (density * shadow_radius);

		ShapeDrawable discShadowDrawable;
		if (elevationSupported())
		{
			discShadowDrawable = new ShapeDrawable(new OvalShape());
			ViewCompat.setElevation(this, density * shadow_elevation);
		}
		else
		{
			OvalShape ovalShape = new OvalShadow(discShadowRadius); // discDiameter
			discShadowDrawable = new ShapeDrawable(ovalShape);

			ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, discShadowDrawable.getPaint());
			discShadowDrawable.getPaint().setShadowLayer(discShadowRadius, shadowXOffset, shadowYOffset, shadow_color);

			// set padding so the inner image sits correctly within the shadow.
			final int padding = discShadowRadius;
			setPadding(padding, padding, padding, padding);
		}
		discShadowDrawable.getPaint().setColor(disc_color);
		ViewCompat.setBackground(this, discShadowDrawable);

		discDrawable = new ViewMaterialProgressDrawable(getContext(), this);
		discDrawable.setBackgroundColor(disc_color);

		super.setImageDrawable(discDrawable);
	}

	/* 是否支持阴影效果 */
	private boolean elevationSupported()
	{
		return android.os.Build.VERSION.SDK_INT >= 21;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!elevationSupported())
		{
			setMeasuredDimension(getMeasuredWidth() + discShadowRadius * 2, getMeasuredHeight() + discShadowRadius * 2);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (isTextDraw)
		{
			textPercentage = textPercentage < 0 ? 0 : textPercentage;
			textPercentage = textPercentage > text_percentage_max ? text_percentage_max : textPercentage;
			String text = textPercentage + "%";
			int x = getWidth() / 2 - text.length() * textSize / 4;
			int y = getHeight() / 2 + textSize / 4;
			canvas.drawText(text, x, y, textPaint);
		}
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		if (null != discDrawable)
		{
			this.clearAnimation();
			discDrawable.stop();
		}
	}

	@Override
	public void onAnimationStart()
	{
		super.onAnimationStart();
		if (animatorListener != null)
		{
			animatorListener.onAnimationStart(getAnimation());
		}
	}

	@Override
	public void onAnimationEnd()
	{
		super.onAnimationEnd();
		if (animatorListener != null)
		{
			animatorListener.onAnimationEnd(getAnimation());
		}
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 给外界设置的量 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
	public void updateSize()
	{
		this.setImageDrawable(null);
		discDrawable.updateSizes(ViewMaterialProgressDrawable.LARGE);
		this.setImageDrawable(discDrawable);
	}

	public void setOnAnimationListener(Animation.AnimationListener listener)
	{
		animatorListener = listener;
	}

	public void setTextDraw(boolean isTextDraw)
	{
		this.isTextDraw = isTextDraw;
	}

	/**
	 * @param isArrowShow Set to true to display the arrowhead on the progress spinner.
	 */
	public void setArrowShow(boolean isArrowShow)
	{
		discDrawable.showArrow(isArrowShow);
	}

	public boolean isArrowShow()
	{
		return discDrawable.isShowArrow();
	}

	/**
	 * @param scale Set the scale of the arrowhead for the spinner.
	 */
	public void setArrowScale(float scale)
	{
		discDrawable.setArrowScale(scale);
	}

	/**
	 * @param alpha Set the alpha of the progress spinner and associated arrowhead.
	 */
	public void setRingAlpha(int alpha)
	{
		discDrawable.setAlpha(alpha);
	}

	public void start()
	{
		if (null != discDrawable && !discDrawable.isRunning())
		{
			discDrawable.setAlpha(255);
			discDrawable.start();
		}
	}

	public void stop()
	{
		discDrawable.stop();
	}

	/**
	 * Set the amount of rotation to apply to the progress spinner.
	 *
	 * @param rotation Rotation is from [0..1]
	 */
	public void setProgressRotation(float rotation)
	{
		discDrawable.setRotation(rotation);
	}

	/**
	 * Set the start and end trim for the progress spinner arc.
	 *
	 * @param startAngle start angle
	 * @param endAngle   end angle
	 */
	public void setStartEndTrim(float startAngle, float endAngle)
	{
		discDrawable.setStartEndTrim(startAngle, endAngle);
	}

	/**
	 * Set the color resources used in the progress animation from color resources.
	 * The first color will also be the color of the bar that grows in response
	 * to a user swipe gesture.
	 *
	 * @param colorResIds
	 */
	public void setColorSchemeResources(int... colorResIds)
	{
		final Resources res = getResources();
		int[] colorRes = new int[colorResIds.length];
		for (int i = 0; i < colorResIds.length; i++)
		{
			colorRes[i] = res.getColor(colorResIds[i]);
		}
		setColorSchemeColors(colorRes);
	}

	/**
	 * Set the colors used in the progress animation. The first
	 * color will also be the color of the bar that grows in response to a user
	 * swipe gesture.
	 *
	 * @param colors
	 */
	public void setColorSchemeColors(int... colors)
	{
		if (discDrawable != null)
		{
			discDrawable.setColorSchemeColors(colors);
		}
	}

	@Override
	public void setBackgroundColor(int color)
	{
		if (getBackground() instanceof ShapeDrawable)
		{
			((ShapeDrawable) getBackground()).getPaint().setColor(color);
		}
	}

	private class OvalShadow extends OvalShape
	{
		private static final int FILL_SHADOW_COLOR = 0x3D000000;

		private RadialGradient mRadialGradient;

		private int mShadowRadius;

		private Paint mShadowPaint;

		OvalShadow(int shadowRadius)
		{
			super();
			mShadowPaint = new Paint();
			mShadowRadius = shadowRadius;
			updateRadialGradient((int) rect().width());
		}

		@Override
		protected void onResize(float width, float height)
		{
			super.onResize(width, height);
			updateRadialGradient((int) width);
		}

		@Override
		public void draw(Canvas canvas, Paint paint)
		{
			final int viewWidth = ViewCircleProgressBar.this.getWidth();
			final int viewHeight = ViewCircleProgressBar.this.getHeight();
			canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, mShadowPaint);
			canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2 - mShadowRadius, paint);
		}

		private void updateRadialGradient(int diameter)
		{
			mRadialGradient = new RadialGradient(diameter / 2, diameter / 2,
					mShadowRadius, new int[]{FILL_SHADOW_COLOR, Color.TRANSPARENT},
					null, Shader.TileMode.CLAMP);
			mShadowPaint.setShader(mRadialGradient);
		}
	}
}
