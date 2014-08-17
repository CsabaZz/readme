package hu.readme.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

public final class LoaderUtils {
    
    public static final int LOADER_CHAPTERS = 0;
    public static final int LOADER_TOPICS = 1;
    public static final int LOADER_CONTENTS = 2;
    
    private LoaderUtils() { }
    
    public static <D> void setLoader(LoaderManager manager, int loaderId, 
            Bundle args, LoaderManager.LoaderCallbacks<D> callback) {
        final Loader<D> loader = manager.getLoader(loaderId);
        if(null == loader || loader.isReset()) {
            manager.initLoader(loaderId, args, callback);
        } else {
            manager.restartLoader(loaderId, args, callback);
        }
    }

}
