package ru.jawaprog.test_task.web.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.xml.transform.TransformerHelper;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Log4j2
public class LogInterceptor implements EndpointInterceptor {

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {

        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        MethodEndpoint me = (MethodEndpoint) endpoint;
        StringWriter message = new StringWriter();

        message.append("\nRequest method: ").append(me.getMethod().getName());

        Transformer transformer = createNonIndentingTransformer();
        message.append("\nRequest body: \n");
        transformer.transform(messageContext.getRequest().getPayloadSource(), new StreamResult(message));

        message.append("Response body: \n");
        transformer.transform(messageContext.getResponse().getPayloadSource(), new StreamResult(message));
        log.info(message);
        if (ex != null) log.info("была ошибка " + ex.getLocalizedMessage());
    }

    private Transformer createNonIndentingTransformer() throws TransformerConfigurationException {
        Transformer transformer = new TransformerHelper().createTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }
}
