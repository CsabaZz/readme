package hu.readme.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            doRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sendPageVisit();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            doSaveInstanceState(outState);
        }
    }

    public String getStackName() {
        return getClass().getName();
    }
    
    protected void doRestoreInstanceState(Bundle savedInstanceState) { }

    protected void doSaveInstanceState(Bundle outState) { }

    public void resetUIState() { }

    protected void sendPageVisit() { }

}
