package whataday.oneweek.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import whataday.oneweek.Main.MainPagerActivity;
import whataday.oneweek.MainActivity;
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

    Button btn_join;
    EditText edit_name;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_join = (Button) rootView.findViewById(R.id.btn_join);
        edit_name = (EditText) rootView.findViewById(R.id.edit_name);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(edit_name)){
                    Toast.makeText(getActivity(), "please typed your name", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(getActivity(), MainPagerActivity.class));
                    getActivity().finish();
                }
            }
        });
    }

    private boolean isEmpty(EditText check_item){
        if( check_item.getText().length() == 0 ){
            return true;
        }else{
            return false;
        }
    }

}