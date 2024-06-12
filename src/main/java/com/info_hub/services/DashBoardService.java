package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.responses.dashboard.ActivityLogResponse;
import com.info_hub.dtos.responses.dashboard.CountResponse;
import com.info_hub.dtos.responses.dashboard.CountUserResponse;
import com.info_hub.dtos.responses.dashboard.DataResponse;
import com.info_hub.enums.Role;
import com.info_hub.enums.Status;
import com.info_hub.models.Article;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.comment.CommentRepository;
import com.info_hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CountResponse countArticleByStatus() {
        return CountResponse.builder()
                .approvedTotals((int) articleRepository.countByStatus(Status.APPROVED))
                .pendingTotals((int) articleRepository.countByStatus(Status.PENDING))
                .rejectedTotals((int) articleRepository.countByStatus(Status.REJECTED))
                .build();
    }

    public CountResponse countCommentsByStatus() {
        return CountResponse.builder()
                .approvedTotals((int) commentRepository.countByStatus(Status.APPROVED))
                .pendingTotals((int) commentRepository.countByStatus(Status.PENDING))
                .rejectedTotals((int) commentRepository.countByStatus(Status.REJECTED))
                .build();
    }

    public CountUserResponse countUsers() {

        return CountUserResponse.builder()
                .userTotals((int) userRepository.countByRole_Code(Role.ROLE_USER.toString()))
                .collaboratorTotals((int) userRepository.countByRole_Code(Role.ROLE_COLLABORATOR.toString()))
                .journalistTotals((int) userRepository.countByRole_Code(Role.ROLE_JOURNALIST.toString()))
                .build();
    }

    public DataResponse<ActivityLogResponse> getActivityLog() {
        Map<String, String> params = new HashMap<>();
        params.put("limit", "5");
        params.put("page", "1");
        params.put("s_status",Status.APPROVED.toString());

        Pageable pageable = GetPageableUtil.getPageable(params);
        List<Article> articleEntities = articleRepository.findByCondition(params, pageable).getData();

        List<ActivityLogResponse> result = articleEntities.stream()
                .map(item -> modelMapper.map(item, ActivityLogResponse.class))
                .toList();

        return DataResponse.<ActivityLogResponse>builder()
                .data(result)
                .build();
    }

}
