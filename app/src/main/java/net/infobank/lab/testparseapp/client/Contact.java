
/**
 * Created by chunghj on 15. 7. 13..
 */
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

package net.infobank.lab.testparseapp.client;

import android.os.Bundle;
import android.util.Log;

import com.unboundid.ldap.sdk.ReadOnlyEntry;

/**
 * Represents a LDAPSyncAdapter contact.
 *
 * @author <a href="mailto:daniel.weisser@gmx.de">Daniel Weisser</a>
 */
public class Contact {
    public static String FIRSTNAME = "FIRSTNAME";
    public static String LASTNAME = "LASTNAME";
    public static String MAIL = "MAIL";

    private String dn = "";
    private String firstName = "";
    private String lastName = "";
    private String[] emails = null;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String[] getEmails() {
        return emails;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }


    /**
     * Creates and returns an instance of the user from the provided LDAP data.
     *
     * @param user The LDAPObject containing user data
     * @param mB   Mapping bundle for the LDAP attribute names.
     * @return user The new instance of LDAP user created from the LDAP data.
     */
    public static Contact valueOf(ReadOnlyEntry user, Bundle mB) {
        Contact c = new Contact();
        try {
            c.setDn(user.getDN());
            c.setFirstName(user.hasAttribute(mB.getString(FIRSTNAME)) ? user.getAttributeValue(mB.getString(FIRSTNAME)) : null);
            c.setLastName(user.hasAttribute(mB.getString(LASTNAME)) ? user.getAttributeValue(mB.getString(LASTNAME)) : null);
            if ((user.hasAttribute(mB.getString(FIRSTNAME)) ? user.getAttributeValue(mB.getString(FIRSTNAME)) : null) == null
                    || (user.hasAttribute(mB.getString(LASTNAME)) ? user.getAttributeValue(mB.getString(LASTNAME)) : null) == null) {
                return null;
            }
            c.setEmails(user.hasAttribute(mB.getString(MAIL)) ? user.getAttributeValues(mB.getString(MAIL)) : null);

        } catch (final Exception ex) {
            Log.i("User", "Error parsing LDAP user object" + ex.toString());
        }
        return c;
    }
}
