package coinMixer.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.fasterxml.jackson.databind.ObjectMapper;

import coinMixer.model.AddressBalance;

public class JobMixerServiceImpl implements JobMixerService {

	private static final String GET_DEPOSIT_URL = "http://jobcoin.gemini.com/negative/api/addresses/";
	private static final String POST_VALUE_URL = "http://jobcoin.gemini.com/negative/api/transactions/";

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
	public void postValue(String from, String to, String balance) throws Exception {
		URL post_url = new URL(POST_VALUE_URL);
		URLConnection con = post_url.openConnection();
		HttpURLConnection http = (HttpURLConnection) con;
		http.setRequestMethod("POST"); // PUT is another valid option
		http.setDoOutput(true);

		Map<String, String> arguments = new HashMap<>();
		arguments.put("fromAddress", from);// deposit add
		arguments.put("toAddress", to); // house account
		arguments.put("amount", balance);// amount
		StringJoiner sj = new StringJoiner("&");
		for (Map.Entry<String, String> entry : arguments.entrySet())
			sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		http.connect();

		try (OutputStream os = http.getOutputStream()) {
			os.write(out);
		}
		
	}

	
	
}
