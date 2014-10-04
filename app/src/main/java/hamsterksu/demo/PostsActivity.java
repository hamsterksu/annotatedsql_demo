package hamsterksu.demo;

import android.app.Activity;
import android.os.Bundle;

import hamsterksu.demo.ui.AddPostFragment;
import hamsterksu.demo.ui.CommentsListFragment;
import hamsterksu.demo.ui.CommentsListFragment.ICommentsActionListener;
import hamsterksu.demo.ui.PostsListFragment;
import hamsterksu.demo.ui.PostsListFragment.IPostActionListener;


public class PostsActivity extends Activity implements IPostActionListener, ICommentsActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PostsListFragment())
                    .commit();
        }
    }

    @Override
    public void onShowAddPost() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, AddPostFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onShowComments(long postId) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, CommentsListFragment.newInstance(postId))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onShowAddComment(long postId) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, AddPostFragment.newInstance(postId))
                .addToBackStack(null)
                .commit();
    }
}
