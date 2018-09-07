import org.certificateservices.intune.ejbcaconnector.IntuneScepServlet
import org.springframework.boot.web.servlet.ServletRegistrationBean

beans = {
    intuneScepServlet(IntuneScepServlet, grailsApplication.config) {
        intuneService = ref("intuneService")
        ejbcaService = ref("ejbcaService")
    }

    dispatcherServletRegistration(ServletRegistrationBean, ref("intuneScepServlet"), ['/scep', '/scep/*']) {
        loadOnStartup = 1
        asyncSupported = true
    }
}