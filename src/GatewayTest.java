
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class GatewayTest extends TestCase {

    private Gateway g;

    @Before
    protected void setUp() {
        g = new Gateway(null, "pass");
    }

    private boolean paramSignatureStartsWith(List<NameValuePair> params, String prefix) {
        return g.sign(params).getValue().startsWith(prefix);
    }

    @Test
    public void testSign() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "two"));
        System.out.println();
        assertTrue(paramSignatureStartsWith(params, "86cdc"));

        params.clear();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "New lines! %0D %0D%0A"));
        assertTrue(paramSignatureStartsWith(params, "cf50d"));

        params.clear();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "strange \"'?& symbols "));
        assertTrue(paramSignatureStartsWith(params, "7c952"));

        params.clear();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "a £ sign"));
        assertTrue(paramSignatureStartsWith(params, "13637"));

        params.clear();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "aa ** stars"));
        assertTrue(paramSignatureStartsWith(params, "47a2b1"));

        params.clear();
        params.add(new BasicNameValuePair("a", "one"));
        params.add(new BasicNameValuePair("b", "newline \n characater"));
        assertTrue(paramSignatureStartsWith(params, "19582"));

        params.clear();
        params.add(new BasicNameValuePair("a[aa]", "12"));
        params.add(new BasicNameValuePair("a[bb]", "13"));
        params.add(new BasicNameValuePair("a1", "0"));
        params.add(new BasicNameValuePair("aa", "1"));
        params.add(new BasicNameValuePair("aZ", "2"));
        assertTrue(paramSignatureStartsWith(params, "4aeaa"));


    }

    // Test that we can actually carry out a 
    @Test
    public void testDirectRequest() {
        List<NameValuePair> params = GatewayUtils.getInitialForm();
        params.addAll(GatewayUtils.getDebuggingBrowserData());

        var gTest = new Gateway("https://test.3ds-pit.com/direct/", "Threeds2Test60System");

        try {
            // Ensure this completes without exception is a test in itself. 
            // If the server response doesn't pass a signature verification,
            // it will throw. 
            var gatewayResponse = gTest.directRequest(params);

            // We're sending a valid transaction request which expects a
            // '3DS Authentication Required' response, which is 65802
            assertEquals("65802", gatewayResponse.get("responseCode"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

}

class GatewayUtils {

    protected static List<NameValuePair> getInitialForm() {
        var uniqid = RandomStringUtils.random(15, true, true);

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

        // The following fields are mandatory for 3DS v2
        params.add(new BasicNameValuePair("remoteAddress", "10.10.10.10"));
        params.add(new BasicNameValuePair("merchantCategoryCode", "5411"));
        params.add(new BasicNameValuePair("threeDSVersion", "2"));
        params.add(new BasicNameValuePair("threeDSRedirectURL", "https://example.net/returnUrl?acs=1")); // PLACEHOLDER

        return params;
    }

    protected static List<NameValuePair> getDebuggingBrowserData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("merchantID", "100856"));
        params.add(new BasicNameValuePair("deviceChannel", "browser"));
        params.add(new BasicNameValuePair("deviceIdentity",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:81.0) Gecko/20100101 Firefox/81.0"));
        params.add(new BasicNameValuePair("deviceTimeZone", "-60"));
        params.add(new BasicNameValuePair("deviceCapabilities", "javascript"));
        params.add(new BasicNameValuePair("deviceScreenResolution", "1920x1080x24"));
        params.add(new BasicNameValuePair("deviceAcceptContent",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
        params.add(new BasicNameValuePair("deviceAcceptEncoding", "gzip, deflate"));
        params.add(new BasicNameValuePair("deviceAcceptLanguage", "en-GB"));

        return params;
    }

}