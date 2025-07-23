package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateCategoryDto;
import menu.ao.springmenu.dto.CreateDishDto;
import menu.ao.springmenu.entity.Category;
import menu.ao.springmenu.entity.Dish;
import menu.ao.springmenu.entity.Role;
import menu.ao.springmenu.entity.User;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.repository.CategoryRepository;
import menu.ao.springmenu.repository.DishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @Captor
    private ArgumentCaptor<Dish> dishArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> dishByIdArgumentCaptor;

    @Nested
    class createDish{

        @Test
        @DisplayName("create a dish with sucess")
        void createDishWithSucess(){

            Category mockCategory = new Category();
            mockCategory.setId(1L);
            when(categoryService.getCategoryById(1L))
                    .thenReturn(mockCategory);

            when(dishRepository.save(any(Dish.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            var input = new CreateDishDto("dish1", "Description", 20D, "imageurl", 1L);
            dishService.createDish(input);

            verify(dishRepository).save(dishArgumentCaptor.capture());
            Dish savedDish = dishArgumentCaptor.getValue();

            assertEquals(input.name(), savedDish.getName());
            assertEquals(input.description(), savedDish.getDescription());
            assertEquals(input.price(), savedDish.getPrice());
            assertEquals(input.imageUrl(), savedDish.getImageUrl());
            assertEquals(1L, savedDish.getCategory().getId());

            verify(categoryService).getCategoryById(1L);
        }
    }

    @Nested
    class getDishById{
        @Test
        @DisplayName("get a dish with sucess")
        void getDishByIdWithSucess(){

            Dish  dish = new Dish();
            dish.setId(1L);

            doReturn(Optional.of(dish)).when(dishRepository).findById(dishByIdArgumentCaptor.capture());

            var output = dishService.getDishById(dish.getId());
            assertEquals(output.getId(), dishByIdArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("dont get a category with sucess")
        void dontGetCategoryByIdWithSucess(){
            var nonExistentId = 1L;
            when(dishRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> {
                dishService.getDishById(nonExistentId);
            });
        }
    }

    @Nested
    class deleteDishById{
        @Test
        @DisplayName("delete a dish with sucess")
        void deleteDishByIdWithSucess(){
            doReturn(true)
                    .when(dishRepository)
                    .existsById(dishByIdArgumentCaptor.capture());

            doNothing()
                    .when(dishRepository)
                    .deleteById(dishByIdArgumentCaptor.capture());

            var dishId = 1L;
            dishService.deleteDish(dishId);
            var allId = dishByIdArgumentCaptor.getAllValues();
            assertEquals(dishId, allId.get(0));
            assertEquals(dishId, allId.get(1));
        }

        @Test
        @DisplayName("dont delete a dish with sucess")
        void dontDeleteDishByIdWithSucess(){
            var nonExistentId = 1L;
            assertThrows(NotFoundException.class, () -> dishService.deleteDish(nonExistentId));

            verify(dishRepository, never()).delete(any());
        }
    }

    @Nested
    class getAllDish {
        @Test
        void getAllDishsWithSucess() {

            Dish expected = new Dish();
            expected.setId(1L);
            expected.setName("cat1");
            expected.setDescription("Description");
            expected.setImageUrl("imageurl");
            expected.setCreatedAt(LocalDateTime.now());

            doReturn(List.of(expected)).
                    when(dishRepository).
                    findAll();

            var output = dishService.getAllDish();
            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

    @Nested
    class getAllDishPage {
        @Test
        void getAllDishsPageWithSucess() {

            Dish expected = new Dish();
            expected.setId(1L);
            expected.setName("cat1");
            expected.setDescription("Description");
            expected.setImageUrl("imageurl");
            expected.setCreatedAt(LocalDateTime.now());
            Pageable pageable = PageRequest.of(0, 2);
            List<Dish> dishList = List.of(expected);
            Page<Dish> dishPage = new PageImpl<>(dishList, pageable, dishList.size());

            doReturn(dishPage).
                    when(dishRepository).
                    findAll(pageable);

            var output = dishService.getAllDishPage(pageable);
            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

}