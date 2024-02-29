package com.info_hub;


import com.info_hub.models.Category;
import com.info_hub.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CategoryTest {

    @Autowired
    CategoryRepository repository;

    @Test
    public void findAll() {
        List<Category> list = repository.findAll();
        showList(list);
    }

    @Test
    public void findSubCategory() {
        Integer id = 1;
        List<Category> list = repository.findSubCategory(id);
        showList(list);
    }


    private void showList(List<Category> list) {
        list.forEach(item -> System.out.println(item.getCode()));
    }




}
