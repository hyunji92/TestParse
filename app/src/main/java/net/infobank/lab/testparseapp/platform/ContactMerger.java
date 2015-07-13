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

package net.infobank.lab.testparseapp.platform;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import net.infobank.lab.testparseapp.client.Contact;
import net.infobank.lab.testparseapp.syncadapter.Logger;

import java.util.ArrayList;

/**
 * A helper class that merges the fields of existing contacts with the fields of new contacts.
 * 
 * @author <a href="mailto:daniel.weisser@gmx.de">Daniel Weisser</a>
 */
public class ContactMerger {

	private final long rawContactId;
	private final Contact newC;
	private final Contact existingC;
	private final ArrayList<ContentProviderOperation> ops;
	private final Logger l;

	public ContactMerger(long rawContactId, Contact newContact, Contact existingContact, ArrayList<ContentProviderOperation> ops, Logger l) {
		this.rawContactId = rawContactId;
		this.newC = newContact;
		this.existingC = existingContact;
		this.ops = ops;
		this.l = l;
	}

	public void updateName() {
		if (TextUtils.isEmpty(existingC.getFirstName()) || TextUtils.isEmpty(existingC.getLastName())) {
			l.d("Set name to: " + newC.getFirstName() + " " + newC.getLastName());
			ContentValues cv = new ContentValues();
			cv.put(StructuredName.GIVEN_NAME, newC.getFirstName());
			cv.put(StructuredName.FAMILY_NAME, newC.getLastName());
			cv.put(StructuredName.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			Builder insertOp = createInsert(rawContactId, cv);
			ops.add(insertOp.build());
		} else if (!newC.getFirstName().equals(existingC.getFirstName()) || !newC.getLastName().equals(existingC.getLastName())) {
			l.d("Update name to: " + newC.getFirstName() + " " + newC.getLastName());
			ContentValues cv = new ContentValues();
			cv.put(StructuredName.GIVEN_NAME, newC.getFirstName());
			cv.put(StructuredName.FAMILY_NAME, newC.getLastName());
			Builder updateOp = ContentProviderOperation.newUpdate(addCallerIsSyncAdapterFlag(Data.CONTENT_URI)).withSelection(
					Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?", new String[] { rawContactId + "", StructuredName.CONTENT_ITEM_TYPE })
					.withValues(cv);
			ops.add(updateOp.build());
		}
	}

	private Builder createInsert(long rawContactId, ContentValues cv) {
		Builder insertOp = ContentProviderOperation.newInsert(addCallerIsSyncAdapterFlag(Data.CONTENT_URI)).withValues(cv);
		if (rawContactId == -1) {
			insertOp.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		} else {
			insertOp.withValue(Data.RAW_CONTACT_ID, rawContactId);
		}
		return insertOp;
	}
	private Uri addCallerIsSyncAdapterFlag(Uri uri) {
		Uri.Builder b = uri.buildUpon();
		b.appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true");
		return b.build();
	}


	public void updateMail(int mailType) {
		String newMail = null;
		String existingMail = null;
		if (mailType == Email.TYPE_WORK) {
			if (newC.getEmails() != null && newC.getEmails().length > 0) {
				newMail = newC.getEmails()[0];
			}
			if (existingC.getEmails() != null && existingC.getEmails().length > 0) {
				existingMail = existingC.getEmails()[0];
			}
		}
		updateMail(newMail, existingMail, mailType);
	}

	private void updateMail(String newMail, String existingMail, int mailType) {
		String selection = Data.RAW_CONTACT_ID + "=? AND " + Email.MIMETYPE + "=? AND " + Email.TYPE + "=?";
		if (TextUtils.isEmpty(newMail) && !TextUtils.isEmpty(existingMail)) {
			l.d("Delete mail data " + mailType + " (" + existingMail + ")");
			ops.add(ContentProviderOperation.newDelete(addCallerIsSyncAdapterFlag(Data.CONTENT_URI)).withSelection(selection,
					new String[] { rawContactId + "", Email.CONTENT_ITEM_TYPE, mailType + "" }).build());
		} else if (!TextUtils.isEmpty(newMail) && TextUtils.isEmpty(existingMail)) {
			l.d("Add mail data " + mailType + " (" + newMail + ")");
			ContentValues cv = new ContentValues();
			cv.put(Email.DATA, newMail);
			cv.put(Email.TYPE, mailType);
			cv.put(Email.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			Builder insertOp = createInsert(rawContactId, cv);
			ops.add(insertOp.build());
		} else if (!TextUtils.isEmpty(newMail) && !newMail.equals(existingMail)) {
			l.d("Update mail data " + mailType + " (" + existingMail + " => " + newMail + ")");
			Builder updateOp = ContentProviderOperation.newUpdate(addCallerIsSyncAdapterFlag(Data.CONTENT_URI)).withSelection(selection,
					new String[] { rawContactId + "", Email.CONTENT_ITEM_TYPE, mailType + "" }).withValue(Email.DATA, newMail);
			ops.add(updateOp.build());
		}
	}



}
