package menu.ao.springmenu.control;

import jakarta.validation.Valid;
import menu.ao.springmenu.dto.CreateDishDto;
import menu.ao.springmenu.entity.Dish;
import menu.ao.springmenu.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @PostMapping
    public ResponseEntity<Void> newDish(@Valid @RequestBody CreateDishDto createDishDto) {

        this.dishService.createDish(createDishDto);
        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @Valid @RequestBody CreateDishDto createDishDto) {

        this.dishService.updateDish(id, createDishDto);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<Dish>> listAllDish() {
        var dish = this.dishService.getAllDish();

        return ResponseEntity.ok(dish);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> findADish(@PathVariable Long id) {
        var category = this.dishService.getDishById(id);

        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        this.dishService.deleteDish(id);
        return ResponseEntity.ok().build();
    }
}
