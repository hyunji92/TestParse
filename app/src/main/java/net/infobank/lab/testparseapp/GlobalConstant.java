package net.infobank.lab.testparseapp;

public interface GlobalConstant {
    String TITLE = "title";
    String LABEL = "label";
    String SECTION = "section";
    String PACKAGE_NAME = "package_name";

    String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    //"net.infobank.lab.testparseapp";
    // 제공 URL ldap://
    String PROVIDER_URL = "infoad01.infobank.net";
    // 보안 인증
    //String SECURITY_PRINCIPAL = "chunghj09@infobank.net";
    // 보안 안중 PASSWORD
    //String SECURITY_CREDENTIALS = "@Guswl9559";


     // 보안 인증
     String SECURITY_PRINCIPAL = "CN=LDAP,OU=Server,OU=Service,DC=infobank,DC=net";
     // 보안 안중 PASSWORD
     String SECURITY_CREDENTIALS = "P@ssword11";


    String BASE_DN = "DC=infobank,DC=net";
}
