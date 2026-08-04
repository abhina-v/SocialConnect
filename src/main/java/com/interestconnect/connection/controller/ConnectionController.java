package com.interestconnect.connection.controller;

import com.interestconnect.connection.dto.ConnectionResponse;
import com.interestconnect.connection.dto.SendConnectionRequest;
import com.interestconnect.connection.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Connections",
        description = "Send and manage connection requests"
)
@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Operation(
            summary = "Send Connection Request",
            description = "Sends a connection request to the owner of a topic."
    )
    @PostMapping("/request")
    public ConnectionResponse sendConnectionRequest(
            @Valid @RequestBody SendConnectionRequest request) {

        return connectionService.sendConnectionRequest(request);
    }

    @Operation(
            summary = "Get Received Requests",
            description = "Returns all connection requests received by the authenticated user."
    )
    @GetMapping("/received")
    public List<ConnectionResponse> getReceivedRequests() {

        return connectionService.getReceivedRequests();
    }

    @Operation(
            summary = "Get Sent Requests",
            description = "Returns all connection requests sent by the authenticated user."
    )
    @GetMapping("/sent")
    public List<ConnectionResponse> getSentRequests() {

        return connectionService.getSentRequests();
    }

    @Operation(
            summary = "Accept Connection Request",
            description = "Accepts a pending connection request and automatically creates a conversation."
    )
    @PatchMapping("/{id}/accept")
    public ConnectionResponse acceptRequest(
            @PathVariable Long id) {

        return connectionService.acceptRequest(id);
    }

    @Operation(
            summary = "Reject Connection Request",
            description = "Rejects a pending connection request."
    )
    @PatchMapping("/{id}/reject")
    public ConnectionResponse rejectRequest(
            @PathVariable Long id) {

        return connectionService.rejectRequest(id);
    }
}