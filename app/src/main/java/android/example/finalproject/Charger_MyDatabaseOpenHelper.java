package android.example.finalproject;


import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MyDatabaseOpenHelper for save date in the database
 */
public class Charger_MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile2";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Charger";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE= "TITLE";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";
    public static final String COL_TELEPHONE = "TELEPHONE";


    /**
     *
     * @param ctx
     */
    public Charger_MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, "
                + COL_LATITUDE + " TEXT, "
                + COL_LONGITUDE + " TEXT, "
                + COL_TELEPHONE+ " TEXT)");
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }


    /**
     *
     * @param title
     * @param latitude
     * @param longitude
     * @param telephone
     * @return
     */
    public boolean addChargerData(String title, String latitude, String longitude, String telephone) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(Charger_MyDatabaseOpenHelper.COL_TITLE, title);
        newRowValues.put(Charger_MyDatabaseOpenHelper.COL_LATITUDE, latitude);
        newRowValues.put(Charger_MyDatabaseOpenHelper.COL_LONGITUDE, longitude);
        newRowValues.put(Charger_MyDatabaseOpenHelper.COL_TELEPHONE, telephone);
        Log.d("addData", "addData: Adding " + title + " , " + latitude + " , " + longitude + " , " +telephone + " to " + TABLE_NAME );

        //insert in the database:
        long result;
        if(title == null) result = -1;
        else result = db.insert(Charger_MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        if(result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * deletes a particular title
     * @param title
     * @return
     */
    public boolean deleteData(String title)
    {   SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Charger_MyDatabaseOpenHelper.TABLE_NAME, COL_TITLE + "=?", new String[]{title}) > 0;
    }


}

