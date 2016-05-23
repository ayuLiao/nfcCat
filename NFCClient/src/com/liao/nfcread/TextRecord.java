/**
 * 将NDEF转成TextRecord
 * 在通过方法getText得到字符串
 */
package com.liao.nfcread;

import java.util.Arrays;

import android.nfc.NdefRecord;

public class TextRecord {
	private String mText;
	private TextRecord(String text){
		mText = text;
	}
	
	public String getText(){
		return mText;
	}
	//将NDEF数据封装成TextRecord
	public static TextRecord parse(NdefRecord record){
		//是否为NDEF数据
		if(record.getTnf() != NdefRecord.TNF_WELL_KNOWN){
			return null;
		}
		if(!Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)){
			return null;
		}
		//是NDEF
		try {
			//获得整体字节流
			byte[] payload = record.getPayload();
			//判断字节流三个部分
			String textEncoding = ((payload[0] & 0x80) == 0)?"UTF-8":"UTF-16";
			int languageCodeLength = payload[0] & 0x3f;
			//获得内容
			String text = new String(payload, languageCodeLength+1, payload.length-languageCodeLength-1,textEncoding);
			return new TextRecord(text);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		
	}
}
