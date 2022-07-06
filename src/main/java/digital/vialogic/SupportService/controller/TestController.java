package digital.vialogic.SupportService.controller;

import digital.vialogic.SupportService.dto.*;
import digital.vialogic.SupportService.dto.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/testService")
    ResponseEntity<SupportResponse> testService(@RequestBody SupportRequest supportRequest){
        SupportResponse response = new SupportResponse();
        ServiceInfo serviceInfo = supportRequest.getRequestData().getServiceInfo();
        try {
            response.setSuccessResponseData(
                    new SuccessResponseData(
                            new Payload(
                                    new Status("Support Service Hits...")
                            ),
                            serviceInfo
                    )
            );
        }catch (Exception err){
            response.setErrorResponseData(
                    new ErrorResponseData(
                            serviceInfo,
                            new Error(
                                    400,
                                    "400",
                                    "Bad Request",
                                    "messagePointer"
                            )
                    )
            );
        }

        return ResponseEntity.ok(response);
    }
}
