package net.infobank.lab.testparseapp;

public interface GlobalConstant {
    String TITLE = "title";
    String LABEL = "label";
    String SECTION = "section";
    String PACKAGE_NAME = "package_name";

    String INITIAL_CONTEXT_FACTORY ="com.sun.jndi.ldap.LdapCtxFactory";
            //"net.infobank.lab.testparseapp";

    String PROVIDER_URL = "ldap://infoad01.infobank.net:389";
    String SECURITY_PRINCIPAL = "CN=LDAP,OU=Server,OU=Service,DC=infobank,DC=net";
    String SECURITY_CREDENTIALS = "P@ssword11";

    //"password");
    //env.put("java.naming.ldap.version","3");
}
