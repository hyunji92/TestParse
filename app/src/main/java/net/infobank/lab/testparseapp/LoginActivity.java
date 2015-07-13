package net.infobank.lab.testparseapp;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

import net.infobank.lab.testparseapp.client.LDAPServerInstance;
import net.infobank.lab.testparseapp.client.LDAPUtilities;

import java.util.Properties;

/**
 * Created by chunghj on 15. 7. 7..
 */
public class LoginActivity extends AccountAuthenticatorActivity implements View.OnClickListener {

    private static final int ERROR_DIALOG = 1;
    private static final int PROGRESS_DIALOG = 0;
    public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
    public static final String PARAM_USEREMAIL = "useremail";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_HOST = "host";
    public static final String PARAM_PORT = "port";
    public static final String PARAM_ENCRYPTION = "encryption";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    public static final String PARAM_SEARCHFILTER = "searchFilter";
    public static final String PARAM_BASEDN = "baseDN";
    public static final String PARAM_MAPPING = "map_";

    private String mSearchFilter;
    private String mBaseDN;
    private String mHost;

    private static final String TAG = "LoginActivity";

    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean mRequestNewAccount = true;

    /**
     * If set we are just checking that the user knows their credentials, this doesn't cause the user's password to be changed on the device.
     */
    private Boolean mConfirmCredentials = false;

    /**
     * for posting authentication attempts back to UI thread
     */
    private final Handler mHandler = new Handler();

    private AccountManager mAccountManager;
    private Thread mAuthThread;
    private String mAuthtoken;
    private String mAuthtokenType;

    private String mIbEmail;
    private EditText mIbEmailEdit;
    private String mIbPassword;
    private EditText mIbPasswordEdit;
    private Button mLoginBtn;
    private String mFirstName;
    private String mLastName;

    private int mEncryption;
    private int mPort;
    private final int port = 389;

    private Dialog dialog;
    private String message;

    //private LDAPConnection ldapConnection;

    //LDAP Context
    //LDAPCon context = null;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountManager = AccountManager.get(this);

        getDataFromIntent();
        setLDAPMappings();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mIbEmailEdit = (EditText) findViewById(R.id.et_email_id);
        mIbPasswordEdit = (EditText) findViewById(R.id.et_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(this);


        String email = mIbEmailEdit.getText().toString();
        String password = mIbPasswordEdit.getText().toString();

        String path = String.format(GlobalConstant.PROVIDER_URL, GlobalConstant.INITIAL_CONTEXT_FACTORY);
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

        LDAPConnection conn = new LDAPConnection();
        try {

            //context = new InitialDirContext(env);
            //SearchControls searcher = new SearchControls();

            conn.connect(GlobalConstant.PROVIDER_URL, port);
            //connection.connect(3, GlobalConstant.PROVIDER_URL, port,
            //GlobalConstant.SECURITY_PRINCIPAL, GlobalConstant.SECURITY_CREDENTIALS);
//            LDAPTestUtils.au

        } catch (LDAPException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the default LDAP mapping attributes
     */
    private void setLDAPMappings() {
        if (mRequestNewAccount) {
            // mSearchFilter = "(objectClass=inetOrgPerson)";
            mSearchFilter = "(objectClass=organizationalPerson)";
            mFirstName = "givenName";
            mLastName = "sn";
            mIbEmail = "mail";
        }
    }

    /**
     * Obtains data from an intent that was provided for the activity. If no intent was provided some default values are set.
     */
    private void getDataFromIntent() {
        final Intent intent = getIntent();
        //mPassword = intent.getStringExtra(PARAM_PASSWORD);
        mIbEmail = intent.getStringExtra(PARAM_USEREMAIL);
        mIbPassword = intent.getStringExtra(PARAM_PASSWORD);
        mHost = intent.getStringExtra(PARAM_HOST);
        mPort = intent.getIntExtra(PARAM_PORT, 389);
        mEncryption = intent.getIntExtra(PARAM_ENCRYPTION, 0);
        mRequestNewAccount = (mIbEmail == null);
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);
    }

    /**
     * Called when response is received from the server for confirm credentials request. See onAuthenticationResult(). Sets the AccountAuthenticatorResult which
     * is sent back to the caller.
     *
     * @param //the confirmCredentials result.
     */
    protected void finishConfirmCredentials(boolean result) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(GlobalConstant.PROVIDER_URL, Constants.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, mIbPassword);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when response is received from the server for authentication request. See onAuthenticationResult(). Sets the AccountAuthenticatorResult which is
     * sent back to the caller. Also sets the authToken in AccountManager for this account.
     */
    protected void finishLogin() {
        Log.i(TAG, "finishLogin()");
        final Account account = new Account(GlobalConstant.PROVIDER_URL, Constants.ACCOUNT_TYPE);

        if (mRequestNewAccount) {
            Bundle userData = new Bundle();
            userData.putString(PARAM_USEREMAIL, mIbEmail);
            userData.putString(PARAM_PORT, mPort + "");
            userData.putString(PARAM_HOST, mHost);
            userData.putString(PARAM_ENCRYPTION, mEncryption + "");
            userData.putString(PARAM_SEARCHFILTER, mSearchFilter);
            userData.putString(PARAM_BASEDN, mBaseDN);
            // Mappings for LDAP data
            // Contact 파일 만들어야함
            //userData.putString(PARAM_MAPPING + Contact.FIRSTNAME, mFirstName);
            //userData.putString(PARAM_MAPPING + Contact.LASTNAME, mLastName);
            //userData.putString(PARAM_MAPPING + Contact.MAIL, mIbEmail);
            mAccountManager.addAccountExplicitly(account, mIbPassword, userData);

            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
            // 주소록 가져오는것 같은데 필요 없을듯
            // ContactManager.makeGroupVisible(account.name, getContentResolver());
        } else {
            mAccountManager.setPassword(account, mIbPassword);
        }
        final Intent intent = new Intent();
        mAuthtoken = mIbPassword;
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        if (mAuthtokenType != null && mAuthtokenType.equals(Constants.AUTHTOKEN_TYPE)) {
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Handles onClick event on the Next button. Sends username/password to the server for authentication.
     *
     * @param view The Next button for which this method is invoked
     */
    public void getLDAPServerDetails(View view) {
        Log.i(TAG, "handleLogin");

        try {

        } catch (NumberFormatException nfe) {
            Log.i(TAG, "No port given. Set port to 389");
            mPort = 389;
        }

        LDAPServerInstance ldapServer = new LDAPServerInstance(mHost, mPort, mEncryption, mIbEmail, mIbPassword);

        showDialog(PROGRESS_DIALOG);
        // Start authenticating...
        mAuthThread = LDAPUtilities.attemptAuth(ldapServer, mHandler, LoginActivity.this);
    }

    /**
     * Call back for the authentication process. When the authentication attempt is finished this method is called.
     *
     * @param baseDNs List of baseDNs from the LDAP server
     * @param result  result of the authentication process
     * @param message Possible error message
     */
    public void onAuthenticationResult(String[] baseDNs, boolean result, String message) {
        Log.i(TAG, "onAuthenticationResult(" + result + ")");
        if (dialog != null) {
            dialog.dismiss();
        }
        this.message = message;
        showDialog(ERROR_DIALOG);
        Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    }
    /**
     * Handles onClick event on the Done button. Saves the account with the account manager.
     *
     * @param view
     *            The Done button for which this method is invoked
     */
    public void saveAccount(View view) {
        String searchFilter = mSearchFilter;
        String baseDN = mBaseDN;
        String firstName = mFirstName;
        String lastName = mLastName;

        if (!mConfirmCredentials) {
            finishLogin();
        } else {
            finishConfirmCredentials(true);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == PROGRESS_DIALOG) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("인증중");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    Log.i(TAG, "dialog cancel has been invoked");
                    if (mAuthThread != null) {
                        mAuthThread.interrupt();
                        finish();
                    }
                }
            });
            this.dialog = dialog;
            return dialog;
        } else if (id == ERROR_DIALOG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection error").setMessage("Could not connect to the server:\n" + message).setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            return alert;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == ERROR_DIALOG) {
            ((AlertDialog) dialog).setMessage("Could not connect to the server:\n" + message);
        }
    }

}
