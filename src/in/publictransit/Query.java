package in.publictransit;

import in.publictransit.model.Database;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.Button;
import android.widget.Toast;

public class Query extends Activity
{

    private Button button;
    private AutoCompleteTextView start_stop;
    private AutoCompleteTextView end_stop;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	Database db = new Database(Query.this);
	ArrayList<String> stops = db.getStopNames("chennai");
	final ArrayAdapter<String> stopsAdapter = new ArrayAdapter<String>(
		Query.this, android.R.layout.simple_dropdown_item_1line, stops);

	setContentView(R.layout.main);
	button = (Button) findViewById(R.id.button1);

	Validator startStopValidator = new Validator() {
	    @Override
	    public boolean isValid(CharSequence text)
	    {
		if (text.equals("")
			|| stopsAdapter.getPosition(text.toString()) == -1) {
		    return false;
		}
		button.setEnabled(true);
		return true;
	    }

	    @Override
	    public CharSequence fixText(CharSequence invalidText)
	    {
		button.setEnabled(false);
		return invalidText;
	    }
	};

	Validator endStopValidator = new Validator() {
	    @Override
	    public boolean isValid(CharSequence text)
	    {
		if (text.equals("")
			|| stopsAdapter.getPosition(text.toString()) == -1) {
		    return false;
		}
		button.setText(R.string.Find_route);
		return true;
	    }

	    @Override
	    public CharSequence fixText(CharSequence invalidText)
	    {
		button.setText(R.string.Goto_stop);
		return invalidText;
	    }
	};

	start_stop = (AutoCompleteTextView) findViewById(R.id.Start_stop);
	start_stop.setAdapter(stopsAdapter);
	start_stop.setValidator(startStopValidator);
	start_stop.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,
		    int arg2, long arg3)
	    {
		button.setEnabled(true);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0)
	    {
		button.setEnabled(false);
	    }
	});

	start_stop.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		start_stop.performValidation();
	    }
	});
	start_stop.setOnFocusChangeListener(new OnFocusChangeListener() {

	    @Override
	    public void onFocusChange(View arg0, boolean arg1)
	    {
		start_stop.performValidation();
	    }
	});

	end_stop = (AutoCompleteTextView) findViewById(R.id.End_stop);
	end_stop.setAdapter(stopsAdapter);
	end_stop.setValidator(endStopValidator);
	end_stop.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,
		    int arg2, long arg3)
	    {
		button.setText(R.string.Find_route);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0)
	    {
		button.setText(R.string.Goto_stop);
	    }
	});
	end_stop.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		end_stop.performValidation();
	    }
	});
	end_stop.setOnFocusChangeListener(new OnFocusChangeListener() {

	    @Override
	    public void onFocusChange(View arg0, boolean arg1)
	    {
		end_stop.performValidation();
	    }
	});

	button.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0)
	    {
		if (button.getText().equals("Goto Stop")) {
		    Intent i = new Intent(Query.this, TransitMap.class);
		    i.putExtra("query_type", "goto_stop");
		    i.putExtra("stop_name", start_stop.getText().toString());
		    try {
			startActivity(i);
		    } catch (ActivityNotFoundException e) {
			Toast.makeText(Query.this,
				"Activity not found.\n" + e.getMessage(),
				Toast.LENGTH_LONG).show();
		    }
		} else {
		    Toast.makeText(Query.this,
			    "This feature is not implemented yet.",
			    Toast.LENGTH_LONG).show();
		}
	    }
	});
    }
}