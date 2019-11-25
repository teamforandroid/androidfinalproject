package android.example.finalproject;


import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MyDatabaseOpenHelper for save date in the database
 */
public class News_MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Article";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE= "TITLE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_URL = "URL";


    /**
     *
     * @param ctx
     */
    public News_MyDatabaseOpenHelper(Activity ctx){
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
                + COL_TITLE + " TEXT, " + COL_DESCRIPTION + " TEXT, " + COL_URL + " TEXT)");
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
     * @param description
     * @param url
     * @return
     */
    public boolean addData(String title, String description, String url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(News_MyDatabaseOpenHelper.COL_TITLE, title);
        newRowValues.put(News_MyDatabaseOpenHelper.COL_DESCRIPTION, description);
        newRowValues.put(News_MyDatabaseOpenHelper.COL_URL, url);
        Log.d("addData", "addData: Adding " + title + " , " + description + " , " + url + " to " + TABLE_NAME );

        //insert in the database:
        long result;
        if(title == null) result = -1;
        else result = db.insert(News_MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

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
        return db.delete(News_MyDatabaseOpenHelper.TABLE_NAME, COL_TITLE + "=?", new String[]{title}) > 0;
    }


}

