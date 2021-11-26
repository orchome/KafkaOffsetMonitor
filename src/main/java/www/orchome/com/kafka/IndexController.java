package www.orchome.com.kafka;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String jsp(Model model) {
        model.addAttribute("message", "this is index jsp page!");
        return "index";
    }
}
