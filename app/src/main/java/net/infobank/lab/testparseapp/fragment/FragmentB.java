package net.infobank.lab.testparseapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.infobank.lab.testparseapp.R;

import java.util.ArrayList;

/**
 * Created by chunghj on 15. 6. 30..
 */
public class FragmentB extends Fragment implements View.OnClickListener {
    private Button mAddData2;
    private Button mLoadData2;
    private TextView mData2;


    public static FragmentB newInstance(CharSequence label) {
        FragmentB f;
        f = new FragmentB();
        Bundle b = new Bundle();
        b.putCharSequence("label", label);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        mAddData2 = (Button) view.findViewById(R.id.button_b);
        mLoadData2 = (Button) view.findViewById(R.id.button2_b);
        mData2 = (TextView) view.findViewById(R.id.textView_b);

        mAddData2.setOnClickListener(this);
        mLoadData2.setOnClickListener(this);

        return view;
    }

    private void saveData() {
        try {
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true); // 해당 데이터에 대한 접근 권한을 모든 사람이 읽을 수 있도록 합니다.
            ParseObject data2 = new ParseObject("testDatas"); // object 생성 및 추가될 class 이름 입력
            data2.put("test_type", 2); // 데이터 입력
            data2.put("test_message", "두번째 데이터 입니다."); // 데이터 입력
            data2.setACL(defaultACL); // object에 ACL set
            data2.save(); // parse.com에 해당 object save
            Toast.makeText(getActivity(), "입력이 완료 되었습니다.", Toast.LENGTH_SHORT).show();


        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            ArrayList<ParseObject> datas = new ArrayList<ParseObject>(); // parse.com에서 읽어온 object들을 저장할 List
            ParseQuery<ParseObject> query = ParseQuery.getQuery("testDatas"); // 서버에 mydatas class 데이터 요청
            query.whereEqualTo("test_type", 2); // my_type이 1인 object만 읽어옴. 해당 함수 호출하지 않으면 class의 모든 데이터를 읽어옴.
            datas.addAll(query.find()); // 읽어온 데이터를 List에 저장
            // 읽어온 데이터를 화면에 보여주기 위한 처리
            StringBuffer str = new StringBuffer();
            for (ParseObject object : datas) {
                str.append("ObjectId: ");
                str.append(object.getObjectId());
                str.append("\n");
                str.append("test_type: ");
                str.append(object.get("test_type"));
                str.append(", ");
                str.append("test_message: ");
                str.append(object.get("test_message"));
                str.append("\n\n");
            }
            mData2.setText(str.toString()); // TextView에 데이터를 넣어준다.
            datas.clear();

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_b:
                saveData();
                break;
            case R.id.button2_b:
                loadData();
                break;
        }

    }
}
