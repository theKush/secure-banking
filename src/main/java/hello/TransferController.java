package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class TransferController {
	
	@RequestMapping(value = "/transfer", method= RequestMethod.POST)
	public String transferSubmit(@ModelAttribute Transfer transfer, Model model){
		
		model.addAttribute("message", "Transfer Successfull");
		
		return "hello";
	}
	
	@RequestMapping(value = "/transfer", method= RequestMethod.GET)
	public String transferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		return "transfer";
	}
	
}
