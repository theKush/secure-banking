package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@Controller
public class TransferController {
	
	@RequestMapping(value = "/transfer", method= RequestMethod.POST)
	public String transferSubmit(@ModelAttribute Transfer transfer, Model model, HttpServletRequest request){
		
		String[] tokens = new String[3];

		if (request != null)
		{
			try
			{
				String tempInput = getStringFromInputStream(request.getInputStream());
				tokens = tempInput.split("&");
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
		}

		String calHashFromAccount = "0";
		String calHashToAccount = "0";
		String calHashAmount = "0";

		if (tokens != null)
		{
			transfer.setFromAccount(Long.parseLong(tokens[0].replaceAll(".*=", "")));
			transfer.setToAccount(Long.parseLong(tokens[1].replaceAll(".*=", "")));
			transfer.setAmount(Integer.parseInt(tokens[2].replaceAll(".*=", "")));

			System.out.println("\n Amount:     " + transfer.getAmount());
			System.out.println(" From Acount: " + transfer.getFromAccount());
			System.out.println(" To Acount:   " + transfer.getToAccount());		

			calHashFromAccount = MD5.getMD5(tokens[0].replaceAll(".*=", ""));
			calHashToAccount = MD5.getMD5(tokens[1].replaceAll(".*=", ""));
			calHashAmount = MD5.getMD5(tokens[2].replaceAll(".*=", ""));
		}

		String hashAmount = request.getHeader("hashAmount");
		String hashFromAccount = request.getHeader("hashFromAccount");
		String hashToAccount = request.getHeader("hashToAccount");
		
		System.out.println( "\n Amount FrontEnd: " +hashAmount);
		System.out.println( " Amount BackEnd : " +calHashAmount);

		System.out.println( "\n Amount FrontEnd: " +hashToAccount);
		System.out.println( " Amount BackEnd : " +calHashToAccount);

		System.out.println( "\n Amount FrontEnd: " +hashFromAccount);
		System.out.println( " Amount BackEnd : " +calHashFromAccount + "\n");

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
//----------------------------- SECURITY CHECK FOR TRANSFER FUNDS -----------------------------------
		else if ((hashAmount != calHashAmount) ||
			(hashFromAccount != calHashFromAccount) ||
			(hashToAccount != calHashToAccount))
		{
			model.addAttribute("error", "An Internal Error Occurred");
				return "transfer";
		}
//---------------------------------- END OF SECURITY CHECK  ----------------------------------------

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
	
		
	private static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		 
		String line;
		try {
		 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		 
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			if (br != null) {
				try {
						br.close();
					} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();	 
	}

}
