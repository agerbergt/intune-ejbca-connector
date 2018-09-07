# Intune EJBCA Connector
Intune EJBCA Connector is a SCEP server that supports request validation 
through Microsoft Intune and certificate enrollment through EJBCA.

## Setup Guide
1.  Prepare your Microsoft Intune tenant for third-party CA integration as
    described in the following article:
    
    https://docs.microsoft.com/en-us/intune/certificate-authority-add-scep-overview#set-up-third-party-ca-integration
    
    **NOTE:** write down the `Application ID` under application settings and the
    `API Access Key` that is generated as a part of the article. This information will
    be needed when configuring the connector.

2.  Prepare integration with EJBCA and create the following certificates if needed:

    - **SSL server certificate** - Used by Tomcat connector to secure communication from mobile devices. Should be issued as a Java Keystore (JKS).
    - **SCEP Receiver certificate** - Used by Intune EJBCA Connector to secure SCEP messages from mobile devices. Should be issued as a Java Keystore (JKS) and will be returned to mobile devices through the SCEP call `GetCACerts`.
    - **EJBCA Admin certificate** - Used by Intune EJBCA Connector to authenticate to EJBCA Web service. Should be issued as a Java Keystore (JKS) and given required administrator permissions.

3.  Prepare a Tomcat application server that is going to host the
    web application. It is strongly recommended to setup a secure HTTPS
    connector, which will be using the SSL server certificate mentioned in step 2.
    
    **NOTE:** The WAR file contains an embedded Tomcat that can be used for testing. To start the connector you could simply run:
    `java -jar intune-ejbca-connector.war` and browse to http://localhost:8080 to verify it has been started.

4.  Create configuration in `/etc/intune-ejbca-connector.yml` 
    (See section Configuration). It should be owned and readable only by the application server as it contains sensitive information.
    
5.  Deploy `intune-ejbca-connector.war` in to *webapps* directory
    of the Tomcat server and start/restart the application server.

6.  Setup and assign Intune SCEP profile as described in the following
    article:
    
    https://docs.microsoft.com/en-us/intune/certificates-scep-configure#create-a-scep-certificate-profile

    **NOTE:** SCEP Server URL must be set to the URL of the web 
    application, ex: https://*server.somehost.org*/intune-ejbca-connector/scep

## Configuration
Configuration is using YAML syntax and the default location
that the web application looks for is:

    /etc/intune-ejbca-connector.yml

A different location can be specified by setting the Java
system property `config.location` when starting the JVM:
 
    -Dconfig.location=/path/to/config.yml

The following shows an example configuration with a basic profile:
    
    intune:
        tenant: sometenant.onmicrosoft.com
        appId: a01b02c0-3d04-e05f-06a0-7b08c09d10e
        appKey: TmloaWwgaGljIHZpZ2lsYXJlIGV0IHJldmVydGFtdXIgYWQgb3BlcmFuZHVtCg==
    
    ejbca:
        serviceName: EJBCA 6.3.1.1
        serviceUrl: https://ca.somecompany.org:8443/ejbca/ejbcaws/ejbcaws?wsdl
        keystorePath: /path/to/ra-admin-keystore.jks
        keystorePassword: somepassword
  
    scep:
        keystorePath: /opt/intune-ejbca-connector/scepreceiver.jks
        keystorePassword: foo123
        
    profile:
        certificateAuthority: LCSO_MobileCA
        certificateProfile: CP_LCSO_Mobile
        endEntityProfile: EEP_LCSO_Mobile
        baseDN: OU=Mobiles,O=Lab Certificate Services Org,C=SE   

Configuration is organized into different sections. We will now look into 
more detailed information about each section.

### Intune configuration
Section (**intune:**) containing configuration needed in order to connect to Intune service.

| Key    | Description
| ------ | -----------
| tenant | Intune tenant to use.
| appId  | Application ID from Azure AD (See section Setup Guide).
| appKey | API key to use when authenticating to cloud service.

### EJBCA configuration
Section (**ejbca:**) containing configuration needed in order to connect to EJBCA.

| Key              | Description
| ---------------- | -----------
| serviceName      | Arbitrary name and version of EJBCA service.
| serviceUrl       | EJBCA web service endpoint URL.
| keystorePath     | Path to java key store containing administrator certificate to use when authenticating to EJBCA web service.
| keystorePassword | Password that protects the keystore and the private key.

### SCEP configuration
Section (**scep:**) containing configuration needed for the SCEP service.

| Key              | Description
| ---------------- | -----------
| keystorePath     | Path to java key store containing RA/Receiver certificate, including CA certificate chain, to use during SCEP enrollment.
| keystorePassword | Password that protects the keystore and the private key.
| keystoreAlias    | Alias of key to use within key store. If not set the first key entry will be used. *(Optional)*

### Profile configuration
Section (**profile:**) containing configuration needed to issue certificates from EJBCA. These values will be used as default profile values and can be overridden by custom profiles if needed (described in section *Custom profiles* below).

| Key                  | Description
| -------------------- | -----------
| certificateAuthority | Certificate authority to use when issuing certificates to mobile devices.
| certificateProfile   | Certificate profile to use for mobile device certificates.
| endEntityProfile     | End entity profile to use for mobile device certificates.
| baseDN               | Optional DN string to append to all certificates, ex: "O=Some Company,C=SE".

## Custom Profiles
Custom profiles can be used in order to enroll multiple SCEP certificates to each device 
(ie. one device certificate and one user certificate) but only using a single instance of Intune EJBCA Connector.

### Custom profiles configuration
Custom profiles are added in the configuration file in the section **customProfiles:** that should 
contain sub-sections for each custom profile. The following gives an example of such section that could
be added to the configuration. Here we define two custom profiles **mobile** and **user** that can be
used to issue two different SCEP certificates from different certificate authorities. Note that the names
*mobile* and *user* are arbitrary and can be given any name, and any number of profiles can be configured.

    customProfiles:
        mobile:
            certificateAuthority: LCSO_MobileCA
            certificateProfile: CP_LCSO_Mobile
            endEntityProfile: EEP_LCSO_Mobile
            baseDN: OU=Mobiles,O=Lab Certificate Services Org,C=SE
        user:
            certificateAuthority: LCSO_UserCA
            certificateProfile: CP_LCSO_User
            endEntityProfile: EEP_LCSO_User
            baseDN: OU=Users,O=Lab Certificate Services Org,C=SE

### Enrolling using custom profiles
When custom profiles have been configured it is possible to specify them in the SCEP Server URL
that is entered into Intune SCEP profile configuration. The last segment of the base URL 
(ending with /scep) is treated as the custom profile name.

The following is an example of a basic SCEP Server URL without custom profile:

    https://server.somehost.org/intune-ejbca-connector/scep
    
The following is an example of a SCEP Server URL with the custom profile *mobile*:

    https://server.somehost.org/intune-ejbca-connector/scep/mobile

The following is an example of a SCEP Server URL with the custom profile *user*:

    https://server.somehost.org/intune-ejbca-connector/scep/user

## Logging
Log is written to standard output which will be available in the application server log (eg. catalina.out)
and to a log file named `intune-ejbca-connector.log` within the application server directory.

## Copyright & License
Copyright (c) 2018 CGI Certificate Services - Released under the 
GNU Affero General Public License.