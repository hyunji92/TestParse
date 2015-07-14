/*
 * Copyright 2010 Daniel Weisser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.infobank.lab.testparseapp.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import net.infobank.lab.testparseapp.Constants;
import net.infobank.lab.testparseapp.LoginActivity;
import net.infobank.lab.testparseapp.client.Contact;
import net.infobank.lab.testparseapp.client.LDAPServerInstance;
import net.infobank.lab.testparseapp.client.LDAPUtilities;
import net.infobank.lab.testparseapp.platform.ContactManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SyncAdapter implementation for synchronizing LDAP contacts to the platform ContactOperations provider.
 * 
 * @author <a href="mailto:daniel.weisser@gmx.de">Daniel Weisser</a>
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String TAG = "LDAPSyncAdapter";

	private final AccountManager mAccountManager;
	private final Context mContext;

	private Date mLastUpdated;

	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
		mAccountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		Logger l = new Logger();

		l.startLogging();
		l.d("Start the sync");
		Log.d(TAG, "Start the sync.");
		List<Contact> users = new ArrayList<Contact>();
		String authtoken = null;
		try {
			// use the account manager to request the credentials
			authtoken = mAccountManager.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, true /* notifyAuthFailure */);
			final String host = mAccountManager.getUserData(account, LoginActivity.PARAM_HOST);
			final String username = mAccountManager.getUserData(account, LoginActivity.PARAM_USERNAME);
			final int port = Integer.parseInt(mAccountManager.getUserData(account, LoginActivity.PARAM_PORT));
			final String sEnc = mAccountManager.getUserData(account, LoginActivity.PARAM_ENCRYPTION);
			int encryption = 0;
			if (!TextUtils.isEmpty(sEnc)) {
				encryption = Integer.parseInt(sEnc);
			}
			LDAPServerInstance ldapServer = new LDAPServerInstance(host, port, encryption, username, authtoken);

			final String searchFilter = mAccountManager.getUserData(account, LoginActivity.PARAM_SEARCHFILTER);
			final String baseDN = mAccountManager.getUserData(account, LoginActivity.PARAM_BASEDN);

			// LDAP name mappings
			final Bundle mappingBundle = new Bundle();
			mappingBundle.putString(Contact.FIRSTNAME, mAccountManager.getUserData(account, LoginActivity.PARAM_MAPPING + Contact.FIRSTNAME));
			mappingBundle.putString(Contact.LASTNAME, mAccountManager.getUserData(account, LoginActivity.PARAM_MAPPING + Contact.LASTNAME));
			mappingBundle.putString(Contact.MAIL, mAccountManager.getUserData(account, LoginActivity.PARAM_MAPPING + Contact.MAIL));
			users = LDAPUtilities.fetchContacts(ldapServer, baseDN, searchFilter, mappingBundle, mLastUpdated, this.getContext());
			if (users == null) {
				syncResult.stats.numIoExceptions++;
				return;
			}
			// update the last synced date.
			mLastUpdated = new Date();
			// update platform contacts.
			Log.d(TAG, "Calling contactManager's sync contacts");
			l.d("Calling contactManager's sync contacts");
			ContactManager cm = new ContactManager(l);
			cm.syncContacts(mContext, account.name, users, syncResult);
			l.stopLogging();
		} catch (final AuthenticatorException e) {
			syncResult.stats.numParseExceptions++;
			Log.e(TAG, "AuthenticatorException", e);
		} catch (final OperationCanceledException e) {
			Log.e(TAG, "OperationCanceledExcetpion", e);
		} catch (final IOException e) {
			Log.e(TAG, "IOException", e);
			syncResult.stats.numIoExceptions++;
		}
	}
}
