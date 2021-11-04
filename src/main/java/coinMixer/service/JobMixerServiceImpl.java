package coinMixer.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import coinMixer.model.AddressBalance;

public class JobMixerServiceImpl implements JobMixerService {

	private static final String GET_DEPOSIT_URL = "http://jobcoin.gemini.com/herbs-vagrancy/api/addresses/";
	private static final String POST_VALUE_URL = "http://jobcoin.gemini.com/herbs-vagrancy/api/transactions";

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String getDeposit(String depositAddress) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(GET_DEPOSIT_URL);
		stringBuilder.append(URLEncoder.encode(depositAddress, "UTF-8"));
		URL obj = new URL(stringBuilder.toString());

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept-Charset", "UTF-8");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		AddressBalance addrBalance = objectMapper.readValue(in, AddressBalance.class);
		in.close();
		return addrBalance.getBalance();
	}

	@Override
	public void sendCoin(String from, String to, String amount) throws Exception {
		HashMap<String, String> values = new HashMap<>();

		values.put("fromAddress", from);
		values.put("toAddress", to);
		values.put("amount", amount);

		String requestBody = objectMapper.writeValueAsString(values);

		System.out.println(requestBody);
		URL obj = new URL(POST_VALUE_URL);
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-Type", "application/json");

		postConnection.setDoOutput(true);
		OutputStream os = postConnection.getOutputStream();
		os.write(requestBody.getBytes());
		os.flush();
		os.close();
		int responseCode = postConnection.getResponseCode();

		if (responseCode == 422) {
			System.out
					.println("ERROR: Insufficient Funds from[" + from + "], to[" + to + "] with amout[" + amount + "]");
		} else if (responseCode != HttpURLConnection.HTTP_OK) {
			System.out.println("ERROR: in send coin from[" + from + "], to[" + to + "] with amout[" + amount + "]");
		} else {
			System.out.println("coin has been sent!");
		}

	}

}
