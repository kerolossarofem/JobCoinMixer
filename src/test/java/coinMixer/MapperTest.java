package coinMixer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coinMixer.model.AddressBalance;

public class MapperTest {

	@Test
	public void testMapperObject() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String in = 
				"{\"balance\": \"20.25\",\"transactions\": [{\"timestamp\": \"2014-04-22T13:10:01.210Z\",\"toAddress\": \"BobsAddress\", \"amount\": \"50.35\"},{\"timestamp\": \"2014-04-23T18:25:43.511Z\",\"fromAddress\": \"BobsAddress\",\"toAddress\": \"AlicesAddress\",\"amount\": \"30.1\"}]}";
		AddressBalance addressBalance =objectMapper.readValue(in, AddressBalance.class);
		assertEquals("20.25", addressBalance.getBalance());
		assertEquals(2, addressBalance.getTransactions().size());
	}

	@Test
	public void testMapperObjectWithZeroBalance() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String in = 
				"{\"balance\": \"0\",\"transactions\": []}";
		AddressBalance addressBalance =objectMapper.readValue(in, AddressBalance.class);
		assertEquals("0", addressBalance.getBalance());
		assertEquals(0, addressBalance.getTransactions().size());
	}
}
