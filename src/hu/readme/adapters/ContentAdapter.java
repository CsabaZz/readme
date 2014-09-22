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
                        null, null, null, null);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        final int idCol = groupCursor.getColumnIndex(AppContract.Topics._ID);
        final int topicId = groupCursor.getInt(idCol);
        return StaticContextApplication.getStaticContentResolver()
                .query(AppContract.Topics.buildContentUri(topicId), 
                        null, null, null, null);
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        final View v = mInflater.inflate(R.layout.topic, parent, false);
        return v;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        int titleCol = cursor.getColumnIndex(AppContract.Tables.TOPICS + "." + AppContract.Topics.TITLE);
        final String title = cursor.getString(titleCol);
        
        final TextView textview = (TextView) view.findViewById(R.id.topic_title);
        textview.setText(title);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild,
            ViewGroup parent) {
        final View v = mInflater.inflate(R.layout.content, parent, false);
        return v;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        int contentCol = cursor.getColumnIndex(AppContract.Contents.CONTENT);
        final String content = cursor.getString(contentCol);
        
        final WebView webview = (WebView) view.findViewById(R.id.content_webview);
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

}
