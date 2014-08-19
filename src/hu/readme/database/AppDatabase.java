package hu.readme.database;

import hu.readme.StaticContextApplication;
import hu.readme.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {

    private static final String DB_FILE = "database.db";
    private static final int DB_VERSION = 1;
    
    public AppDatabase() throws IOException {
        this(null);
    }
    
    public AppDatabase(DatabaseErrorHandler errorHandler) throws IOException {
        super(StaticContextApplication.getAppContext(), DB_FILE, null, DB_VERSION, errorHandler);
        
        if(!isDatabaseExists()) {
            copyDatabaseFile();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    
    private boolean isDatabaseExists() {
        final Context context = StaticContextApplication.getAppContext();
        final File dbFile = context.getDatabasePath(DB_FILE);
        return dbFile.exists();
    }
    
    private void copyDatabaseFile() throws IOException {
        final Context context = StaticContextApplication.getAppContext();
        final File dbPath = context.getDatabasePath(DB_FILE);
        if(dbPath.mkdirs()) {
            dbPath.delete();
            dbPath.createNewFile();
        }
        
        final InputStream input = context.getAssets().open(DB_FILE);
        final OutputStream output = new FileOutputStream(dbPath);
        
        Utils.copyStream(input, output);
        
        input.close();
        output.close();
    }

}
