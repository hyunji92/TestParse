package net.infobank.lab.testparseapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //private final String PARSE_APPLICATION_ID = "ch68mVW8T9mWjkm14MQqbXVzX8fzI9uuNwSubY53";
    //private final String PARSE_CLIENT_KEY = "9PZBYzBJZH96Ou1M9ACV7m91GeKPfoZ92Lrps1VG";

    private Button mAddData;
    private Button mLoadData;
    private TextView mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "ch68mVW8T9mWjkm14MQqbXVzX8fzI9uuNwSubY53", "9PZBYzBJZH96Ou1M9ACV7m91GeKPfoZ92Lrps1VG");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
// client에서 class를 만들 경우, ParseObject를 만들고 save하면 parse.com에 자동으로 해당 class가 추가
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

        mData = (TextView) findViewById(R.id.textView);

        mAddData = (Button) findViewById(R.id.button);
        mLoadData = (Button) findViewById(R.id.button2);

        mAddData.setOnClickListener(this);
        mLoadData.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                saveData();
                break;
            case R.id.button2:
                loadData();
                break;
        }

    }

    private void saveData() {
        try {
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true); // 해당 데이터에 대한 접근 권한을 모든 사람이 읽을 수 있도록 합니다.
            ParseObject data1 = new ParseObject("testDatas"); // object 생성 및 추가될 class 이름 입력
            data1.put("test_type", 1); // 데이터 입력
            data1.put("test_message", "첫번째 데이터 입니다."); // 데이터 입력
            data1.setACL(defaultACL); // object에 ACL set
            data1.save(); // parse.com에 해당 object save


            ParseObject data2 = new ParseObject("testDatas"); // object 생성 및 추가될 class 이름 입력
            data2.put("test_type", 2); // 데이터 입력
            data2.put("test_message", "두번째 데이터 입니다."); // 데이터 입력
            data2.setACL(defaultACL); // object에 ACL set
            data2.save(); // parse.com에 해당 object save


            ParseObject data3 = new ParseObject("testDatas"); // object 생성 및 추가될 class 이름 입력
            data3.put("test_type", 3); // 데이터 입력
            data3.put("test_message", "두번째 데이터 입니다."); // 데이터 입력
            data3.setACL(defaultACL); // object에 ACL set
            data3.save(); // parse.com에 해당 object save
            Toast.makeText(this, "입력이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            ArrayList<ParseObject> datas = new ArrayList<ParseObject>(); // parse.com에서 읽어온 object들을 저장할 List
            ParseQuery<ParseObject> query = ParseQuery.getQuery("testDatas"); // 서버에 mydatas class 데이터 요청
            //query.whereEqualTo("test_type", 1); // my_type이 1인 object만 읽어옴. 해당 함수 호출하지 않으면 class의 모든 데이터를 읽어옴.
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
            mData.setText(str.toString()); // TextView에 데이터를 넣어준다.
            datas.clear();

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
