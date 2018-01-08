package com.coolvisit;


import java.util.Properties;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Created by qicool on 2018/1/5.
 */
public class main {
    public static void main(String[] args) {
        System.out.println("start ");
        Properties env = new Properties();
        String adminName = "administrator@coolvisit.com";//username@domain
        String adminPassword = "As123456";//password
        String ldapURL = "LDAP://172.16.109.111:389";//ip:port
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        env.put(Context.PROVIDER_URL, ldapURL);

        try

        {
            LdapContext ctx = new InitialLdapContext(env, null);
            System.out.println("auth success ");
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
            String searchBase = "DC=coolvisit,DC=com";
            String returnedAtts[] = {"memberOf","name","mobile","mail","department"};
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);

            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                System.out.println("<<<::[" + sr.getName() + "]::>>>>");
                System.out.println("<<<::[" + sr.getAttributes().get("mail") + "]::>>>>");
                System.out.println("<<<::[" + sr.getAttributes() + "]::>>>>");
            }

            ctx.close();
        } catch (
                NamingException e)

        {
            e.printStackTrace();
            System.err.println("Problem searching directory: " + e);
        }

        System.err.println("end ");
    }
}
