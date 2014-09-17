package hu.readme.ui;
import android.app.*;
import android.view.*;
import android.os.*;
import hu.readme.*;

public class DisplayContentFragment extends Fragment
{
	private static final String EXTRA_BASE = DisplayContentFragment.class.getName() + "::";
	public static final String EXTRA_POSITION = EXTRA_BASE + "Position";

	public static Fragment newInstance(int position)
	{
		final Bundle args = new Bundle();
		args.putInt(EXTRA_POSITION, position);
		
		final DisplayContentFragment f = new DisplayContentFragment();
		f.setArguments(args);
		
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View contentView = inflater.inflate(R.layout.content, container, false);
		return contentView;
	}
	
}
