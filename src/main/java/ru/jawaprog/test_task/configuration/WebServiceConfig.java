package ru.jawaprog.test_task.configuration;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import ru.jawaprog.test_task.web.utils.LogInterceptor;

import java.util.List;

@EnableWs
@EnableWebMvc
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public DispatcherServletPath dispatcherServletPath() {
        return () -> "/ws/";
    }

    @Bean
    public ServletRegistrationBean dispatcherServlet2(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "soap-api")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClientsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://jawaprog.ru/test-task-mts");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        LogInterceptor logger = new LogInterceptor();
        interceptors.add(logger);

        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setSchemaLanguage("ru");
        validatingInterceptor.setXsdSchema(soapApiSchema());
        interceptors.add(validatingInterceptor);
    }

    @Bean
    public XsdSchema soapApiSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap-api.xsd"));
    }
}
