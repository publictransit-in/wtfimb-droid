package in.publictransit.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.com.bytecode.opencsv.CSVReader;

public class Database
{
    private static final String DATABASE_NAME = "publictransit_in.db";
    private static final int DATABASE_VERSION = 1;

    private static final String STOPS_TABLE_NAME = "stop";

    private static final String STOPS_COLUMN_NAME = "name";
    /**
     * Latitude in microdegrees. i.e., Latitude in degrees * 1E6
     */
    private static final String STOPS_COLUMN_LATITUDE = "latitude_mcd";
    /**
     * Longitude in microdegrees. i.e., Latitude in degrees * 1E6
     */
    private static final String STOPS_COLUMN_LONGITUDE = "longitude_mcd";
    private static final String STOPS_COLUMN_CITY = "city";

    private DatabaseHelper dbHelper;

    public Database(Context context) {
	this.dbHelper = new DatabaseHelper(context);
	this.dbHelper.getWritableDatabase();
    }

    public ArrayList<String> getStopNames(String cityName)
    {
	SQLiteDatabase db = dbHelper.getReadableDatabase();
	Cursor c = db.query(STOPS_TABLE_NAME,
		new String[] { STOPS_COLUMN_NAME }, STOPS_COLUMN_CITY
			+ " = 'chennai'", null, null, null, STOPS_COLUMN_NAME);
	ArrayList<String> stopNames = new ArrayList<String>();
	while (c.moveToNext()) {
	    stopNames.add(c.getString(0));
	}
	return stopNames;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
	Context context;

	public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	    db.execSQL("CREATE TABLE " + STOPS_TABLE_NAME + " ( "
		    + STOPS_COLUMN_NAME + " VARCHAR(255), "
		    + STOPS_COLUMN_LATITUDE + " INTEGER, "
		    + STOPS_COLUMN_LONGITUDE + " INTEGER, " + STOPS_COLUMN_CITY
		    + " VARCHAR(255) );");
	    AssetManager am = context.getResources().getAssets();
	    InputStream is = null;
	    try {
		is = am.open("chennai/mtc/stops.csv");
		CSVReader csr = new CSVReader(new InputStreamReader(is));
		ContentValues stopRow = new ContentValues();
		for (String[] row : csr.readAll()) {
		    if (row[0].equals(""))
			continue;
		    stopRow.put(STOPS_COLUMN_NAME, row[0]);
		    if (!row[1].equals(""))
			stopRow.put(STOPS_COLUMN_LATITUDE,
				(int) (Double.valueOf(row[1]) * 1E6));
		    if (!row[2].equals(""))
			stopRow.put(STOPS_COLUMN_LONGITUDE,
				(int) (Double.valueOf(row[2]) * 1E6));
		    stopRow.put(STOPS_COLUMN_CITY, "chennai");
		    db.insert(STOPS_TABLE_NAME, null, stopRow);
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    } finally {
		if (is != null)
		    try {
			is.close();
		    } catch (IOException e) {
		    }
	    }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    onCreate(db);
	}
    }

    public GeoPoint getLocation(String stopName)
    {
	SQLiteDatabase db = dbHelper.getReadableDatabase();
	Cursor c = db.query(STOPS_TABLE_NAME, new String[] {
		STOPS_COLUMN_LATITUDE, STOPS_COLUMN_LONGITUDE },
		STOPS_COLUMN_NAME + " = '" + stopName + "'", null, null, null,
		null);
	if (c.moveToNext()) {
	    try {
		if (c.getInt(0) == 0 || c.getInt(1) == 0) {
		    return null;
		}
		return new GeoPoint(c.getInt(0), c.getInt(1));
	    } catch (Exception e) {
		return null;
	    }
	} else {
	    return null;
	}
    }
}