package com.info_hub.components;

import com.info_hub.constant.EnvironmentConstant;
import com.info_hub.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class GetPageableUtil {

    /**
     * Define a method to handle common pagination logic for consistency:
     *
     * @param params
     * @return
     */
    public static Pageable getPageable(Map<String, String> params) {
        int page = EnvironmentConstant.PAGE_DEFAULT_INDEX;
        int limit = EnvironmentConstant.LIMIT_DEFAULT;
        try {
            if (params.get("page") != null) {
                page = Integer.parseInt(params.get("page")) - 1; // Adjust for 1-based indexing
            }
            if (params.get("limit") != null) {
                limit = Integer.parseInt(params.get("limit"));
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid page or limit values");
        }

        // Validate input
        if (page < 0) {
            throw new BadRequestException("Page number must be greater than or equal to 0");
        }
        if (limit <= 0) {
            throw new BadRequestException("Limit must be greater than 0");
        }

        // sorting
        Sort sort = Sort.by("createdDate","id").descending();
        String sortDir = params.get("sort");
        if (sortDir != null) {
            if (sortDir.equals("asc")) {
                sort = sort.ascending();
            }
        }

        return PageRequest.of(page, limit, sort);
    }
}
