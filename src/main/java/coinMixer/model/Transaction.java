/**
 * 
 */
package coinMixer.model;

import java.util.Date;

/**
 * @author ksarofem
 *
 */
public class Transaction {
	
	private Date timestamp;
	private String toAddress;
	private String fromAddress;
	private String amount;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	           	    
}
