package com.florianmski.chooser.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.florianmski.chooser.ChooserView;
import com.florianmski.chooser.sample.R;
import com.florianmski.chooser.ChooserView.OnChoiceListener;

public class XMLActivity extends Activity implements OnChoiceListener
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xml);

		((TextView)findViewById(R.id.weapon1))
				.setText("Ravaging Steam Pistol");
		
		((TextView)findViewById(R.id.weapon2))
				.setText("Honed Pistol");
		
		((ChooserView)findViewById(R.id.ChooserWeapon)).setChoiceListener(this);
		((ChooserView)findViewById(R.id.ChooserCarac)).setChoiceListener(this);
		((ChooserView)findViewById(R.id.ChooserSpe)).setChoiceListener(this);
	}

	@Override
	public void onChoiceMade(ChooserView cv, View v, int position, boolean fromUser) 
	{
		if(fromUser)
			Toast.makeText(this, "Click on choice " + position, Toast.LENGTH_SHORT).show();
	}

}
