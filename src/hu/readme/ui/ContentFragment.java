package hu.readme.ui;

import hu.readme.R;
import hu.readme.database.AppContract;
import hu.readme.utils.LoaderUtils;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class ContentFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String[] CONTENT_PROJECTION = new String[] { 
		AppContract.Tables.CONTENT + "." + AppContract.Contents._ID, 
        AppContract.Tables.TOPICS + "." + AppContract.Topics._ID, 
        AppContract.Tables.TOPICS + "." + AppContract.Topics.TITLE, 
		AppContract.Tables.CONTENT + "." + AppContract.Contents.CONTENT
	};
	
    private static final int COLUMN_NUMBER = 1;
	private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_CONTENT = 3;
	
	private static final String BASE_EXTRA = ContentFragment.class + "::";
	public static final String EXTRA_TOPIC_ID = BASE_EXTRA + "TopicId";
	
	public static ContentFragment instantiate(int topicId) {
		final Bundle args = new Bundle();
		args.putInt(EXTRA_TOPIC_ID, topicId);
		
		final ContentFragment f = new ContentFragment();
		f.setArguments(args);
		return f;
	}

	private int mTopicId;
	private Cursor mCursor;
	
	private TextView mTitleText;
	private WebView mWebview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Bundle args = getArguments();
		mTopicId = args.getInt(EXTRA_TOPIC_ID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View contentView = inflater.inflate(R.layout.content, container, false);
        
		mTitleText = (TextView) contentView.findViewById(R.id.content_title);
		mWebview = (WebView) contentView.findViewById(R.id.content_webview);
        
		return contentView;
	}
	
	@Override
	public void onDestroyView() {
        getLoaderManager().destroyLoader(LoaderUtils.LOADER_CONTENTS);
        
	    mTitleText = null;
	    mWebview = null;
	    
	    super.onDestroyView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LoaderUtils.setLoader(getLoaderManager(), 
				LoaderUtils.LOADER_CONTENTS, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), 
				AppContract.Topics.buildContentUri(mTopicId), 
				CONTENT_PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCursor = data;
		updateUi();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursor = null;
		updateUi();
	}
	
	@Override
	public String getStackName() {
		if(mTopicId == 0) {
			return super.getStackName();
		} else {
			return super.getStackName() + "://" + mTopicId;
		}
	}

	private void updateUi() {
		if(null == mCursor) {
			mWebview.loadUrl("about:blank");
		} else if(mCursor.moveToFirst()) {
            final String number = mCursor.getString(COLUMN_NUMBER);
            final String title = mCursor.getString(COLUMN_TITLE);
		    mTitleText.setText(number + ". " + title);
		    
	        final String content = mCursor.getString(COLUMN_CONTENT);
	        mWebview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
		}
	}

}
