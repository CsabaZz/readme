package hu.readme.adapters;

import hu.readme.ui.ContentFragment;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TopicsAdapter extends FragmentHashStatePagerAdapter {
    
	private Cursor mCursor;

    public TopicsAdapter(FragmentManager fm, int chapterId) {
    	super(fm);
    }

	@Override
	public int getCount() {
		if(null == mCursor) {
			return 0;
		} else {
			return mCursor.getCount();
		}
	}

	@Override
	public Fragment getItem(int position) {
		if(!mCursor.moveToPosition(position)) {
			throw new IllegalArgumentException("Position must be between 0 and max!");
		}
		
    	final int topicId = mCursor.getInt(0);
		final ContentFragment f = ContentFragment.instantiate(topicId);
		return f;
	}

	public void swapCursor(Cursor data) {
		mCursor = data;
		notifyDataSetChanged();
	}

}
