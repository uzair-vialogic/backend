package digital.vialogic.SupportService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import digital.vialogic.SupportService.client.EventClient;
import digital.vialogic.SupportService.client.GraphqlClient;
import digital.vialogic.SupportService.dto.*;
import digital.vialogic.SupportService.dto.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SupportService {
    private EventClient eventClient;
    private GraphqlClient graphqlClient;


    public SupportService(EventClient eventClient, GraphqlClient graphqlClient) {
        this.eventClient = eventClient;
        this.graphqlClient = graphqlClient;
    }

    public SupportResponse sendSupportQuery(SupportRequest supportRequest) throws JsonProcessingException {
        boolean isSent = eventClient.sendEvent(supportRequest);
        if(isSent){
            log.info("Event Published Successfully");
        }

        SupportResponse response = new SupportResponse();
        assert supportRequest.getRequestData() != null;
        if(supportRequest.getRequestData().getPerson() != null){
            String status = graphqlClient.submitQuery(
                    supportRequest.getRequestData().getPerson().getPersonDetails());

            if(status != null){
                response.setSuccessResponseData(
                        new SuccessResponseData(
                                new Payload(
                                        new Status("Your Query Submitted Successfully")
                                ),
                                graphqlClient.getServiceInfo(supportRequest)
                        )
                );
            }else{
                response.setErrorResponseData(
                        new ErrorResponseData(
                            graphqlClient.getServiceInfo(supportRequest),
                                new Error(
                                        503,
                                        "503",
                                        "System Error, Please try again",
                                        "message Pointer"
                                )
                        )
                );
            }

        }else{
            log.info("No personal Details in the request body");
        }

        return response;
    }
}
