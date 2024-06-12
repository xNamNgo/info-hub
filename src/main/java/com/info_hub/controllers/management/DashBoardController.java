package com.info_hub.controllers.management;


import com.info_hub.dtos.responses.dashboard.ActivityLogResponse;
import com.info_hub.dtos.responses.dashboard.CountResponse;
import com.info_hub.dtos.responses.dashboard.CountUserResponse;
import com.info_hub.dtos.responses.dashboard.DataResponse;
import com.info_hub.services.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {
    private final DashBoardService service;

    @GetMapping("/total-articles")
    public ResponseEntity<CountResponse> countArticleByStatus() {
        return ResponseEntity.ok(service.countArticleByStatus());
    }

    @GetMapping("/total-comments")
    public ResponseEntity<CountResponse> countCommentsByStatus() {
        return ResponseEntity.ok(service.countCommentsByStatus());
    }

    @GetMapping("/total-users")
    public ResponseEntity<CountUserResponse> countUsers() {
        return ResponseEntity.ok(service.countUsers());
    }

    @GetMapping("/activity-log")
    public ResponseEntity<DataResponse<ActivityLogResponse>> getActivityLog() {
        return ResponseEntity.ok(service.getActivityLog());

    }
}
