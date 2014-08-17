package hu.readme.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public final class Utils {
    
    private static final String PRE_TAG = "ReadMe-";
    
    private static String TAG = makeTag(Utils.class);
    
    private static final int BUFFER_SIZE = 32768;
    
    private Utils() { }
    
    public static String makeTag(Class<?> clazz) {
        return PRE_TAG + clazz.getSimpleName();
    }
    
    public static void copyStream(InputStream is, OutputStream os) {
        try {
            final byte[] bytes = new byte[BUFFER_SIZE];
            
            int count = is.read(bytes, 0, BUFFER_SIZE);
            while (count > -1) {
                os.write(bytes, 0, count);
                count = is.read(bytes, 0, BUFFER_SIZE);
            }
            
            os.flush();
        } catch (Exception ex) {
            Log.e(TAG, "There was a problem during copying the stream", ex);
        }
    }


}
