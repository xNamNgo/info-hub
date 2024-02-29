package com.info_hub.services;

import com.info_hub.constant.EnvironmentConstant;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.repositories.BaseRepository;
import com.info_hub.responses.ListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.Optional;

public class BaseService<Entity> {
    protected final BaseRepository<Entity> baseRepository;

    public BaseService(BaseRepository<Entity> baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * @param params: A map containing parameters for pagination and search:
     *  *            - "page": The page number for pagination. Default is 1 if not provided.
     *  *            - "limit": The maximum number of items per page. Default is 10 if not provided.
     *  *            - "q": The keyword for searching Entity.
     *  *                   If not provided, all Entity are retrieved.
     * @return A ListResponse containing a list of Entity objects.
     */
    public ListResponse<Entity> getAll(Map<String, String> params) {
        // page number
        int page = EnvironmentConstant.PAGE_DEFAULT_INDEX;
        if (params.get("page") != null) {
            // default 1 instead 0
            page = Integer.parseInt(params.get("page")) - 1;
        }

        // limit page
        int limit = EnvironmentConstant.LIMIT_DEFAULT;
        if (params.get("limit") != null) {
            limit = Integer.parseInt(params.get("limit"));
        }

        // validate
        if (page < 0) {
            throw new BadRequestException("Page number must be greater than 0");
        }
        if (limit <= 0) {
            throw new BadRequestException("Value must be positive");
        }

        Page<Entity> pages;
        String q = params.get("q");
        if (q != null) {
            pages = baseRepository.search(q, PageRequest.of(page, limit));
        } else {
            pages = baseRepository.findAll(PageRequest.of(page, limit));
        }

        ListResponse<Entity> response = new ListResponse<>();
        response.data = pages.getContent();
        response.page = page + 1;
        response.limit = limit;
        response.totalPage = pages.getTotalPages();
        response.totalItems = (int) pages.getTotalElements();
        return response;
    }

    public Optional<Entity> getById(Integer id) {
        return baseRepository.findById(id);
    }

//    public void delete(int id) {
//        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
//
//        // get actual name of class because name like "com.package.Entity"
//        // so need to split to get "Entity"
//        String[] splitClassName = className.split("\\.");
//        String nameEntity = splitClassName[splitClassName.length - 1];
//
//        Entity entity = baseRepository.findById(id).orElseThrow(() -> new NotFoundExcept(nameEntity,"id",id));
//
//        baseRepository.delete(entity);
//    }


}
