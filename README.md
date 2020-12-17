# README

# Contents
- Introduction
- Prerequisites
- Using the Gateway SDK
- License

# Introduction
This Java SDK provides an easy method to integrate with the payment gateway.
 - The Gateway.java file contains the main body of the SDK.

# Prerequisites
- The SDK requires the following prerequisites to be met in order to function correctly:
    - SSL <span style="color: red">**NB: HTTPS is expected to be in place as the payment gateway will respond over SSL when redirecting the user's browser. Failure to provide an environment where HTTPS traffic is possible, will result in the SDK failing**</span>
    - For convenience the following Java libraries have been included for easier set up:
	- commons-cli-1,4
	- commons-codec-1.13
	- commons-io-2.8.0
	- commons-lang3-3.11
	- commons-text-1.9
	- hamcrest-2.2
	- httpclient5-5.0.2
	- httpclient5-cache-5.0.2
	- httpclient5-fluent-5.0.2
	- httpclient5-testing-5.0.2
	- httpclient5-win-5.0.2
	- httpcore5-5.0.2
	- httpcore5-h2-5.0.2
	- httpcore5-reactive-5.0.2
	- httpcore5-testing-5.0.2
	- jna-5.2.0
	- jna-platform-5.2.0
	- junit-4.13
	- reactive-streams-1.0.2
	- rxjava-2.2.8
	- slf4j-api-1.7.25

> <span style="color: red">Please note that we can only offer support for the SDK itself. While every effort has been made to ensure the sample code is complete and bug free, it is only a guide and should not be used in a production environment.</span>

# Using the Gateway SDK

Require the gateway SDK into your project

```
    private Gateway gateway;
```

Instantiate the Gateway object

```
var gateway = new Gateway("https://test.3ds-pit.com/direct/", "Threeds2Test60System")
```

Once your SDK has been required. You create your request array, for example:
```
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("merchantID", "100856"));
        params.add(new BasicNameValuePair("action", "SALE"));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(new BasicNameValuePair("transactionUnique", uniqid));
        params.add(new BasicNameValuePair("countryCode", "826"));
        params.add(new BasicNameValuePair("currencyCode", "826"));
        params.add(new BasicNameValuePair("amount", "1001"));
        params.add(new BasicNameValuePair("cardNumber", "4012001037141112"));
        params.add(new BasicNameValuePair("cardExpiryMonth", "12"));
        params.add(new BasicNameValuePair("cardExpiryYear", "20"));
        params.add(new BasicNameValuePair("cardCVV", "083"));
        params.add(new BasicNameValuePair("customerName", "Test Customer"));
        params.add(new BasicNameValuePair("customerEmail", "test@testcustomer.com"));
        params.add(new BasicNameValuePair("customerAddress", "16 Test Street"));
        params.add(new BasicNameValuePair("customerPostCode", "TE15 5ST"));
        params.add(new BasicNameValuePair("orderRef", "Test purchase"));

        // The following fields are mandatory for direct 3DS v2
        params.add(new BasicNameValuePair("threeDSVersion", "2"));
        params.add(new BasicNameValuePair("threeDSRedirectURL", "https://example.net/returnUrl?acs=1")); // PLACEHOLDER

```
> NB: This is a sample request. The gateway features many more options. Please see our integration guides for more details.

Then, depending on your integration method, you'd either call:

```
var gatewayResponse = gateway.directRequest(params);
```

OR

```
var gatewayResponse = gateway.hostedRequest(params);
```

And then handle the response received from the gateway.

License
----
MIT
