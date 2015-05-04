package hello;

import java.io.InputStream;
import java.net.URLDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ProfileController {
	
	@RequestMapping(value = "/profile", method= RequestMethod.POST)
	public String transferSubmit(@ModelAttribute Profile profile, Model model, HttpServletRequest request, HttpServletResponse response){
		
		try{
			InputStream i = request.getInputStream();
            
			String formData = Confidentiality.Decrypt(i);
            String[] parts = formData.split("&");
            
            String firstName = InputStreamToString.getFormValue(parts[0]);
            String lastName = InputStreamToString.getFormValue(parts[1]);
            String email = URLDecoder.decode(InputStreamToString.getFormValue(parts[2]), "UTF-8");
            String phoneNumber = InputStreamToString.getFormValue(parts[3]);
           
            User.firstName = firstName;
    		User.lastName = lastName;
    		User.email = email;
    		User.phoneNumber = Long.parseLong(phoneNumber);
            
		 }catch (Exception e){
		    e.printStackTrace();
		}
		
		model.addAttribute("message", "Profile Change Successfull");
		
		return "hello";
	}
		
	@RequestMapping(value = "/profile", method= RequestMethod.GET)
	public String profileForm(Model model, HttpServletRequest request, HttpServletResponse response){
		Profile profile = new Profile();
	
		response.setHeader("passphrase", Confidentiality.passphrase);
		response.setHeader("salt", Confidentiality.salt);
		response.setHeader("iv", Confidentiality.iv);
		response.setHeader("size", Integer.toString(Confidentiality.size));
		response.setHeader("iteration", Integer.toString(Confidentiality.iteration));
		
		profile.setFirstName(User.firstName);
		profile.setLastName(User.lastName);
		profile.setEmail(User.email);
		profile.setPhoneNumber(User.phoneNumber);
		
		model.addAttribute("profile", profile);
		return "profile";
	}
		
}
