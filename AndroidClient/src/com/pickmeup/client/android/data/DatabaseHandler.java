package com.pickmeup.client.android.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler {
	private static final String DATABASE_NAME = "PickMeUp";
	private static final String USER_DATA_TABLENAME = "USER_DATA";
	private static final String USER_DATA_COLUMN_NAME = "name";
	private static final String USER_DATA_COLUMN_PHONENUMBER = "phoneNumber";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = String.format("create table %s ("
			+ "%s text primary key not null, %s text not null);", USER_DATA_TABLENAME, USER_DATA_COLUMN_NAME,
			USER_DATA_COLUMN_PHONENUMBER);

	private final Context context;
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + USER_DATA_TABLENAME);
			onCreate(db);
		}
	}

	public DatabaseHandler(Context ctx) {
		this.context = ctx;
	}

	public DatabaseHandler open() throws SQLException {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		databaseHelper.close();
	}

	/**
	 * @return rowId or -1 if failed
	 */
	public long storeUserData(String name, String phoneNumber) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(USER_DATA_COLUMN_NAME, name);
		initialValues.put(USER_DATA_COLUMN_PHONENUMBER, phoneNumber);

		return database.insert(USER_DATA_TABLENAME, null, initialValues);
	}
	
	public void deleteUserData() {
		open();
		database.delete(USER_DATA_TABLENAME, null, null);
		close();
	}

	public UserData getUserData() {
		UserData userData = null;
		open();
		Cursor cursor = database.query(USER_DATA_TABLENAME, new String[] { USER_DATA_COLUMN_NAME, USER_DATA_COLUMN_PHONENUMBER }, null,
				null, null, null, null);
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				cursor.moveToFirst();
				userData = new UserData(cursor.getString(0), cursor.getString(1));
			}
			cursor.close();
		}
		close();
		return userData;
	}
}

