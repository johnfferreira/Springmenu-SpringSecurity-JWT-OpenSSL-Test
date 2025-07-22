package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateCategoryDto;
import menu.ao.springmenu.entity.Category;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> categoryByIdArgumentCaptor;

    @Nested
    class createCategory{

        @Test
        @DisplayName("create a category with sucess")
        void createCategoryWithSucess(){
            Category expectedCategory = new Category();
            expectedCategory.setId(1L);
            expectedCategory.setName("cat1");
            expectedCategory.setDescription("Description");
            expectedCategory.setCreatedAt(LocalDateTime.now());
            expectedCategory.setUpdatedAt(LocalDateTime.now());


            doReturn(expectedCategory).when(categoryRepository).save(categoryArgumentCaptor.capture());
            var input = new CreateCategoryDto("cat1", "Description");
            categoryService.createCategory(input);
            var cateCap = categoryArgumentCaptor.getValue();

            assertEquals(input.name(), cateCap.getName());
            assertEquals(input.description(), cateCap.getDescription());
            verify(categoryRepository).save(any(Category.class));
        }
    }

    @Nested
    class getCategoryById{
        @Test
        @DisplayName("get a category with sucess")
        void getCategoryByIdWithSucess(){
            Category  category = new Category();
            category.setId(1L);

            doReturn(Optional.of(category)).when(categoryRepository).findById(categoryByIdArgumentCaptor.capture());

            var output = categoryService.getCategoryById(category.getId());
            assertEquals(output.getId(), categoryByIdArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("dont get a category with sucess")
        void dontGetCategoryByIdWithSucess(){
            var nonExistentId = 1L;
            when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> {
                categoryService.getCategoryById(nonExistentId);
            });
        }
    }

    @Nested
    class deleteCategoryById{
        @Test
        @DisplayName("delete a category with sucess")
        void deleteCategoryByIdWithSucess(){
            doReturn(true)
                    .when(categoryRepository)
                    .existsById(categoryByIdArgumentCaptor.capture());

            doNothing()
                    .when(categoryRepository)
                    .deleteById(categoryByIdArgumentCaptor.capture());

            var catId = 1L;
            categoryService.deleteCategory(catId);
            var allId = categoryByIdArgumentCaptor.getAllValues();
            assertEquals(catId, allId.get(0));
            assertEquals(catId, allId.get(1));
        }

        @Test
        @DisplayName("dont delete a category with sucess")
        void dontDeleteCategoryByIdWithSucess(){
            var nonExistentId = 1L;
            assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(nonExistentId));

            verify(categoryRepository, never()).delete(any());
        }
    }

    @Nested
    class getAllCategories {
        @Test
        void getAllCategoriesWithSucess() {

            Category expectedCategory = new Category();
            expectedCategory.setId(1L);
            expectedCategory.setName("cat1");
            expectedCategory.setDescription("Description");
            expectedCategory.setCreatedAt(LocalDateTime.now());
            expectedCategory.setUpdatedAt(LocalDateTime.now());

            doReturn(List.of(expectedCategory)).
                    when(categoryRepository).
                    findAll();

            var output = categoryService.getAllCategory();
            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

}