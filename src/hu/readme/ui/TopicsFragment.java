package hu.readme.ui;

import hu.readme.R;
import hu.readme.adapters.TopicsAdapter;
import hu.readme.database.AppContract;
import hu.readme.utils.LoaderUtils;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String[] PROJECTION = new String[] {
		AppContract.Tables.TOPICS + "." + AppContract.Topics._ID, 
		AppContract.Tables.TOPICS + "." + AppContract.Topics.TITLE
	};
    
    private static final String EXTRA_BASE = TopicsFragment.class.getName() + "::";
    public static final String EXTRA_CHAPTER_ID = EXTRA_BASE + "ChapterId";

    public static Fragment newInstance(int chapterId) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_CHAPTER_ID, chapterId);
        
        final TopicsFragment f = new TopicsFragment();
        f.setArguments(args);
        return f;
    }
    
    private int mChapterId;

    private ViewPager mViewPager;
    private TopicsAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((LaunchActivity) activity).onSectionAttached(
                getArguments().getInt(EXTRA_CHAPTER_ID));
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChapterId = getArguments().getInt(EXTRA_CHAPTER_ID);
        mAdapter = new TopicsAdapter(getChildFragmentManager(), mChapterId);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.chapter_content, container, false);
        mViewPager = (ViewPager) contentView.findViewById(R.id.chapter_content_pager);
        return contentView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(mAdapter);
		LoaderUtils.setLoader(getLoaderManager(), 
				LoaderUtils.LOADER_TOPICS, null, this);
	}
    
    @Override
    public void onDestroyView() {
        if(null != mViewPager) {
        	mViewPager.setAdapter(null);
            mViewPager = null;
        }
        
        super.onDestroyView();
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), 
				AppContract.Chapters.buildTopicsUri(mChapterId), 
				PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

}
