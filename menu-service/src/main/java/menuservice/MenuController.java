package menuservice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    @GetMapping
    public String getAllMenu(Model model) {
        model.addAttribute("menu", menuRepository.findAll());
        return "menu-list";
    }

    @PostMapping
    public Menu addMenuItem(@RequestBody Menu menuItem) {
        return menuRepository.save(menuItem);
    }

    @GetMapping("/add-form")
    public String showForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "add-menu-item";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Menu item) {
        menuRepository.save(item);
        return "redirect:/menu";
    }


    // REST Endpoints
    @GetMapping("/api")
    @ResponseBody
    public List<Menu> getAllMenuApi() {
        return menuRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Menu getMenuItemApi(@PathVariable Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пункт меню не найден"));
    }
}
