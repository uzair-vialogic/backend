package digital.vialogic.SupportService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import digital.vialogic.SupportService.client.EventClient;
import digital.vialogic.SupportService.client.GraphqlClient;
import digital.vialogic.SupportService.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SupportServiceTest {

    @Mock
    private EventClient eventClient;

    @Mock
    private GraphqlClient graphqlClient;

    private SupportService supportService;

    @Before
    public void setUp(){
        supportService = new SupportService(eventClient, graphqlClient);
    }

    @Test
    public void sendSupport_ShouldReturnSuccessResponse_WhenRequestReceived() throws JsonProcessingException {
        // GIVEN
        SupportRequest request = createMockRequest();
        when(eventClient.sendEvent(request)).thenReturn(true);
        when(graphqlClient.submitQuery(getPersonDetails())).thenReturn("Status");
        // WHEN
        SupportResponse response = supportService.sendSupportQuery(request);
        // THEN
        assertNotNull(response);
        assertNull(response.getErrorResponseData());
        assertNotNull(response.getSuccessResponseData());
    }


    private SupportRequest createMockRequest(){
        return new SupportRequest(
                getRequestData()
        );
    }

    private RequestData getRequestData(){
        return new RequestData(
                getPerson(),
                getServiceInfo()
        );
    }

    private Person getPerson(){
        return new Person(
                getPersonDetails()
        );
    }

    private PersonDetails getPersonDetails(){
        return new PersonDetails(
                "firstName",
                "lastName",
                "email",
                "mobileNumber",
                "message"
        );
    }

    private ServiceInfo getServiceInfo(){
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setService("service");
        serviceInfo.setIam("Iam");
        serviceInfo.setPartyType("LEGAL_ENTITY");
        serviceInfo.setOrigin("identityService");
        serviceInfo.setInstance("EEEEE");
        serviceInfo.setTransactionId("fcfd663c-d44f-4029-b632-8ff1a24fc34d");
        serviceInfo.setChannelId("DDDDD");
        serviceInfo.setAccountProviderId("eeeee");
        serviceInfo.setUserId("dddd");
        serviceInfo.setCountryId("pak");
        serviceInfo.setTimestamp("16/03/2022 14:26:52");
        serviceInfo.setLocation("Pakistan");
        serviceInfo.setActivity("Registration");
        return serviceInfo;
    }
}
