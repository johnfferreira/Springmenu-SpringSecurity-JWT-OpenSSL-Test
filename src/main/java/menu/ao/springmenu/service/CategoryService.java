package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateCategoryDto;
import menu.ao.springmenu.entity.Category;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.PersistenceException;
import menu.ao.springmenu.repository.CategoryRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public void createCategory(CreateCategoryDto categoryDto) {


        var category = new Category();

        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        try {
            categoryRepository.save(category);
        } catch (IllegalArgumentException e) {

            throw new PersistenceException("Erro a cadastrar");
        } catch (OptimisticLockingFailureException e) {

            throw new PersistenceException("Erro a cadastrar");
        }

    }


    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria nao encontrado"));
    }


    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public Category updateCategory(Long id, CreateCategoryDto updatedCategory) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.name());
                    category.setDescription(updatedCategory.description());
                    category.setUpdatedAt(LocalDateTime.now());

                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrado");
        }
       this.categoryRepository.deleteById(id);
    }
}
