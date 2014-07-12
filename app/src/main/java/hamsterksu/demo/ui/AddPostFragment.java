package hamsterksu.demo.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import hamsterksu.demo.R;
import hamsterksu.demo.cmd.AddCommentCommand;
import hamsterksu.demo.cmd.AddPostCommand;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public class AddPostFragment extends Fragment {

    private static final String ARG_POST_ID = "ARG_POST_ID";

    private long postId;

    public static AddPostFragment newInstance(long postId){
        Bundle args = new Bundle(1);
        args.putLong(ARG_POST_ID, postId);

        AddPostFragment f = new AddPostFragment();
        f.setArguments(args);

        return f;
    }

    public static AddPostFragment newInstance(){
        return new AddPostFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.postId = args == null ? 0 : args.getLong(ARG_POST_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_post_frm, container, false);

        final EditText edit = (EditText) v.findViewById(R.id.edit);
        edit.setHint(postId > 0 ? R.string.add_comment : R.string.add_post);

        final View btnSave = v.findViewById(R.id.btn_add);
        btnSave.setEnabled(false);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSave.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave(edit.getText().toString());
            }
        });

        edit.requestFocus();
        return v;
    }

    private void onSave(String text) {
        getFragmentManager().popBackStack();
        if(postId > 0){
            AddCommentCommand.start(getActivity(), postId, text);
        }else{
            AddPostCommand.start(getActivity(), text);
        }
        getActivity().getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}
