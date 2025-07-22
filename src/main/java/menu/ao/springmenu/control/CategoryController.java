package menu.ao.springmenu.control;

import jakarta.validation.Valid;
import menu.ao.springmenu.dto.CreateCategoryDto;
import menu.ao.springmenu.entity.Category;
import menu.ao.springmenu.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @PostMapping
    public ResponseEntity<Void> newCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {

        this.categoryService.createCategory(createCategoryDto);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<Category>> listAllCatgory() {
        var category = this.categoryService.getAllCategory();

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findACategory(@PathVariable Long id) {
        var category = this.categoryService.getCategoryById(id);

        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
