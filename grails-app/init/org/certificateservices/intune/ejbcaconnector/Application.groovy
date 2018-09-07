package org.certificateservices.intune.ejbcaconnector

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.env.Environment;


class Application extends GrailsAutoConfiguration implements EnvironmentAware {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    void setEnvironment(Environment environment) {
        String configPath = System.properties["config.location"] ?: '/etc/intune-ejbca-connector.yml'
        Resource resourceConfig = new FileSystemResource(configPath)
        YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean()
        ypfb.setResources(resourceConfig)
        ypfb.afterPropertiesSet()
        Properties properties = ypfb.getObject()
        environment.propertySources.addFirst(new PropertiesPropertySource("config.location", properties))
    }
}