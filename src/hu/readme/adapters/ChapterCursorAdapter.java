package hu.readme.adapters;

import hu.readme.R;
import hu.readme.database.AppContract;
import android.content.Context;
import android.widget.SimpleCursorAdapter;

public class ChapterCursorAdapter extends SimpleCursorAdapter {

    public ChapterCursorAdapter(Context context) {
        super(context, R.layout.drawer_list_item, null, 
                new String[] { AppContract.Chapters.TITLE }, new int[] { R.id.drawer_list_item }, 0);
    }

}
