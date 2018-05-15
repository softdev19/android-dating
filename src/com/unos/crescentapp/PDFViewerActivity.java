package com.unos.crescentapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PDFViewerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdfviewer);
	}
	/*public ByteBuffer readToByteBuffer(InputStream inStream) throws IOException
	{
	    long startTime = System.currentTimeMillis();
	    BufferedReader in = new BufferedReader(new InputStreamReader(this.getAssets().open("test.pdf")));
	    StringBuilder total = new StringBuilder();
	    String line;
	    while ((line = in.readLine()) != null) {
	        total.append(line);
	    }

	    int length = total.length();
	    byte[] buffer = new byte[length];
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream(length);
	    int read;
	    while (true) {
	      read = inStream.read(buffer);
	      if (read == -1)
	        break;
	      outStream.write(buffer, 0, read);
	    }
	    ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
	    long stopTime = System.currentTimeMillis();
	    mGraphView.fileMillis = stopTime-startTime;
	    return byteData;
	  }*/
}
