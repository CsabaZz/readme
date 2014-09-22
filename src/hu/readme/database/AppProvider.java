package hu.readme.database;

import hu.readme.StaticContextApplication;
import hu.readme.database.AppContract.Chapters;
import hu.readme.database.AppContract.Contents;
import hu.readme.database.AppContract.Tables;
import hu.readme.database.AppContract.Topics;
import hu.readme.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class AppProvider extends ContentProvider {
    private static final String TAG = Utils.makeTag(AppProvider.class);
    
    public static final int CHAPTERS = 100;
    public static final int CHAPTER = 101;
    public static final int TOPICS_IN_CHAPTER = 102;
    
    public static final int TOPICS = 200;
    public static final int TOPIC = 201;
    public static final int CONTENTS_IN_TOPIC = 202;

    public static final int CONTENTS = 300;
    public static final int CONTENT = 301;
    
    private AppDatabase mDatabaseHelper;
    
    private static final UriMatcher sUriMatcher = buildMatcher();
    
    public static UriMatcher buildMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AppContract.CONTENT_AUTHORITY;
        
        matcher.addURI(authority, "chapters", CHAPTERS);
        matcher.addURI(authority, "chapters/*", CHAPTER);
        matcher.addURI(authority, "chapters/*/topics", TOPICS_IN_CHAPTER);
        
        matcher.addURI(authority, "topic", TOPICS);
        matcher.addURI(authority, "topic/*", TOPIC);
        matcher.addURI(authority, "topic/*/content", CONTENTS_IN_TOPIC);
        
        matcher.addURI(authority, "content", CONTENTS);
        matcher.addURI(authority, "content/*", CONTENT);
        
        return matcher;
    }

    @Override
    public boolean onCreate() {
        try {
            mDatabaseHelper = new AppDatabase();
            return true;
        } catch(IOException ex) {
            Log.e(TAG, "Can not open or create the database file", ex);
        }
        
        return false;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHAPTERS:
                return Chapters.CONTENT_TYPE;
            case CHAPTER:
                return Chapters.ITEM_TYPE;

            case TOPICS:
                return Topics.CONTENT_TYPE;
            case TOPIC:
            case TOPICS_IN_CHAPTER:
                return Topics.ITEM_TYPE;

            case CONTENTS:
                return Contents.CONTENT_TYPE;
            case CONTENT:
            case CONTENTS_IN_TOPIC:
                return Contents.ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Log.e(TAG, "query (uri = " + uri + ")");
        
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        final Cursor c = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
        if(null != c) {
            c.setNotificationUri(StaticContextApplication.getStaticContentResolver(), uri);
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("You can't insert item into this db!");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("You can't delete item from this db!");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("You can't update item in this db!");
    }
    
    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }
    
    private SelectionBuilder buildSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHAPTERS: {
                return builder.table(Tables.CHAPTERS);
            }
            case CHAPTER: {
                final String id = Chapters.getId(uri);
                return builder.table(Tables.CHAPTERS)
                        .where(Chapters._ID + " = ?", new String[] { id });
            }

            case TOPICS: {
                return builder.table(Tables.TOPICS);
            }
            case TOPIC: {
                final String id = Topics.getId(uri);
                return builder.table(Tables.TOPICS)
                        .where(Topics._ID + " = ?", new String[] { id });
            }
            case TOPICS_IN_CHAPTER: {
                final String id = Chapters.getId(uri);
                return builder.table(Tables.TOPICS_IN_CHAPTER)
                        .where(Tables.CHAPTERS + "." + Chapters._ID + " = ?", 
                                new String[] { id });
            }

            case CONTENTS: {
                return builder.table(Tables.CONTENT);
            }
            case CONTENT: {
                final String id = Contents.getId(uri);
                return builder.table(Tables.CONTENT)
                        .where(Contents._ID + " = ?", new String[] { id });
            }
            case CONTENTS_IN_TOPIC: {
                final String id = Topics.getId(uri);
                return builder.table(Tables.CONTENT_IN_TOPIC)
                        .where(Tables.TOPICS + "." + Topics._ID + " = ?", 
                                new String[] { id });
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

}
