# Intune EJBCA Connector
Intune EJBCA Connector is a SCEP server that supports request validation 
through Microsoft Intune and certificate enrollment through EJBCA.

## Requirements
- EJBCA 6 (Developed with version 6.3.1.1)
- Microsoft Intune Tenant
- Java Application Server (Tested with Tomcat 7)

## Platform Compatibility
| Platform         | Status      | Notes
| ---------------- | ----------- | ---------
| Android          | OK          | Tested with Android 8.
| iOS              | OK          | Tested with iOS 11.4.1.
| Windows          | OK          | Tested with Windows 10. SCEP Receiver certificate must be issued by the same CA chain as the end entity certificates.
| MacOS X          | OK          | Tested with MacOS 10.12.6.

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
    
4.  Create configuration in `/etc/intune-ejbca-connector.yml` 
    (See section Configuration). It should be owned and readable only by the application server as it contains sensitive information.
    
5.  Deploy `intune-ejbca-connector.war` in to *webapps* directory
    of the Tomcat server and start/restart the application server.
    
    **NOTE:** Verify that application as started successfully by opening *https://`<hostname>`/intune-ejbca-connector*
    in a browser, assuming a connector has been configured on port 443. It should say *Ready to serve!* if everything seems OK, 
    otherwise wiew the logfile.

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

The following shows an example configuration:
    
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
Values for `appId` and `appKey` should be specified as noted in step 1 in _Setup Guide_ above.

| Key    | Description
| ------ | -----------
| tenant | Intune tenant to use _(ex. johnnycash.onmicrosoft.com)_.
| appId  | Application ID from Azure.
| appKey | API key to use when authenticating to cloud service.

### EJBCA configuration
Section (**ejbca:**) containing configuration needed in order to connect to EJBCA.

| Key              | Description
| ---------------- | -----------
| serviceName      | Arbitrary name and version of EJBCA service _(ex. EJBCA 6.3.1.1)_.
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
Section (**profile:**) containing configuration needed to issue certificates from EJBCA.

| Key                  | Description
| -------------------- | -----------
| certificateAuthority | Certificate authority to use when issuing certificates to mobile devices.
| certificateProfile   | Certificate profile to use for mobile device certificates.
| endEntityProfile     | End entity profile to use for mobile device certificates.
| baseDN               | Optional DN string to append to all certificates, ex: "O=Some Company,C=SE".

## Logging
Log is written to standard output which will be available in the application server log (eg. catalina.out)
and to a log file named `intune-ejbca-connector.log` within the application server directory.

## Copyright & License
Copyright (c) 2018 CGI Certificate Services - Released under the 
GNU Affero General Public License.