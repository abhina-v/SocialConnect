package com.interestconnect.connection.controller;

import com.interestconnect.connection.dto.ConnectionResponse;
import com.interestconnect.connection.dto.SendConnectionRequest;
import com.interestconnect.connection.service.ConnectionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/request")
    public ConnectionResponse sendConnectionRequest(
            @Valid @RequestBody SendConnectionRequest request) {

        return connectionService.sendConnectionRequest(request);
    }

    @GetMapping("/received")
    public List<ConnectionResponse> getReceivedRequests() {

        return connectionService.getReceivedRequests();
    }

    @GetMapping("/sent")
    public List<ConnectionResponse> getSentRequests() {

        return connectionService.getSentRequests();
    }

    @PatchMapping("/{id}/accept")
    public ConnectionResponse acceptRequest(
            @PathVariable Long id) {

        return connectionService.acceptRequest(id);
    }

    @PatchMapping("/{id}/reject")
    public ConnectionResponse rejectRequest(
            @PathVariable Long id) {

        return connectionService.rejectRequest(id);
    }
}