package com.fairlink.passenger.application.dbmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fairlink.passenger.util.Logger;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "application.db";
	private static final int DATABASE_VERSION = 3;
	Logger logger = new Logger(this, "application");

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用l
	@Override
	public void onCreate(SQLiteDatabase db) {
		logger.info("create application table");
		db.execSQL("CREATE TABLE IF NOT EXISTS application"
				+ "(id INTEGER PRIMARY KEY, name VARCHAR, developer VARCHAR, component_name VARCHAR, "
				+ "type VARCHAR, category VARCHAR, description VARCHAR, version VARCHAR, origin VARCHAR, is_using TINYINT)");
		db.execSQL("CREATE TABLE IF NOT EXISTS cachefile"
				+ "(key VARCHAR PRIMARY KEY, value VARCHAR)");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("CREATE TABLE IF NOT EXISTS cachefile"
				+ "(key VARCHAR PRIMARY KEY, value VARCHAR)");
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}