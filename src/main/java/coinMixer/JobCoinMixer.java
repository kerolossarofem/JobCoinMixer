package coinMixer;

import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.TimeUnit;

import coinMixer.service.JobMixerService;
import coinMixer.service.JobMixerServiceImpl;

public class JobCoinMixer extends Thread {
	public static final String house_account = "House";
	public static final BigDecimal FEE = BigDecimal.valueOf(0.015);
	private static JobMixerService jobMixerService = new JobMixerServiceImpl();

	public static void main(String args[]) throws Exception {
		// Step 1: System will ask user to provide unused addresses
		List<String> addresses = userAddresses();

		// Step 2: System chooses a deposit address
		String depositAddress = getDepositAddr();// generate address
		System.out.println("Please deposit your coins this address: " + depositAddress);
		System.out.println("A fee of 1.5% will be deducted");
		// run step 3-5 into a different thread.
		new Thread(() -> { // Lambda Expression
			try {
				// Step 3: then checks to see if deposit has occurred
				String balance = getDepositInfo(depositAddress);
				System.out.println("Your deposit was a success!");

				// Step 4: deposit is moved to house_account
				sendCoin(depositAddress, house_account, balance);
				System.out.println("Your deposit was moved to house account!");

				// Step 5: Different increments of the balance is moved from the house account
				distribute(balance, addresses);
				System.out.println("Your deposit was distributed to your addresses!");
			} catch (Exception e) {
				System.out.println("ERROR: " + e);
			}
		}).start();
		System.out.println("main Thread");

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
	private static String getDepositAddr() {
		// randamly generated
		return UUID.randomUUID().toString();
	}

	// makes a get request and get balance!
	// if the balance still 0, thried will wait for 5 sec and try again
	public static String getDepositInfo(String depositAddress) throws Exception {
		String balance = "0";
		do {
			TimeUnit.SECONDS.sleep(5);
			balance = jobMixerService.getDeposit(depositAddress);
		} while (balance.equals("0"));
		return jobMixerService.getDeposit(depositAddress);
	}

	// takes fees and breaks the deposit into small deposits and sends the
	// parameters to sendcoin
	public static void distribute(String balance, List<String> addresses) throws Exception {
		// double currBalance = (Integer.parseInt(balance)) * 1.0;
		BigDecimal currBalance = new BigDecimal(balance);
		currBalance = currBalance.subtract(currBalance.multiply(FEE));
		Random rand = new Random();

		// nextInt as provided by Random is exclusive of the top value so you need to
		// add 1
		int min = addresses.size();
		int max = min * 3;
		for (int i = 0; i < addresses.size() - 1; i++) {
			int randomNum = rand.nextInt((max - min) + 1) + min;
			BigDecimal balance1 = currBalance.divide(new BigDecimal(randomNum), MathContext.DECIMAL128);
			currBalance = currBalance.subtract(balance1);
			TimeUnit.SECONDS.sleep(5);
			sendCoin(house_account, addresses.get(i), String.valueOf(balance1));
		}
		sendCoin(house_account, addresses.get(addresses.size() - 1), String.valueOf(currBalance));
	}

	// sendCoin send amount from an address to another address
	public static void sendCoin(String from, String to, String balance) throws Exception {
		jobMixerService.sendCoin(from, to, balance);
	}

}
