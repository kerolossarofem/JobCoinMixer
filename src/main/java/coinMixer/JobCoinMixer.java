package coinMixer;

import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.TimeUnit;

import coinMixer.service.JobMixerService;
import coinMixer.service.JobMixerServiceImpl;

public class JobCoinMixer {
	public static final String url = "http://jobcoin.gemini.com/negative/api/";
	public static final String house_account = "House";
	public static final BigDecimal FEE = BigDecimal.valueOf(0.015);
	private static JobMixerService jobMixerService = new JobMixerServiceImpl();

	public static void main(String args[]) throws Exception {
		// Step 1: System will ask user for 3 unused addresses
		List<String> addresses = userAddresses();

		// Step 2: System chooses a deposit address
		String depositAddress = chooseDepositAddress();// generate address
		System.out.println("Please deposit your jobcoins to the following address: " + depositAddress);
		System.out.println("A 1.5% fee will be taken out");

		// Step 3: then checks to see if deposit has occurred
		String balance = getDepositInfo(depositAddress);
		System.out.println("Your deposit was a success!");

		// Step 4: deposit is moved to house_account after 3-10 seconds
		postValue(depositAddress, house_account, balance);

		// Step 5: Different incrememnts of the balance is moved from the house account
		// to three different adddresses provided by the user
		distributeFunds(balance, addresses);

	}

	private static List<String> userAddresses() {
		ArrayList<String> addresses = new ArrayList<>();
		System.out.println("Please provide unused addresses- When you done write Quit: ");
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String address = scanner.next();
			if (address.equalsIgnoreCase("Quit")) {
				break;
			}
			addresses.add(address);
		}
		return addresses;
	}

//chooseDepositAddress() chooses a random address from a list of possible deposit addresses
	private static String chooseDepositAddress() {
		// randamly generated
		return UUID.randomUUID().toString();
	}

//getDepositInfo() makes a get request and checks the deposit account every 8 seconds to see if deposit has been made
	public static String getDepositInfo(String depositAddress) throws Exception {
		String balance = "0";
		do {
			TimeUnit.SECONDS.sleep(5);
			balance = jobMixerService.getDeposit(depositAddress);
		} while (balance.equals("0"));
		return jobMixerService.getDeposit(depositAddress);
	}

//distributeFunds() takes fees and breaks the deposit into 3 different deposits and sends the parameters to postValue()
	public static void distributeFunds(String balance, List<String> addresses) throws Exception {
		// double currBalance = (Integer.parseInt(balance)) * 1.0;
		BigDecimal currBalance = new BigDecimal(balance);
		currBalance = currBalance.subtract(currBalance.multiply(FEE));
		Random rand = new Random();

		// nextInt as provided by Random is exclusive of the top value so you need to
		// add 1
		int min = addresses.size();
		int max = min*3;
		for (int i = 0; i < addresses.size() - 1; i++) {
			int randomNum = rand.nextInt((max - min) + 1) + min;
			BigDecimal balance1 = currBalance.divide(new BigDecimal(randomNum),  MathContext.DECIMAL128);
			currBalance = currBalance.subtract(balance1);
			TimeUnit.SECONDS.sleep(5);
			postValue(house_account, addresses.get(i), String.valueOf(balance1));
		}
		postValue(house_account, addresses.get(addresses.size() - 1), String.valueOf(currBalance));
	}

//postValue takes in the 3 values needed for a transaction, and completes the transaction
	public static void postValue(String from, String to, String balance) throws Exception {
		jobMixerService.postValue(from, to, balance);
	}

}
