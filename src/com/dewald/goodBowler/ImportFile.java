package com.dewald.goodBowler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;


public class ImportFile {

	File sdcard = Environment.getExternalStorageDirectory();
	File data = Environment.getDataDirectory();
	
	String currentDBPath = "data//com.dewald.goodBowler//databases//bowlerdata";
	String backupDBPath = "bowlerdata";
	
	public boolean exportData(){
		Log.v("sdcard", sdcard.toString());
		Log.v("sdcard", data.toString());
		boolean export = false;
		try {
		if(sdcard.canWrite()) {
			File currentDB = new File(data, currentDBPath);
			File backupDB = new File(sdcard, backupDBPath);
			Log.v("canWrite", "canWrite");
			Log.v("currentDB", currentDB.toString());
			
			if(currentDB.exists()){
				FileChannel src = new FileInputStream(backupDB).getChannel();
				FileChannel dst = new FileOutputStream(currentDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				export = true;
				Log.v("currentDB", "exists");
			}
		}
		}catch(Exception e){
			Log.v("Exception", e.toString());
		}
		return export;
	}
}
