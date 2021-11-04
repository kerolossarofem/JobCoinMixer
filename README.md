# JobCoinMixer 
Coin Mixer Challenge
Despite some media reports, Bitcoin is not an anonymous protocol. Instead, it's often referred to as a pseudonymous system. All transactions to or from any Bitcoin address are publicly available, so Bitcoin's “anonymity” hinges on not knowing which addresses belong to which people. But because Bitcoin addresses are so trivial to create (it’s essentially just a key pair), you can help ensure your anonymity by using a bunch of addresses instead of just one.

*For simplicity the application is in single thread as of now, however I added step 3-5 to a different thread to demonstrate the use of multi thread. we can control the thread by locking database or/and thread safe collections*

### Steps:
- User provides a list of unused addresses. Application won't finish this step unless the use enter `quit` in the system
``` 
while (true) {
			String address = scanner.next();
			if (address.equalsIgnoreCase("Quit")) {
				break;
			}
			addresses.add(address);
		}
```
- Mixer will provide user a new address.
```
UUID.randomUUID().toString()

```
- User will use the address that mixer provided to transafer coin.

- Mixer will keep ping the API every 5 sec to check on the transafer.
```
do {
			TimeUnit.SECONDS.sleep(5);
			balance = jobMixerService.getDeposit(depositAddress);
		} while (balance.equals("0"));
```    

- Mixer will transafer the coin from deposited address to the house-hold account.
```
	jobMixerService.sendCoin(from, to, balance);
  
```

- Mixer will deduct the fee and randamly divided the coint to small pieces and transfer them to the addresses that user provided in first step.

```
for (int i = 0; i < addresses.size() - 1; i++) {
			int randomNum = rand.nextInt((max - min) + 1) + min;
			BigDecimal balance1 = currBalance.divide(new BigDecimal(randomNum), MathContext.DECIMAL128);
			currBalance = currBalance.subtract(balance1);
			TimeUnit.SECONDS.sleep(5);
			sendCoin(house_account, addresses.get(i), String.valueOf(balance1));
		}
		sendCoin(house_account, addresses.get(addresses.size() - 1), String.valueOf(currBalance));
    
```



