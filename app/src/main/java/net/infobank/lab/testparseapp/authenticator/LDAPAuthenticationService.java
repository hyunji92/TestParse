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

package net.infobank.lab.testparseapp.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to handle Account authentication. It instantiates the authenticator and returns its IBinder.
 * 
 * @author <a href="mailto:daniel.weisser@gmx.de">Daniel Weisser</a>
 */
public class LDAPAuthenticationService extends Service {
	private LDAPAuthenticator mAuthenticator;

	@Override
	public void onCreate() {
		mAuthenticator = new LDAPAuthenticator(this);
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mAuthenticator.getIBinder();
	}
}
