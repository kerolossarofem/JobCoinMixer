/**
 * 
 */
package coinMixer.model;

import java.util.List;

/**
 * @author ksarofem
 *
 */
public class AddressBalance {
	
	private String balance;
	private List<Transaction> transactions;
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	} 
	  
}
