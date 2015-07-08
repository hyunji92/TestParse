package net.infobank.lab.testparseapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;

import java.util.Hashtable;
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

    //LDAP Context
    //LDAPCon context = null;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mIbEmail = (EditText) findViewById(R.id.et_email_id);
        mIbPassword = (EditText) findViewById(R.id.et_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(this);


        String email = mIbEmail.getText().toString();
        String password = mIbPassword.getText().toString();

        String path = String.format(GlobalConstant.PROVIDER_URL,GlobalConstant.INITIAL_CONTEXT_FACTORY);
        String filterString = "(CN=LDAP)";


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
        // LdapContext connection = new InitialLdapContext(env, null);

        LDAPAttributeSet ldapAttributeSet = new LDAPAttributeSet();
        //ldapAttributeSet.

        LDAPConnection connection = new LDAPConnection();
        try {

            //context = new InitialDirContext(env);
            //SearchControls searcher = new SearchControls();

            connection.connect(GlobalConstant.PROVIDER_URL, port);
            //connection.
            //connection.connect(3, GlobalConstant.PROVIDER_URL, port,
            //GlobalConstant.SECURITY_PRINCIPAL, GlobalConstant.SECURITY_CREDENTIALS);

        } catch (LDAPException e) {
            e.printStackTrace();
        }

        /**
         * LDAP 계정과 암호를 이용한 사용자 인증
         *
         * @param userId
         *            계정명
         * @param password
         *            암호
         * @return 인증 여부 (ID / PASS 가 일치하는지 아닌지를 확인함)
         */
        public static boolean isAuthenticatedUser( mIbEmail,  mIbPassword) {
            boolean isAuthenticated = false;
            //String path = String.format(ldapPathFormat, ldapDomain);
            if (mIbEmail != null &&  mIbPassword != "") {
                Hashtable<String, String> properties = new Hashtable<String, String>();
                env.put(GlobalConstant.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(GlobalConstant.PROVIDER_URL, "ldap://infoad01.infobank.net:389");
                env.put(GlobalConstant.SECURITY_PRINCIPAL,
                        "CN=LDAP,OU=Server,OU=Service,DC=infobank,DC=net");
                env.put(GlobalConstant.SECURITY_CREDENTIALS, "P@ssword11");
                env.put("java.naming.ldap.version", "3");

                try {
                    LDAPContext con = new InitialLDAPContext(properties);
                    isAuthenticated = true;
                    con.close();
                } catch (NamingException e) {
                }
            }
            return isAuthenticated;
        }


    }
}