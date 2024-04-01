package com.info_hub.controllers.auth;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.auth.ProfileDTO;
import com.info_hub.dtos.auth.ResetPasswordDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.article.SavedArticleResponse;
import com.info_hub.dtos.responses.user.AllMyCommentResponse;
import com.info_hub.dtos.responses.user.ProfileResponse;
import com.info_hub.dtos.responses.user.UploadAvatarResponse;
import com.info_hub.models.Image;
import com.info_hub.services.ImageService;
import com.info_hub.services.auth.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/my-account")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ImageService imageService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        ProfileResponse response = profileService.getMyProfile();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/upload-avatar")
    public ResponseEntity<UploadAvatarResponse> uploadAvatar(@RequestParam("file") MultipartFile img) {
        Image image = imageService.createImage(img);
        return ResponseEntity.ok(profileService.uploadAvatar(image));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ResponseMessage> updateProfile(@RequestBody ProfileDTO profileDTO) {
        ResponseMessage response = profileService.updateProfile(profileDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody @Valid ResetPasswordDTO req) {
        ResponseMessage response = profileService.changePassword(req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/article-saved")
    public ResponseEntity<SimpleResponse<SavedArticleResponse>> getListSavedArticle(
            @RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(profileService.getArticleSaved(params));
    }

    @GetMapping("/my-comments")
    public ResponseEntity<List<AllMyCommentResponse>> getListSavedArticle(){
        return ResponseEntity.ok(profileService.getAllMyComment());
    }
}
