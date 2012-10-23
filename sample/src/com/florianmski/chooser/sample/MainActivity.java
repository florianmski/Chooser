package com.florianmski.chooser.sample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import com.florianmski.chooser.sample.R;

public class MainActivity extends ListActivity
{
	private final static String[] items = {"Programmatically", "XML"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1);
		for(String s : items)
			adapter.add(s);	
		
		getListView().setAdapter(adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				Intent i = new Intent();
				
				switch(position)
				{
				case 0:
					i.setClass(MainActivity.this, ProgrammaticallyActivity.class);
					break;
				case 1:
					i.setClass(MainActivity.this, XMLActivity.class);
					break;
				}
				
				startActivity(i);
			}
		});
	}

}
