package menuservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /*   @GetMapping
       public List<Menu> getMenu() {
           return menuRepository.findAll();
       }

       @GetMapping("/{id}")
       public Menu getMenuItem(@PathVariable Long id) {
           return menuRepository.findById(id).orElseThrow();
       }*/
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
}
