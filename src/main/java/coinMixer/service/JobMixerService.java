/**
 * 
 */
package coinMixer.service;

/**
 * @author ksarofem
 *
 */
public interface JobMixerService {
	String getDeposit(String depositAddress) throws Exception;

	void sendCoin(String from, String to, String balance) throws Exception;
}
