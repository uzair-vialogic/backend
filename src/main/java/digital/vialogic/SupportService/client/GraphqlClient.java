package digital.vialogic.SupportService.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import digital.vialogic.SupportService.dto.GraphQlDaoResponse;
import digital.vialogic.SupportService.dto.PersonDetails;
import digital.vialogic.SupportService.dto.ServiceInfo;
import digital.vialogic.SupportService.dto.SupportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class GraphqlClient {

    @Value("${graphql.dao.layer.uri}")
    private String graphqlUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private final RestTemplate restTemplate;

    public GraphqlClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String submitQuery(PersonDetails personDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String MUTATION_QUERY = "\"mutation($support:SupportInput!){\n" +
                "supportQuery(supportQuery:$support){\n" +
                "message\n" +
                "}}\"";
        String QUERY = "{\"query\": " + MUTATION_QUERY + ", \"variables\": { " +
                "\"support\": " + objectMapper.writeValueAsString(personDetails) + "}}";

        String status = null;
        try {
            Gson gson = new Gson();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonReader reader = new JsonReader(new StringReader(QUERY.trim()));
            reader.setLenient(true);
            Object json = gson.fromJson(reader, Object.class);
            HttpEntity<Object> entity = new HttpEntity<>(json, headers);
            ResponseEntity<GraphQlDaoResponse> restResponse = restTemplate.postForEntity(graphqlUrl, entity, GraphQlDaoResponse.class);
            if (Objects.requireNonNull(restResponse.getBody()).getData().getSupportQuery() != null) {
                status = restResponse.getBody().getData().getSupportQuery().getMessage();
                log.info("User Query submitted Successfully");
            }
        } catch (Exception er) {
            log.info("Failed To sent query: " + er.getLocalizedMessage());
        }
        return status;
    }

    public ServiceInfo getServiceInfo(SupportRequest supportRequest) {
        assert supportRequest.getRequestData() != null;
        ServiceInfo serviceInfo = supportRequest.getRequestData().getServiceInfo();
        assert serviceInfo != null;
        serviceInfo.setService(applicationName);
        serviceInfo.setOrigin(applicationName);
        serviceInfo.setTimestamp(String.valueOf(LocalDateTime.now()));
        return serviceInfo;
    }
}
