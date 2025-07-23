package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateDishDto;
import menu.ao.springmenu.entity.Dish;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.PersistenceException;
import menu.ao.springmenu.repository.DishRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishService {

    private final CategoryService categoryService;
    private final DishRepository dishRepository;

    public DishService(CategoryService categoryService, DishRepository dishRepository) {
        this.categoryService = categoryService;
        this.dishRepository = dishRepository;
    }


    public void createDish(CreateDishDto createDishDto) {

        var categoryId = this.categoryService.getCategoryById(createDishDto.categoryId());

        var dish = new Dish();

        dish.setName(createDishDto.name());
        dish.setDescription(createDishDto.description());
        dish.setPrice(createDishDto.price());
        dish.setCategory(categoryId);
        dish.setImageUrl(createDishDto.imageUrl());
        dish.setCreatedAt(LocalDateTime.now());

        try {
            this.dishRepository.save(dish);
        } catch (IllegalArgumentException e) {

            throw new PersistenceException("Erro a cadastrar");
        } catch (OptimisticLockingFailureException e) {

            throw new PersistenceException("Erro a cadastrar");
        }

    }


    public Dish getDishById(Long id) {

        return this.dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dish nao encontrado"));
    }

    public List<Dish> getDishsByIds(List<Long> ids) {
        List<Dish> dishes = dishRepository.findAllById(ids);

        if (dishes.size() != ids.size()) {
            throw new NotFoundException("Um ou mais Dishes não foram encontrados.");
        }
        return dishes;
    }

    public List<Dish> getAllDish() {
        return this.dishRepository.findAll();
    }

    public List<Dish> getAllDishPage(Pageable pageable) {

        return this.dishRepository.findAll(pageable).getContent();
    }

    public Dish updateDish(Long id, CreateDishDto updateDish) {

        var categoryId = this.categoryService.getCategoryById(updateDish.categoryId());
        return this.dishRepository.findById(id)
                .map(dish -> {
                    dish.setName(updateDish.name());
                    dish.setDescription(updateDish.description());
                    dish.setPrice(updateDish.price());
                    dish.setCategory(categoryId);
                    dish.setImageUrl(updateDish.imageUrl());
                    return this.dishRepository.save(dish);
                })
                .orElseThrow(() -> new NotFoundException("Dish não encontrado"));
    }

    public void deleteDish(Long id) {
        if (!this.dishRepository.existsById(id)) {
            throw new NotFoundException("Dish não encontrado");
        }
        this.dishRepository.deleteById(id);
    }
}
