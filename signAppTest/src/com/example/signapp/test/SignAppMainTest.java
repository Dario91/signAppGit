package com.example.signapp.test;

import android.content.Intent;
import android.test.AndroidTestCase;
import junit.framework.TestCase;

public class SignAppMainTest extends AndroidTestCase {
	
	public void testOpenPicture() {
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		assertNotNull(i);
	}
	
	public void testsendPicture() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		
		assertEquals(Intent.ACTION_SEND, shareIntent.getAction());
	}
}