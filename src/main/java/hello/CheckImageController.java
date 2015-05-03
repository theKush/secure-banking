package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CheckImageController {
	
	@RequestMapping(value = "/checkImage", method= RequestMethod.GET)
	public String checkImage(Model model){
		return "checkImage";
	}
	
}