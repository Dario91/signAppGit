package com.example.signapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.database.sqlite.SQLiteDatabase;


public class SignAppMain extends Activity {
	private Uri uriData = null;
	private String signText = null;
    private int fontSize=0;
    private int color=0;
	private static int RESULT_LOAD_IMAGE = 1;
	private File filePath;
	private String fileName = "lastSignedFile.png";
	private File f = new File("MyDB.sqlite3");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sign_app_main);

        if(!f.exists()) {
            // Database
            SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS Daten (id Integer,signText VARCHAR, fontSize Integer, color Integer);");
            db.execSQL("INSERT INTO Daten VALUES(1,'change Me',12,0);");
            db.close();
        }else
        {
            openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        }

		Button button_sign = (Button) findViewById(R.id.button_sign);
		Button button_send = (Button) findViewById(R.id.button_send);

		View gestureView = this.findViewById(R.id.imgView);
		gestureView.setClickable(true);
		gestureView.setFocusable(true);

		GestureDetector.SimpleOnGestureListener gestureListener = new Gesture();
		final GestureDetector gd = new GestureDetector(this, gestureListener);

		gestureView.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionEvent) {
				gd.onTouchEvent(motionEvent);
				return false;
			}
		});

		button_send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (uriData != null) {
					sendPicture();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please pick a Picture", Toast.LENGTH_SHORT).show();
				}

			}
		});

		button_sign.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (uriData != null) {
					signPicture();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please pick a Picture", Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
		case R.id.action_settings:
			openSettings();
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

	public void openPicture() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Log.i("Gallery", "Open Gallery!");

		startActivityForResult(i, RESULT_LOAD_IMAGE);
		Log.i("Gallery", "Start new Activity");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			uriData = selectedImage;
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Log.i("load" + "", MediaStore.Images.Media.DATA);

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			Log.i("load", picturePath);
			cursor.close();
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}
	}

	public void sendPicture() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		Log.i("send", shareIntent.getAction() + "Intent SEND Loaded!");

		// shareIntent.putExtra(Intent.EXTRA_STREAM,
		// ((BitmapDrawable)((ImageView)
		// findViewById(R.id.imgView)).getDrawable()).getBitmap());// uriData
		// sollte als �bergabe wert kommen
		Uri x = Uri.fromFile(getTempFile());
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getTempFile()));
		shareIntent.setType("image/*");
		startActivity(Intent.createChooser(shareIntent,
				getResources().getText(R.string.send_to)));
		Log.i("send", "Picture 'uriData' was send!");
//		File f= new File(filePath.getPath() + "/" + fileName);
//		f.delete();
	}

	public void signPicture() {

        loadSettings();

        ImageView imageView = (ImageView) findViewById(R.id.imgView);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);

        Paint p = new Paint();
        p.setColor(color);
        p.setTextSize(fontSize);
        c.drawText(signText, 100, 100, p);

        imageView.setImageBitmap(mutableBitmap);
        saveTempImage(mutableBitmap);

	}

	private void saveTempImage(Bitmap _bitmap) {

		String filename = filePath.getPath() + "/" + fileName;
		// String str = "Hello World";
		File imageFile = new File(filename);
		Log.i("save", imageFile.getPath());

		FileOutputStream outStream;

		try {
			// fo = openFileOutput(filename, Context.MODE_PRIVATE);
			outStream = new FileOutputStream(imageFile);
			_bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
			outStream.flush();
			outStream.close();
			// fo.write(str.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "Ready to be sended",
				Toast.LENGTH_LONG).show();

	}

	private File getTempFile() {
		File[] fileNumber = filePath.listFiles();
		String[] files = filePath.list();

		Log.i("syso", new Integer(files.length).toString());

		File readedFile = new File(filePath + "/" + fileName);

		return readedFile;
	}
	
	private void openSettings() {
		Intent i = new Intent(this, SettingsMain.class);
        Bundle settings = new Bundle();
        settings.putString("signText",signText);
        settings.putInt("fontSize",fontSize);
        settings.putInt("color",color);
        i.putExtras(settings);
		startActivityForResult(i, 0);
	}

    private void loadSettings()
    {
        SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM Daten", null);

        c.moveToFirst();
        signText = c.getString(c.getColumnIndex("signText"));
        fontSize = c.getInt(c.getColumnIndex("fontSize"));
        color = c.getInt(c.getColumnIndex("color"));

        c.close();
        db.close();
    }

}