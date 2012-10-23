package com.florianmski.chooser;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class ChooserView extends LinearLayout implements OnClickListener
{
	private static final int ANIM_DURATION = 500;

	protected OnChoiceListener listener;
	protected boolean choiceMade = false;
	protected View selectedChoice = null;

	public ChooserView(Context context, OnChoiceListener listener) 
	{
		super(context);
		init();
		setChoiceListener(listener);
	}

	public ChooserView(Context context) 
	{
		super(context);
		init();
	}

	public ChooserView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init();
	}

	protected void init()
	{
		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params)
	{
		super.addView(child, index, params);
		child.setOnClickListener(this);
		((LinearLayout.LayoutParams)child.getLayoutParams()).weight = 1;
	}
	
	/**
	 * Add a choice to the chooser
	 * @param v The view you want to add as a choice
	 * @return
	 */
	public ChooserView addChoice(View v)
	{
		this.addView(v);
		return this;
	}

	/**
	 * Register a callback to be invoked when a choice is clicked.
	 * @param listener The callback that will run
	 */
	public void setChoiceListener(OnChoiceListener listener)
	{
		this.listener = listener;
	}

	/**
	 * Undo the current choice
	 * @param animate If the chooser should animate the undo or not
	 */
	public void undo(boolean animate)
	{
		if(choiceMade)
			onUndo(selectedChoice, animate);
	}

	/**
	 * Select a choice based on his position
	 * @param position The position of the choice you want to be selected
	 * @param animate If the chooser should animate the undo or not
	 */
	public void setChoice(final int position, final boolean animate)
	{
		if(position < 0 || position >= this.getChildCount())
			return;

		undo(animate);
		this.postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				onChoiceMade(getChildAt(position), animate, false);
			}
		}, animate && choiceMade ? ANIM_DURATION : 0);
	}

	/**
	 * Returns if a choice has been made or not
	 * @return
	 */
	public boolean isChoiceSelected()
	{
		return choiceMade;
	}
	
	/**
	 * Returns the view id of the selected choice or -1 if no choice has been made
	 * @return
	 */
	public int getSelectedChoiceId()
	{
		return selectedChoice == null ? -1 : selectedChoice.getId();
	}

	/**
	 * Returns the view position of the selected choice or -1 if no choice has been made
	 * @return
	 */
	public int getSelectedChoicePosition()
	{
		return selectedChoice == null ? -1 : this.indexOfChild(selectedChoice);
	}

	public interface OnChoiceListener
	{
		public void onChoiceMade(ChooserView cv, View v, int position, boolean fromUser);
	}

	protected void onChoiceMade(View v, boolean animate, boolean fromUser)
	{
		choiceMade = true;
		selectedChoice = v;
		
		float toX = (float)(this.getWidth() - v.getRight() - v.getLeft()) / 2.0f;

		start(0.0f, toX, 1.0f, 0.0f, animate);

		if(listener != null)
			listener.onChoiceMade(this, v, this.indexOfChild(v), fromUser);
	}

	protected void onUndo(View v, boolean animate)
	{
		choiceMade = false;
		float fromX = (float)(this.getWidth() - selectedChoice.getRight() - selectedChoice.getLeft()) / 2.0f;

		start(fromX, 0.0f, 0.0f, 1.0f, animate);

		selectedChoice = null;
	}

	protected void start(float fromX, float toX, float fromAlpha, float toAlpha, boolean animate)
	{
		int duration = animate ? ANIM_DURATION : 0;
		
		TranslateAnimation ta = new TranslateAnimation(fromX, toX, 0.f, 0.f);
		ta.setDuration(duration);

		AlphaAnimation aa = new AlphaAnimation(fromAlpha, toAlpha);
		aa.setDuration(duration);

		for(int i = 0; i < this.getChildCount(); i++)
		{
			final View vChoice = this.getChildAt(i);

			AnimationSet as = new AnimationSet(true);
			as.setDuration(duration);
			as.setInterpolator(new DecelerateInterpolator());
			as.setFillAfter(true);
			as.addAnimation(ta);

			if(vChoice != selectedChoice)
				as.addAnimation(aa);

			vChoice.startAnimation(as);
		}
	}

	@Override
	public void onClick(View v) 
	{		
		if(choiceMade)
			onUndo(v, true);
		else
			onChoiceMade(v, true, true);
	}

	@Override
	public Parcelable onSaveInstanceState() 
	{
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putInt("choicePosition", this.getSelectedChoicePosition());

		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) 
	{
		if (state instanceof Bundle) 
		{
			Bundle bundle = (Bundle) state;
			int choicePosition = bundle.getInt("choicePosition");
			if(choicePosition != -1)
				setChoice(choicePosition, false);
			
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
			return;
		}

		super.onRestoreInstanceState(state);
	}
}
