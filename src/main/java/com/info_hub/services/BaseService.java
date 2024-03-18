package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.repositories.BaseRepository;
import com.info_hub.dtos.responses.SimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public SimpleResponse<Entity> getAllOrSearchByKeyword(Map<String, String> params) {

        String q = params.get("q");
        q = q != null ? q : "";

        // search by keyword: tag, category repository.
        Pageable pageable = GetPageableUtil.getPageable(params);
        Page<Entity> results = baseRepository.search(q, pageable);

        SimpleResponse<Entity> response = new SimpleResponse<>();
        response.data = results.getContent();
        response.page = pageable.getPageNumber() + 1;
        response.limit = pageable.getPageSize();
        response.totalPage = results.getTotalPages();
        response.totalItems = (int) results.getTotalElements();
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
