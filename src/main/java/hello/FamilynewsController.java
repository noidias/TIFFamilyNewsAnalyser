package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FamilynewsController {

    @GetMapping("/familynews")
    public String familynewsForm(Model model) {
        model.addAttribute("familynews", new Familynews());
        return "familynews";
    }

    @PostMapping("/familynews")
    public String familynewsSubmit(@ModelAttribute Familynews familynews) {
        return "result";
    }

}
