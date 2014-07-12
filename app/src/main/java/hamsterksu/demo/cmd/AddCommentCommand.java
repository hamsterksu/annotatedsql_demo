package hamsterksu.demo.cmd;

import android.content.ContentValues;
import android.content.Context;

import com.telly.groundy.Groundy;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import hamsterksu.demo.store.PostStore.CommentTable;
import hamsterksu.demo.store.PostStore.PostsView;
import hamsterksu.demo.store.PostsProvider;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public class AddCommentCommand extends GroundyTask {

    private static final String ARG_POST_ID = "ARG_POST_ID";
    private static final String ARG_COMMENT_TEXT = "ARG_COMMENT_TEXT";

    @Override
    protected TaskResult doInBackground() {
        long postId = getLongArg(ARG_POST_ID);
        String text = getStringArg(ARG_COMMENT_TEXT);

        ContentValues v = new ContentValues(2);
        v.put(CommentTable.POST_ID, postId);
        v.put(CommentTable.TEXT, text);

        getContext().getContentResolver().insert(PostsProvider.contentUri(CommentTable.CONTENT_URI), v);
        return succeeded();
    }

    public static void start(Context context, long postId, String text){
        Groundy.create(AddCommentCommand.class).arg(ARG_POST_ID, postId).arg(ARG_COMMENT_TEXT, text).queueUsing(context);
    }
}
