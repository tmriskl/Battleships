package com.example.battleships.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.battleships.Logic.TableRow;
import com.example.battleships.Logic.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by pc on 1/2/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BATTLESHIP_DB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Scores";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SCORE = "SCORE";
    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String COLUMN_DIFFICULTY = "DIFFICULTY";

    /*
    private static final String SQL_CREATE_ENTRIES = ""
            + "CREATE TABLE" + DBContract.FeedEntry.TABLE_NAME
            + "(" + DBContract.FeedEntry.COLUMN_ID + "INT PRIMARY KEY AUTOINCREMENT,"
            + DBContract.FeedEntry.COLUMN_NAME + "TEXT,"
            + DBContract.FeedEntry.COLUMN_SCORE + "INT,"
            + DBContract.FeedEntry.COLUMN_LOCATION + "TEXT,"
            + DBContract.FeedEntry.COLUMN_DIFFICULTY + "TEXT)" ;*/


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS" + TABLE_NAME;

    private static final HashMap<String, String> SCORE_FIELDS = new HashMap<String, String>() {
        {
            put(COLUMN_NAME, "TEXT");
            put(COLUMN_SCORE, "INTEGER");
            put(COLUMN_LOCATION, "TEXT");
            put(COLUMN_DIFFICULTY, "TEXT");                          // Neatly write fields and their types
        }
    };
    private static String diffculty = Utility.Difficulty.EASY.toString();


    SQLiteDatabase sqLiteDatabase;

/*
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    */

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, TABLE_NAME, SCORE_FIELDS);          // Create the table in a single, beautiful line.
    }

    // This function does all the dirty work
    public static void createTable(SQLiteDatabase db, String tableName, HashMap<String, String> fields) {
        String command = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT";

        for (Map.Entry<String, String> entry : fields.entrySet())
            command = command + ", " + entry.getKey() + " " + entry.getValue();

        command = command + " )";
        db.execSQL(command);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertScore(TableRow object, String difficulty) {
        boolean isInserted = false;
        sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, object.getName());
        values.put(COLUMN_SCORE, object.getScore());
        values.put(COLUMN_LOCATION, object.getLocation());
        values.put(COLUMN_DIFFICULTY, difficulty);


        if (sqLiteDatabase.insert(TABLE_NAME, null, values) != -1)
            isInserted = true;

        sqLiteDatabase.close();
        return isInserted;

    }


    /* get Single TableRow by id .*/
    public TableRow getTableRow(int id) {
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor =
                sqLiteDatabase.query(TABLE_NAME,
                        new String[]{COLUMN_ID,
                                COLUMN_NAME,
                                COLUMN_SCORE, COLUMN_LOCATION,
                                COLUMN_DIFFICULTY}, COLUMN_ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TableRow tb = new TableRow(cursor.getString(0), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
        tb.setDifficultyAndGet(cursor.getString(3));
        return tb;
    }

    // Getting All TableRows
    public List<TableRow> getAllTableRows(String diff) {
        List<TableRow> tableRowList = new LinkedList<TableRow>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // Name, Score(INT) , Location , Difficulty
                //System.out.println(cursor.getString(0)); // ID
               // System.out.println(cursor.getString(3)); // Difficulty
               // System.out.println(cursor.getString(1)); // Location
               // System.out.println(cursor.getString(4)); // Name
                //System.out.println(cursor.getString(2)); // Score

                // ID [0] , Diff [1], Location [2], Name [3] , Score [4]
                // Name,Score (int), Location
//                String difficulty = cursor.getString(1);
//                String Location =  cursor.getString(2);
//                String Name = cursor.getString(3);
//                int score = Integer.parseInt(cursor.getString(4));

                String difficulty = cursor.getString(3);
                String Location =  cursor.getString(1);
                String Name = cursor.getString(4);
                int score = Integer.parseInt(cursor.getString(2));
                TableRow tb = new TableRow( Name, score, Location);
                tb.setDifficultyAndGet(difficulty);
                if(difficulty!=null)
                    if(difficulty.equals(diff)&&(!tableRowList.contains(tb))) {
                        tableRowList.add(tb);
                    }
            } while (cursor.moveToNext());
        }

        return tableRowList;
    }
/*
    public List<TableRow> getTopTenScores(String diffculty) {
        this.diffculty = diffculty;
        String SELECT_TOPTEN = ""
                + "SELECT* TOP 10 "
                + "FROM" + TABLE_NAME
                + "WHERE" + COLUMN_DIFFICULTY + "=" + diffculty
                + "ORDER BY " + COLUMN_SCORE
                + "DESC";
        List<TableRow> list = new ArrayList<>();
        sqLiteDatabase = this.getReadableDatabase();
        String select_all = "Select * from" + TABLE_NAME;
        // Cursor cursor = sqLiteDatabase.rawQuery(select_all,null);
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_TOPTEN, null);
        cursor.move(0);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2);
                String location = cursor.getString(3);
                String diff = cursor.getString(4);
                TableRow object = new TableRow(name, score, location);
                list.add(object);

            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return list;
    }
    */


    /* Update Record - Return number of rows affected */
    public int updateData(String id, TableRow object) {
        String name = object.getName();
        int score = object.getScore();
        String location = object.getLocation();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_SCORE, score);
        contentValues.put(COLUMN_LOCATION, location);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    /* Delete Record - Delete single Score-Record from Table*/
    public void deleteData(String id) {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id});
        sqLiteDatabase.close();
    }

    public void clearDatabase(String TABLE_NAME) {
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        sqLiteDatabase.execSQL(clearDBQuery);
    }


}