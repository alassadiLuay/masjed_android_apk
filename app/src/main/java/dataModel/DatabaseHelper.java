package dataModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DigitalNet on 9/21/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "anas.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(" OnCreateHelper " ,"CREATE TABLE " + AnasContract.Semester.TABLE_NAME + " ( " +
                AnasContract.Semester._ID + " integer primary key, " +
                AnasContract.Semester.COLUMN_NAME_SEMESTER_ID + " text, " +
                AnasContract.Semester.COLUMN_NAME_SEMESTER_NAME + " text ); ");

        db.execSQL("CREATE TABLE " + AnasContract.Semester.TABLE_NAME + " ( " +
                AnasContract.Semester._ID + " integer primary key, " +
                AnasContract.Semester.COLUMN_NAME_SEMESTER_ID + " text, " +
                AnasContract.Semester.COLUMN_NAME_SEMESTER_NAME + " text ); ");

        db.execSQL("CREATE TABLE " + AnasContract.Teacher.TABLE_NAME + " ( " +
                AnasContract.Teacher._ID + " integer primary key, " +
                AnasContract.Teacher.COLUMN_NAME_TEACHER_ID + " text, "+
                AnasContract.Teacher.COLUMN_NAME_SEMESTER_ID + " text, "+
                //AnasContract.Teacher.COLUMN_NAME_STUDENT_NUMBER + " INT DEFAULT 0, "+
                AnasContract.Teacher.COLUMN_NAME_TEACHER_NAME + " text ); ");

        db.execSQL("CREATE TABLE " + AnasContract.Student.TABLE_NAME + " ( " +
                AnasContract.Student._ID + " integer primary key, " +
                AnasContract.Student.COLUMN_NAME_STUDENT_ID + " text, "+
                AnasContract.Student.COLUMN_NAME_TEACHER_ID + " text, "+
                AnasContract.Student.COLUMN_NAME_SEMESTER_ID + " text, "+
                AnasContract.Student.COLUMN_NAME_STUDENT_NAME + " text ); ");

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AnasContract.Semester.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AnasContract.Teacher.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AnasContract.Student.TABLE_NAME);
        onCreate(db);
    }

    public void Empty(SQLiteDatabase db, String table){
        db.execSQL("DELETE FROM " + table);
    }
}
