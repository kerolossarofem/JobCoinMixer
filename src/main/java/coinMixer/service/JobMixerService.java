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
	void postValue(String from, String to, String balance) throws Exception;
}
