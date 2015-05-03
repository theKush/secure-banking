package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class BalanceController {
	
	@RequestMapping(value = "/balance", method= RequestMethod.GET)
	public String balance(Model model){
		model.addAttribute("checkingBalance", User.checkingBalance);
		model.addAttribute("checkingAccount", User.checkingAccount);
		model.addAttribute("savingBalance", User.savingBalance);
		model.addAttribute("savingAccount", User.savingAccount);
		return "balance";
	}
	
}
