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
		
		if (((transfer.getFromAccount() != User.checkingAccount) && (transfer.getFromAccount() != User.savingAccount )) || ((transfer.getToAccount() != User.checkingAccount) && (transfer.getToAccount() != User.savingAccount ))) {
			model.addAttribute("error", "Account Numbers not correct");
			return "transfer";
		}
		else if (transfer.getFromAccount() == transfer.getToAccount()) {
			model.addAttribute("error", "Cannot transfer to same account");
			return "transfer";
		}
		else if (transfer.getAmount() <= 0){
			model.addAttribute("error", "Amount should be greater than $0");
			return "transfer";
		}
		
		if (transfer.getFromAccount() == User.checkingAccount) {
			if (transfer.getAmount() > User.checkingBalance) {
				model.addAttribute("error", "Not enough balance in your account");
				return "transfer";
			}
		} 
		else if (transfer.getFromAccount() == User.savingAccount) {
			if (transfer.getAmount() > User.savingBalance) {
				model.addAttribute("error", "Not enough balance in your account");
				return "transfer";
			}
		} 
		
		if (transfer.getFromAccount() == User.checkingAccount) {
			if (transfer.getToAccount() == User.savingAccount) {
				if (transfer.getAmount() > 0) {
					User.savingBalance = User.savingBalance + transfer.getAmount();
					User.checkingBalance = User.checkingBalance - transfer.getAmount();
				}
			}
		}
		
		else if (transfer.getFromAccount() == User.savingAccount) {
			if (transfer.getToAccount() == User.checkingAccount) {
				if (transfer.getAmount() > 0) {
					User.savingBalance = User.savingBalance - transfer.getAmount();
					User.checkingBalance = User.checkingBalance + transfer.getAmount();
				}
			}
		}
		
		model.addAttribute("message", "Transfer Successfull");
		
		return "hello";
	}
	
	@RequestMapping(value = "/transfer", method= RequestMethod.GET)
	public String transferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		return "transfer";
	}
	
}
