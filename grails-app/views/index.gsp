<%@ page import="org.certificateservices.intune.ejbcaconnector.EjbcaService" %>
<%@ page import="org.certificateservices.intune.ejbcaconnector.IntuneService" %>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Intunes EJBCA Connector</title>
</head>
<body>
    <div id="content" role="main">
        <section class="row colset-2-its">
            <asset:image src="logo.png" width="128" height="128"/>
            <h1>Intune EJBCA Connector</h1>
            <g:if test="${!EjbcaService.isInitialized()}">
                <p>Failed to initialize connection to EJBCA! Please check log files.</p>
            </g:if>
            <g:elseif test="${!IntuneService.isInitialized()}">
                <p>Failed to initialize connection to Microsoft Intune! Please check log files.</p>
            </g:elseif>
            <g:else>
                <p>Ready to serve!</p>
            </g:else>
            <p>
                <a href="https://github.com/agerbergt/intune-ejbca-connector/blob/master/README.md" target="_blank">View Documentation on Github</a>
            </p>
        </section>
    </div>
</body>
</html>
