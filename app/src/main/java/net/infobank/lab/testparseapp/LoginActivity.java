package net.infobank.lab.testparseapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

import java.util.Properties;

/**
 * Created by chunghj on 15. 7. 7..
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText mIbEmail;
    private EditText mIbPassword;
    private Button mLoginBtn;

    private final int port = 389;
    //private LDAPConnection ldapConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mIbEmail = (EditText) findViewById(R.id.et_email_id);
        mIbPassword = (EditText) findViewById(R.id.et_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);

        String email = mIbEmail.getText().toString();
        String password = mIbPassword.getText().toString();

        mLoginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Properties env = new Properties();
        env.put(GlobalConstant.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(GlobalConstant.PROVIDER_URL, "ldap://infoad01.infobank.net:389");
        env.put(GlobalConstant.SECURITY_PRINCIPAL,
                "CN=LDAP,OU=Server,OU=Service,DC=infobank,DC=net");
        env.put(GlobalConstant.SECURITY_CREDENTIALS, "P@ssword11");
        env.put("java.naming.ldap.version", "3");

        // 여기서 저정보들을 연결할때 넘긴다?
        // LDAPConnection ldapConnection  = new LDAPConnection(env, null);

        LDAPConnection connection = new LDAPConnection();
        try {
            connection.connect(GlobalConstant.PROVIDER_URL, port);
            //connection.connect(3, GlobalConstant.PROVIDER_URL, port,
            //GlobalConstant.SECURITY_PRINCIPAL, GlobalConstant.SECURITY_CREDENTIALS);

        } catch (LDAPException e) {
            e.printStackTrace();
        }


    }
}