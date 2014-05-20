package com.example.signapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.*;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.net.*;
import android.os.*;
import android.provider.*;

public class SignAppMain extends Activity {
	private Uri uriData = null;

	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_app_main);
        
        Button button_sign = (Button)findViewById(R.id.button_sign);
        Button button_send = (Button)findViewById(R.id.button_send);
        
        button_send.setOnClickListener(new View.OnClickListener() {
            
           public void onClick(View v) {
        	   sendPicture();
           }
       });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            uriData = selectedImage;
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Log.i("syso", MediaStore.Images.Media.DATA);
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i("syso", picturePath);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        } 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.sign_app_main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.new_pic_icon:
	            openPicture();
	            break;
	        default:
	            break;
	    }
	    return true;
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sign_app_main,
					container, false);
			return rootView;
		}
	}
	
	public void openPicture(){
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	public void sendPicture(){
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uriData);// uriData sollte als übergabe wert kommen
		shareIntent.setType("image/*");
		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
	}
}