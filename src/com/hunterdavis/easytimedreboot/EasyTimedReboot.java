package com.hunterdavis.easytimedreboot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyTimedReboot extends Activity {
	PendingIntent sender;

	int requestCodeForIntent = 123865;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// listener for enable button
		OnClickListener enableListener = new OnClickListener() {
			public void onClick(View v) {
				Button enableButton = (Button) findViewById(R.id.enablebutton);
				Button disableButton = (Button) findViewById(R.id.disablebutton);

				// get a Calendar object with current time
				Calendar cal = Calendar.getInstance();

				// now find our timepicker
				TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
				int hour = tp.getCurrentHour();
				int minute = tp.getCurrentMinute();
				String alarm_message = "Easy Timed Reboot is now rebooting your system";

				 //add 5 minutes to the calendar object
				 //cal.add(Calendar.SECOND, 5);
				 //int theirhours = cal.get(Calendar.HOUR);
				 //int theirmins = cal.get(Calendar.MINUTE);
				 //String ourAlertString = "Our time:"+ hour + ":" + minute + " and their time: " + theirhours + ":" + theirmins;

					//Toast.makeText(getBaseContext(), ourAlertString, Toast.LENGTH_SHORT).show();
				cal.set(Calendar.HOUR, hour);
				cal.set(Calendar.MINUTE, minute);

				Intent intent = new Intent(getBaseContext(),
						AlarmReceiver.class);
				// In reality, you would want to have a static variable for the
				// request code instead of 192837
				sender = PendingIntent.getBroadcast(getBaseContext(),
						requestCodeForIntent, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// Get the AlarmManager service
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				// am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				// sender);

				am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
						(24 * 60 * 60 * 1000), sender);

				// turn on alarm
				// disable enable button
				// enable disable button
				enableButton.setEnabled(false);
				disableButton.setEnabled(true);
			}
		};

		// listener for disable button
		OnClickListener disableListener = new OnClickListener() {
			public void onClick(View v) {
				Button enableButton = (Button) findViewById(R.id.enablebutton);
				Button disableButton = (Button) findViewById(R.id.disablebutton);
				// turn off alarm
				// enable enable button
				// disable disable button
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				try {
					alarmManager.cancel(sender);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				enableButton.setEnabled(true);
				disableButton.setEnabled(false);

			}
		};

		// buttons
		Button enableButton = (Button) findViewById(R.id.enablebutton);
		enableButton.setOnClickListener(enableListener);
		Button disableButton = (Button) findViewById(R.id.disablebutton);
		disableButton.setOnClickListener(disableListener);

		OnTimeChangedListener mylocalOnTimeChangeListener = new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				Button enableButton = (Button) findViewById(R.id.enablebutton);
				Button disableButton = (Button) findViewById(R.id.disablebutton);

				// turn off alarm
				// enable enable
				// disable disable
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				try {
					alarmManager.cancel(sender);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				enableButton.setEnabled(true);
				disableButton.setEnabled(false);
			}

		};

		// alarm spinner
		TimePicker timepicker = (TimePicker) findViewById(R.id.timePicker1);
		timepicker.setOnTimeChangedListener(mylocalOnTimeChangeListener);

		// reboot button listener
		OnClickListener RebootButtonListner = new OnClickListener() {
			public void onClick(View v) {
				yesnoRebootHandler("Are you sure?",
						"Are you sure you want to reboot?");
			}
		};
		Button rebootButton = (Button) findViewById(R.id.rebootbutton);
		rebootButton.setOnClickListener(RebootButtonListner);

		lookupAlarmAndSetButtonState();

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
	}

	public void lookupAlarmAndSetButtonState() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Button enableButton = (Button) findViewById(R.id.enablebutton);
		Button disableButton = (Button) findViewById(R.id.disablebutton);

		// alarm spinner
		TimePicker timepicker = (TimePicker) findViewById(R.id.timePicker1);

		enableButton.setEnabled(true);
		disableButton.setEnabled(false);

	}

	public static String runSUcommand (String command)
	{  
	 final StringBuilder output = new StringBuilder();
	 Process a;
	 
	 BufferedReader read = null;
	 try {
	    a = Runtime.getRuntime().exec("su");   // launch the shell (i.e., either su or sh)
	    DataOutputStream b = new DataOutputStream(a.getOutputStream());
	    b.writeBytes(command + "\n");          // send the command (\n is probably not needed if your command has it already)
	    read = new BufferedReader(new InputStreamReader(a.getInputStream()), 8192);
	    b.writeBytes("exit\n");                // exit the shell
	    b.flush();                             // flush the buffer
	    String line;
	    String separator = System.getProperty("line.separator");
	 
	    while ((line = read.readLine()) != null)   // read any output the command produced
	     output.append(line + separator);
	 
	    try
	    {
	     a.waitFor();
	     if (a.exitValue() == 255)                     // error occurred, exit value 255     
	      output.append("su/root command failed");       
	    }
	    catch (InterruptedException e)
	    {
	     output.append("su/root command failed ");     // SU command failed to execute
	    }
	 } 
	 catch (Exception e)
	 {
	 }
	 
	 return output.toString();
	 }
	 
	 
	 protected void yesnoRebootHandler(String title, String mymessage) {
		new AlertDialog.Builder(this)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
									String output = runSUcommand("reboot\n");
									 output = runSUcommand("reboot\n");
									 output = runSUcommand("reboot\n");
									 output = runSUcommand("reboot\n");
								

							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

}