package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ProfileController {
	
	@RequestMapping(value = "/profile", method= RequestMethod.POST)
	public String transferSubmit(@ModelAttribute Profile profile, Model model){
		
		model.addAttribute("message", "Profile Change Successfull");
		
		System.out.println(profile.getEmail());
		return "hello";
	}
	
	@RequestMapping(value = "/profile", method= RequestMethod.GET)
	public String profileForm(Model model){
		model.addAttribute("profile", new Profile());
		return "profile";
	}
	
}
