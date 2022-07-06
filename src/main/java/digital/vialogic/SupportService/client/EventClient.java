package digital.vialogic.SupportService.client;

import digital.vialogic.SupportService.dto.ServiceInfo;
import digital.vialogic.SupportService.dto.SupportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EventClient {

    @Value("${event.publisher.url}")
    private String eventUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private RestTemplate restTemplate;

    public EventClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean sendEvent(SupportRequest supportRequest){
        SupportRequest request = createSubscriptionRequest(supportRequest);
        boolean isSent = false;
        try {
            // Responsible for sending data to Graphql using rest template
            ResponseEntity<Object> publisherResponseEntity =
                    restTemplate.postForEntity(eventUrl, request, Object.class);
            log.info("Data Sent successfully over rest template");
            isSent = true;
        } catch (Exception er) {
            log.info("Failed to publish event: " + er.getLocalizedMessage());
        }
        return isSent;
    }

    private SupportRequest createSubscriptionRequest(SupportRequest subscriptionRequest){
        assert subscriptionRequest.getRequestData() != null;
        ServiceInfo serviceInfo = subscriptionRequest.getRequestData().getServiceInfo();
        assert serviceInfo != null;
        serviceInfo.setService(applicationName);
        serviceInfo.setOrigin(applicationName);
        serviceInfo.setTimestamp(String.valueOf(LocalDateTime.now()));

        subscriptionRequest.getRequestData().setServiceInfo(serviceInfo);
        return subscriptionRequest;
    }
}
