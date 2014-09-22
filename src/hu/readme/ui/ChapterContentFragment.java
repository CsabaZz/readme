package hu.readme.ui;

import hu.readme.R;
import hu.readme.adapters.ContentAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class ChapterContentFragment extends Fragment {
    
    private static final String EXTRA_BASE = ChapterContentFragment.class.getName() + "::";
    public static final String EXTRA_CHAPTER_ID = EXTRA_BASE + "ChapterId";

    public static Fragment newInstance(int chapterId) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_CHAPTER_ID, chapterId);
        
        final ChapterContentFragment f = new ChapterContentFragment();
        f.setArguments(args);
        return f;
    }

    private ExpandableListView mListView;
    private ContentAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ContentAdapter(getActivity(), getArguments().getInt(EXTRA_CHAPTER_ID));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.chapter_content, container, false);
        mListView = (ExpandableListView) contentView.findViewById(R.id.chapter_content_listview);
        return contentView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    public void onDestroyView() {
        if(null != mListView) {
            mListView.setAdapter((ContentAdapter)null);
            mListView = null;
        }
        
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((LaunchActivity) activity).onSectionAttached(
                getArguments().getInt(EXTRA_CHAPTER_ID));
    }

}
