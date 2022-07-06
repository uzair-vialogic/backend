package digital.vialogic.SupportService.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import digital.vialogic.SupportService.dto.SupportRequest;
import digital.vialogic.SupportService.dto.SupportResponse;
import digital.vialogic.SupportService.service.SupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class SupportController {

    private SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @CrossOrigin
    @PostMapping("/submitQuery")
    ResponseEntity<SupportResponse> submitQuery(
            @RequestBody @Valid @NotNull SupportRequest supportRequest
    ) throws JsonProcessingException {
        return ResponseEntity.ok(supportService.sendSupportQuery(supportRequest));
    }
}
