package com.pgeeKer.loading_page.view;

import com.example.loading_page.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	private AnimationSetupCallback animationSetupCallback;
	private Matrix shaderMatrix;   //��ɫ������
	private float axleX, axleY;   //��������
	private boolean isSink;   //�ж��Ƿ��³������Ϊtrue����ɫ����ʾ����
	private boolean isSetup;
	private Drawable waveDrawable;  //���Ʋ���
	private BitmapShader shader;  //������ɫ��
	private float offsetY;

	public boolean isSetup() {
		return isSetup;
	}

	public CustomTextView(Context context) {
		super(context);
		//��ʼ��
		init();
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		shaderMatrix = new Matrix();
	}
	
	public float getAxleX() {
		return axleX;
	}

	public void setAxleX(float axleX) {
		this.axleX = axleX;
		invalidate();    //�ú�������Ҫ�����ǣ�����View�������ػ�
	}

	public float getAxleY() {
		return axleY;
	}

	public void setAxleY(float axleY) {
		this.axleY = axleY;
		invalidate();
	}
	
	public boolean isSink() {
		return isSink;
	}

	public void setSink(boolean isSink) {
		this.isSink = isSink;
	}

	public AnimationSetupCallback getAnimationSetupCallback() {
		return animationSetupCallback;
	}

	public void setAnimationSetupCallback(
			AnimationSetupCallback animationSetupCallback) {
		this.animationSetupCallback = animationSetupCallback;
	}	
	
	public interface AnimationSetupCallback {
		public void onSetupAnimation(CustomTextView customTextView);
	}
	

	@Override
	public void setTextColor(ColorStateList colors) {
		super.setTextColor(colors);
		CreateShader();
	}

	@Override
	public void setTextColor(int color) {
		super.setTextColor(color);
		CreateShader();
	}

	/**
	 * ���������뵱ǰ��������ɫ
	 * �ظ�ˮƽ��λͼ���̶���ֱ�������ɫ
	 */
	private void CreateShader() {
		if (waveDrawable == null) {
			waveDrawable = this.getResources().getDrawable(R.drawable.wave);
		}
		int waveW = waveDrawable.getIntrinsicWidth();
		int waveH = waveDrawable.getIntrinsicHeight();
		
		//��������λͼ
		Bitmap bitmap = Bitmap.createBitmap(waveW, waveH, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(getCurrentTextColor());
		waveDrawable.setBounds(0, 0, waveW, waveH);
		waveDrawable.draw(canvas);
		
		shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		getPaint().setShader(shader);
		
		//Y��ƫ����,�����ƶ�
		offsetY = (getHeight() - waveH) / 2;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		CreateShader();
		
		if (!isSetup) {
			isSetup = true;
			if (animationSetupCallback != null) {
				animationSetupCallback.onSetupAnimation(CustomTextView.this);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		//�����³�״̬�޸����ֵ���ɫ
		if (isSink && shader != null) {
			if (getPaint().getShader() == null) {
				getPaint().setShader(shader);
			}
			
			shaderMatrix.setTranslate(axleX, axleY + offsetY);
			shader.setLocalMatrix(shaderMatrix);
		}else {
			getPaint().setShader(null);
		}
		super.draw(canvas);
	}
}
