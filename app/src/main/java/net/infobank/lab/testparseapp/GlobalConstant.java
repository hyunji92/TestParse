package net.infobank.lab.testparseapp;

public interface GlobalConstant {
    String TITLE = "title";
    String LABEL = "label";
    String SECTION = "section";
    String PACKAGE_NAME = "package_name";

    String INITIAL_CONTEXT_FACTORY ="com.sun.jndi.ldap.LdapCtxFactory";
            //"net.infobank.lab.testparseapp";
    // 제공 URL
    String PROVIDER_URL = "ldap://infoad01.infobank.net:389";
    // 보안
    String SECURITY_CREDENTIALS = "CN=LDAP,OU=Server,OU=Service,DC=infobank,DC=net";
    // 보안 안중서
    String SECURITY_CREDENTIALS_PASSWORD = "P@ssword11";

    String BASE_DN = "OU=INFOBANK,DC=infobank,DC=net";
    //"password");
    //env.put("java.naming.ldap.version","3");
}
