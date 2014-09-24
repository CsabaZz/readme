package hu.readme.adapters;

import hu.readme.R;
import hu.readme.StaticContextApplication;
import hu.readme.database.AppContract;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

public class ContentAdapter extends CursorTreeAdapter {
	
	private static final String[] TOPIC_PROJECTION = new String[] {
		AppContract.Tables.TOPICS + "." + AppContract.Topics._ID,
		AppContract.Tables.TOPICS + "." + AppContract.Topics.TITLE
	};
	
	private static final String[] CONTENT_PROJECTION = new String[] {
		AppContract.Tables.CONTENT + "." + AppContract.Contents._ID,
		AppContract.Tables.CONTENT + "." + AppContract.Contents.CONTENT
	};
    
    private LayoutInflater mInflater;

    public ContentAdapter(Context context, int chapterId) {
        this(context, chapterId, false);
    }

    public ContentAdapter(Context context, int chapterId, boolean autoRequery) {
        super(getGroupCursor(chapterId), context, autoRequery);
        mInflater = StaticContextApplication.getLayoutInflater();
    }
    
    private static Cursor getGroupCursor(int chapterId) {
        return StaticContextApplication.getStaticContentResolver()
                .query(AppContract.Chapters.buildTopicsUri(chapterId), 
                		TOPIC_PROJECTION, null, null, null);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        final int idCol = groupCursor.getColumnIndex(AppContract.Topics._ID);
        final int topicId = groupCursor.getInt(idCol);
        return StaticContextApplication.getStaticContentResolver()
                .query(AppContract.Topics.buildContentUri(topicId), 
                		CONTENT_PROJECTION, null, null, null);
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        final View v = mInflater.inflate(R.layout.topic, parent, false);
        return v;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
    	final int id = cursor.getInt(0);
        final String title = cursor.getString(1);
        
        final TextView textview = (TextView) view.findViewById(R.id.topic_title);
        textview.setText(String.format("%03d", id) + ". " + title);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild,
            ViewGroup parent) {
        final View v = mInflater.inflate(R.layout.content, parent, false);
        return v;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        final String content = cursor.getString(1);
        
        final WebView webview = (WebView) view.findViewById(R.id.content_webview);
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

}
