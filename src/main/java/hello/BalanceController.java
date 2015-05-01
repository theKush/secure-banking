package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class BalanceController {
	
	@RequestMapping(value = "/balance", method= RequestMethod.GET)
	public String transferForm(Model model){
		model.addAttribute("balance", "1200.0");
		model.addAttribute("accountNumber", "1234567890");
		return "balance";
	}
	
}
