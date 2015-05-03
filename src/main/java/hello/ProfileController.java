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
		User.firstName = profile.getFirstName();
		User.lastName = profile.getLastName();
		User.email = profile.getEmail();
		User.phoneNumber = profile.getPhoneNumber();
		model.addAttribute("message", "Profile Change Successfull");
		
	
		return "hello";
	}
	
	@RequestMapping(value = "/profile", method= RequestMethod.GET)
	public String profileForm(Model model){
		Profile profile = new Profile();
	
		profile.setFirstName(User.firstName);
		profile.setLastName(User.lastName);
		profile.setEmail(User.email);
		profile.setPhoneNumber(User.phoneNumber);
		
		model.addAttribute("profile", profile);
		return "profile";
	}
	
}
