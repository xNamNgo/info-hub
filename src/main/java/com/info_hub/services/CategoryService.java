package com.info_hub.services;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.category.CategoryDTO;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.Category;
import com.info_hub.repositories.CategoryRepository;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.category.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CategoryService extends BaseService<Category> {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ModelMapper modelMapper) {
        super(categoryRepository); //  base method
        this.categoryRepository = categoryRepository; // custom method
        this.modelMapper = modelMapper;
    }


    public SimpleResponse<CategoryResponse> getAllCategories(Map<String, String> params) {
        SimpleResponse<Category> entityResponse = this.getAllOrSearchByKeyword(params);

        // convert entity data to dto data
        List<CategoryResponse> data = entityResponse.getData().stream()
                .map(entity -> modelMapper.map(entity, CategoryResponse.class))
                .toList();

        return SimpleResponse.<CategoryResponse>builder()
                .data(data)
                .page(entityResponse.getPage())
                .limit(entityResponse.getLimit())
                .totalPage(entityResponse.getTotalPage())
                .totalItems(entityResponse.getTotalItems())
                .build();
    }

    public CategoryResponse getCategoryById(Integer id) {
        Optional<Category> category = this.getById(id);
        if (category.isPresent()) {
            return  modelMapper.map(category, CategoryResponse.class);
        } else {
            throw new BadRequestException("Category not found with id: " + id);
        }

    }

    public ResponseMessage createCategory(CategoryDTO categoryDTO) {

        String name = categoryDTO.getName();
        String code = categoryDTO.getCode();
        // check unique name and code then update
        validateCreateCategory(name, code);

        Category newCategory = Category.builder()
                .name(name)
                .code(code)
                .build();


        // set parent
        Integer parentCategoryId = categoryDTO.getParentId();
        setParentCategory(parentCategoryId, newCategory);

        categoryRepository.save(newCategory);
        return ResponseMessage.success();
    }

    /**
     * Sets the parent category for a given subcategory.
     *
     * @param parentId:    The ID of the parent category
     * @param subCategory: The subcategory for which to set the parent category.
     */
    private void setParentCategory(Integer parentId, Category subCategory) {
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId).get();
            subCategory.setParentCategory(parent);
        }
    }

    public ResponseMessage updateCategory(Integer id, CategoryDTO categoryUpdate) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequestException("Category not found!"));
        // if the data don't change -> not update
        validateUpdateCategory(existingCategory, categoryUpdate);
        setParentCategory(categoryUpdate.getParentId(), existingCategory);
        categoryRepository.save(existingCategory);
        return ResponseMessage.success();
    }

    public ResponseMessage deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Category not found with id: " + id));

        // Check if the category has any subcategories
        if (categoryRepository.existsByParentCategory_Id(id)) {
            throw new BadRequestException("Cannot delete this category as it currently has subcategories.");
        }

        // If no subcategories exist, delete the category
        categoryRepository.delete(category);

        return ResponseMessage.success();
    }

    /**
     * Check unique before create category
     *
     * @param name: Tin tức
     * @param code: tin-tuc
     */
    private void validateCreateCategory(String name, String code) {
        boolean isNameUnique = categoryRepository.existsByName(name);
        boolean isCodeUnique = categoryRepository.existsByCode(code);

        if (isCodeUnique) {
            throw new BadRequestException("The code of category is exist!");
        }

        if (isNameUnique) {
            throw new BadRequestException("The name of category is exist!");
        }
    }

    /**
     * Check for unique name and code before updating.
     * If the changed name or code already exists in another category, then throw an exception.
     *
     * @param existCategory:  The name and code in the current category.
     * @param categoryUpdate: The name or code after the update.
     */
    private void validateUpdateCategory(Category existCategory, CategoryDTO categoryUpdate) {
        String name = categoryUpdate.getName();
        String code = categoryUpdate.getCode();

        // if the name change
        if (!existCategory.getName().equals(name)) {
            boolean isNameExist = categoryRepository.existsByName(name);
            if (isNameExist) {
                throw new BadRequestException("The name of category is exist!");
            }

            existCategory.setName(name);
        }

        // if the code change
        if (!existCategory.getCode().equals(code)) {
            boolean isCodeExist = categoryRepository.existsByCode(code);

            if (isCodeExist) {
                throw new BadRequestException("The code of category is exist!");
            }

            existCategory.setCode(code);
        }
    }
}