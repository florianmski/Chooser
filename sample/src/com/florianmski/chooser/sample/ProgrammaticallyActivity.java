package com.florianmski.chooser.sample;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.florianmski.chooser.ChooserView;
import com.florianmski.chooser.sample.R;
import com.florianmski.chooser.ChooserView.OnChoiceListener;

public class ProgrammaticallyActivity extends Activity implements OnChoiceListener
{
	private static final int[] COLORS = {
		Color.parseColor("#33B5E5"),
		Color.parseColor("#AA66CC"),
		Color.parseColor("#99CC00"),
		Color.parseColor("#FFBB33"),
		Color.parseColor("#FF4444")
	};

	private ChooserView[] choosers = new  ChooserView[3];

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_programmatically);

		LinearLayout llChooser = (LinearLayout) findViewById(R.id.linearLayoutChooser);
		llChooser.setPadding(5, 5, 5, 5);

		choosers[0] = new ChooserView(this, this)
		.addChoice(createView(R.string.i, COLORS[0]))
		.addChoice(createView(R.string.you, COLORS[1]));
		choosers[0].setId(R.id.chooser1);

		choosers[1] = new ChooserView(this, this)
		.addChoice(createView(R.string.like, COLORS[0]))
		.addChoice(createView(R.string.hate, COLORS[1]))
		.addChoice(createView(R.string.choose, COLORS[2]));
		choosers[1].setId(R.id.chooser2);

		choosers[2] = new ChooserView(this, this)
		.addChoice(createView(R.string.lemons, COLORS[0]))
		.addChoice(createView(R.string.obama, COLORS[1]))
		.addChoice(createView(R.string.apples, COLORS[2]))
		.addChoice(createView(R.string.pikachu, COLORS[3]))
		.addChoice(createView(R.string.trains, COLORS[4]));
		choosers[2].setId(R.id.chooser3);

		Button btnRandom = new Button(this);
		btnRandom.setText("Tell me the truth!");
		btnRandom.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Random rd = new Random();
				for(ChooserView cv : choosers)
					cv.setChoice(rd.nextInt(cv.getChildCount()-1), true);
			}
		});

		llChooser.addView(choosers[0]);
		llChooser.addView(choosers[1]);
		llChooser.addView(choosers[2]);
		llChooser.addView(btnRandom);
	}

	public TextView createView(int textResId, int bgColor)
	{
		TextView tv = new TextView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.setMargins(5, 5, 5, 5);
		tv.setLayoutParams(params);
		tv.setId(textResId);
		tv.setText(textResId);
		tv.setTextColor(Color.WHITE);
		tv.setBackgroundColor(bgColor);
		tv.setGravity(Gravity.CENTER);
		tv.setHeight(250);
		return tv;
	}

	@Override
	public void onChoiceMade(ChooserView cv, View v, int position, boolean fromUser) 
	{
		if(fromUser)
			Toast.makeText(this, "Click on choice " + position, Toast.LENGTH_SHORT).show();

		String sentence = "";
		for(ChooserView chooser : choosers)
			if(chooser.getSelectedChoiceId() == -1)
				return;
			else
				sentence += getString(chooser.getSelectedChoiceId()) + " ";

		String message = "\"" + sentence.substring(0, sentence.length()-1) + "\"" + "\n";
		if(choosers[2].getSelectedChoiceId() == R.string.pikachu &&
				choosers[1].getSelectedChoiceId() != R.string.hate)
			message += "Pika! Pika!";
		else if(choosers[0].getSelectedChoiceId() == R.string.i)
			message += "No way! Me too!";
		else
			message += "That's interesting.";

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		//let the time to finish animation
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				builder.create().show();
			}
		}, 1000);
	}

}
