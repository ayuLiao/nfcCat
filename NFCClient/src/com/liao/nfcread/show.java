package com.liao.nfcread;


import com.liao.nfcclient.R;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;

public class show extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Intent myIntent = new Intent(this,ReadNFC.class);
		myIntent.putExtras(intent);
		myIntent.setAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		startActivity(myIntent);
		finish();
	}
	
}
