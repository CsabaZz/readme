package hu.readme.adapters;

import hu.readme.R;
import hu.readme.database.AppContract;
import android.content.Context;

public class ChaptersAdapter extends SimpleCursorAdapter {

    public ChaptersAdapter(Context context) {
        super(context, R.layout.drawer_list_item, null, 
                new String[] { AppContract.Chapters.TITLE }, new int[] { R.id.drawer_list_item }, 0);
    }

}
