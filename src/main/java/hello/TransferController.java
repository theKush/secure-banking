package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;



@Controller
public class TransferController {
	
	@RequestMapping(value = "/transfer", method= RequestMethod.POST)
	public String transferSubmit(@ModelAttribute Transfer transfer, Model model, HttpServletRequest request){
		
		try{
			
			//----------------------------- CONFIDENTIALITY CHECK FOR TRANSFER FUNDS -----------------------------------

			InputStream i = request.getInputStream();
            
			String formData = Confidentiality.Decrypt(i);
            String[] parts = formData.split("&");
          //---------------------------------- END OF CONFIDENTIALITY CHECK  ----------------------------------------

            String fromAccount = InputStreamToString.getFormValue(parts[0]);
            String toAccount = InputStreamToString.getFormValue(parts[1]);
            String amount = InputStreamToString.getFormValue(parts[2]);
            
          //----------------------------- INTEGRITY CHECK FOR TRANSFER FUNDS -----------------------------------

            String calHashFromAccount = Integrity.getMD5(fromAccount);
			String calHashToAccount = Integrity.getMD5(toAccount);
			String calHashAmount = Integrity.getMD5(amount);
            
			String hashAmount = request.getHeader("hashAmount");
			String hashFromAccount = request.getHeader("hashFromAccount");
			String hashToAccount = request.getHeader("hashToAccount");
			

			System.out.println( "\n Amount FrontEnd: " +hashAmount);
			System.out.println( " Amount BackEnd : " +calHashAmount);

			System.out.println( "\n To Account FrontEnd: " +hashToAccount);
			System.out.println( " To Account BackEnd : " +calHashToAccount);

			System.out.println( "\n From Account FrontEnd: " +hashFromAccount);
			System.out.println( " From Account BackEnd : " +calHashFromAccount + "\n");
			
			Boolean fromAccountCheck = Integrity.check(hashFromAccount, calHashFromAccount);
			Boolean toAccountCheck = Integrity.check(hashToAccount, calHashToAccount);
			Boolean amountCheck = Integrity.check(hashAmount, calHashAmount);
			
			if (fromAccountCheck && toAccountCheck && amountCheck) {
				
				transfer.setFromAccount(Long.parseLong(fromAccount));
				transfer.setToAccount(Long.parseLong(toAccount));
				transfer.setAmount(Integer.parseInt(amount));
				
				System.out.println("\n After Amount:     " + transfer.getAmount());
				System.out.println(" From Acount: " + transfer.getFromAccount());
				System.out.println(" To Acount:   " + transfer.getToAccount());	

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
			}

			
          //---------------------------------- END OF INTEGRITY CHECK  ----------------------------------------

						
		}catch (Exception e){
		    e.printStackTrace();
		}
		
		model.addAttribute("message", "Transfer Successfull");
		
		return "hello";
	}
	
	@RequestMapping(value = "/transfer", method= RequestMethod.GET)
	public String transferForm(HttpServletResponse response, Model model){
		
		response.setHeader("passphrase", Confidentiality.passphrase);
		response.setHeader("salt", Confidentiality.salt);
		response.setHeader("iv", Confidentiality.iv);
		response.setHeader("size", Integer.toString(Confidentiality.size));
		response.setHeader("iteration", Integer.toString(Confidentiality.iteration));
		
		model.addAttribute("transfer", new Transfer());
		return "transfer";
	}
		
}
