package hamsterksu.demo.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import hamsterksu.demo.R;
import hamsterksu.demo.store.PostStore.CommentTable;
import hamsterksu.demo.store.PostsProvider;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public class CommentsListFragment extends CursorListFragment {

    private static final String ARG_POST_ID = "ARG_POST_ID";

    private long postId;

    /**
     * always use static method to create fragment
     */
    public static CommentsListFragment newInstance(long postId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_POST_ID, postId);

        CommentsListFragment f = new CommentsListFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getLong(ARG_POST_ID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getTitleId() {
        return R.string.comments_title;
    }

    @Override
    protected int getEmptyTextId() {
        return R.string.comments_empty;
    }

    private ICommentsActionListener getListener() {
        return (ICommentsActionListener) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.comments, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_replay) {
            getListener().onShowAddComment(postId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ResourceCursorAdapter createAdapter() {
        return new CommentsAdapter(getActivity());
    }

    @Override
    protected Loader<Cursor> onCreateLoader(int id) {
        //you can use where condition or "smart" uri

        //1. where condition
        /*return new CursorLoader(getActivity(),
                PostsProvider.contentUri(CommentTable.CONTENT_URI),
                null,
                CommentTable.POST_ID + " = ?", new String[]{String.valueOf(postId)}, null);*/

        //2. smart uri
        return new CursorLoader(getActivity(),
                PostsProvider.contentUri(CommentTable.CONTENT_URI_BY_POST, postId),
                null,
                null, null, null);
    }

    private class CommentsAdapter extends ResourceCursorAdapter {

        public CommentsAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor c) {
            TextView text = (TextView) view;
            text.setText(c.getString(c.getColumnIndex(CommentTable.TEXT)));
        }
    }

    public static interface ICommentsActionListener {
        void onShowAddComment(long postId);
    }
}
