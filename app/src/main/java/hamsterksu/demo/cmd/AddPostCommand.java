package hamsterksu.demo.cmd;

import android.content.ContentValues;
import android.content.Context;

import com.telly.groundy.Groundy;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import hamsterksu.demo.store.PostStore.PostTable;
import hamsterksu.demo.store.PostsProvider;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public class AddPostCommand extends GroundyTask {

    private static final String ARG_POST = "ARG_POST";

    @Override
    protected TaskResult doInBackground() {
        String text = getStringArg(ARG_POST);

        ContentValues v = new ContentValues(1);
        v.put(PostTable.TEXT, text);

        getContext().getContentResolver().insert(PostsProvider.contentUri(PostTable.CONTENT_URI), v);
        return succeeded();
    }

    public static void start(Context context, String text) {
        Groundy.create(AddPostCommand.class).arg(ARG_POST, text).queueUsing(context);
    }
}
