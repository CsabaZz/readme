package hu.readme.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class AppContract {
    
    public static final String CONTENT_AUTHORITY = "hu.readme";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    
    private AppContract() { }
    
    public interface Tables {
        String CHAPTERS = "chapters";
        String TOPICS = "topic";
        String CONTENT = "content";
        
        String TOPICS_IN_CHAPTER = "topic " 
                + "LEFT OUTER JOIN chapters ON topic.chapter_id=chapters._id";
        String CONTENT_IN_TOPIC = "content " 
                + "LEFT OUTER JOIN topic ON content.topic_id=topic._id";
    }
    
    public interface ChapterColumns extends BaseColumns {
        String TITLE = "title";
    }
    
    public interface TopicColumns extends BaseColumns {
        String CHAPTER_ID = "chapter_id";
        String TITLE = "title";
    }
    
    public interface ContentColumns extends BaseColumns {
        String CHAPTER_ID = "chapter_id";
        String TOPIC_ID = "topic_id";
        String CONTENT = "content";
    }
    
    protected static class Base {
        protected static final String BASE_CONTENT_TYPE = 
                "vnd.android.cursor.dir/vnd.readme.";
        protected static final String BASE_ITEM_TYPE = 
                "vnd.android.cursor.item/vnd.readme.";
        
        public static String getId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
    
    public static class Chapters extends Base implements ChapterColumns {
        
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.CHAPTERS).build();
        
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + Tables.CHAPTERS;
        public static final String ITEM_TYPE = BASE_ITEM_TYPE + Tables.CHAPTERS;
        
        public static Uri buildTopicsUri(int chapterId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(chapterId))
                    .appendPath("topics")
                    .build();
        }
        
    }
    
    public static class Topics extends Base implements TopicColumns {
        
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.TOPICS).build();
        
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + Tables.TOPICS;
        public static final String ITEM_TYPE = BASE_ITEM_TYPE + Tables.TOPICS;
        
        public static Uri buildContentUri(int topicId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(topicId))
                    .appendPath("content")
                    .build();
        }
        
    }
    
    public static class Contents extends Base implements ContentColumns {
        
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.CONTENT).build();
        
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + Tables.CONTENT;
        public static final String ITEM_TYPE = BASE_ITEM_TYPE + Tables.CONTENT;
        
    }

}
