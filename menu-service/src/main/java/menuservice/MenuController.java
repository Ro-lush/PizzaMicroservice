package menuservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<Menu> getMenu() {
        return menuRepository.findAll();
    }

    @GetMapping("/{id}")
    public Menu getMenuItem(@PathVariable Long id) {
        return menuRepository.findById(id).orElseThrow();
    }
}
