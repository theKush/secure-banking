package hello;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/hello", method= RequestMethod.GET)
	public String hello(Model model){
		// use the flag so that it doesnt try to get the user information every time this page is loaded
		if (User.flag == 0) {
			JSONParser parser = new JSONParser();
	    	try {
	    		
	    		Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "/src/main/java/users.txt"));
	    		
	    		
	    		JSONObject jsonObject = (JSONObject) obj;
	    		
	    		
	    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    		String loggedinUser = authentication.getName();
	    		JSONObject user1 = (JSONObject) jsonObject.get(loggedinUser);
	    
	    		
	       		User.firstName = (String) user1.get("firstName");
	    		User.lastName = (String) user1.get("lastName");
	    		User.email = (String) user1.get("email");
	    		User.phoneNumber = (long) user1.get("phoneNumber");
	    		User.checkingAccount = (long) user1.get("checkingAccount");
	    		User.checkingBalance = (long) user1.get("checkingBalance");
	    		User.savingAccount = (long) user1.get("savingAccount");
	    		User.savingBalance = (long) user1.get("savingBalance");
	    		
	    		User.flag = 1;

	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
		
		return "hello";
	}
	
	
}
