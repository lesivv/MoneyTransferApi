import answer.Answer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

public class MoneyTransferDriver {
    private final static String URL = "http://localhost:4567/moneyTransfer";
    private final String CONTENT_TYPE = "application/json";

    public Answer makeMoneyTransferRequest(String payload) throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.start();
        Request request = httpClient.POST(URL);
        request.method(HttpMethod.POST);
        request.header(HttpHeader.ACCEPT, CONTENT_TYPE);
        request.header(HttpHeader.CONTENT_TYPE, CONTENT_TYPE);
        request.content(new StringContentProvider(payload), CONTENT_TYPE);
        ContentResponse response = request.send();

        return new ObjectMapper().readValue(response.getContentAsString(), Answer.class);
    }
}
