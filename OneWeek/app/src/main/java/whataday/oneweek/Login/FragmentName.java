package whataday.oneweek.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.ViewAnimation;
import whataday.oneweek.Main.MainPagerActivity;
import whataday.oneweek.NetworkObject.User;
import whataday.oneweek.R;

/**
 * Created by hoon on 2016-03-06.
 */
public class FragmentName extends android.support.v4.app.Fragment {
    View rootView;

    public static FragmentName newInstance() {
        FragmentName MenuFragment = new FragmentName();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.login_fragment_name, container, false);
        this.rootView = rootView;
        return rootView;
    }

    Button btn_join_name;
    EditText edit_name;
    Boolean check_empty = true;
    SharedPreferences preferences;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = ((JoinActivity) getActivity()).pref;

        btn_join_name = (Button) rootView.findViewById(R.id.btn_join_name);
        edit_name = (EditText) rootView.findViewById(R.id.edit_name);

        btn_join_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_empty) {
                    Toast.makeText(getActivity(), "please typed your name", Toast.LENGTH_SHORT).show();
                } else {
                    ((JoinActivity) getActivity()).editor.putString("nick", edit_name.getText().toString());
                    ((JoinActivity) getActivity()).editor.commit();

                    ((JoinActivity) getActivity()).user.setNick(edit_name.getText().toString());

                    ApplicationController.getServerInterface().postRegister(
                            ((JoinActivity) getActivity()).user, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    Log.i("POST status :", String.valueOf(response.getStatus()));
                                    Log.i("POST response :", response.getBody().toString());

                                    ApplicationController.setDefaultRealm(
                                            ((JoinActivity) getActivity()).pref.getString("id", null));

                                    startActivity(new Intent(getActivity(), MainPagerActivity.class));
                                    getActivity().finish();

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                }
                            });

                    //TODO pref저장 / 서버가입포스트요청

                }
            }
        });

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    btn_join_name.setBackgroundResource(R.drawable.btn_gray);
                    check_empty = true;
                }else{
                    btn_join_name.setBackgroundResource(R.drawable.color_graytoyellow);
                    ViewAnimation.grayToYellow(btn_join_name);
                    check_empty = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}