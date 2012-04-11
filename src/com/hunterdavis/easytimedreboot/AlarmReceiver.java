package com.hunterdavis.easytimedreboot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
 
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			
			String message = "Rebooting";
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(
					context,
					"There was an error somewhere, but we still received a reboot request from Easy Timed Reboot",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();

		}
		String output = runSUcommand("reboot\n");
		 output = runSUcommand("reboot\n");
		 output = runSUcommand("reboot\n");
		 output = runSUcommand("reboot\n");;
		

		
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

}
