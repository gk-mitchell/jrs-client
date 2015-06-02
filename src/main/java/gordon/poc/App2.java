package gordon.poc;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gk104446 on 6/1/2015.
 */
public class App2 {

    private String hostname = "localhost";
    private Integer port = 8080;
    private String username = "jasperadmin";
    private String password = "jasperadmin";


    public static void main( String[] args ) throws Exception {
        final App2 app2 = new App2();
        app2.getHttpClient();
    }

    public void getHttpClient() throws Exception {

        final HttpHost host = new HttpHost("localhost", 8080, "http");

        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope(host.getHostName(), host.getPort()),
            new UsernamePasswordCredentials(username, password));
        //credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        final CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCredentialsProvider(credsProvider)
            .build();

        try {

            // Create AuthCache instance
            final AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            final BasicScheme basicAuth = new BasicScheme();
            authCache.put(host, basicAuth);

            // Add AuthCache to the execution context
            final HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);

            final HttpPost login = new HttpPost("http://localhost:8080/jasperserver/rest/login");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("j_username", username));
            nameValuePairs.add(new BasicNameValuePair("j_password", password));
            login.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            final String reportParameters = URLEncoder.encode("Cascading_name_single_select=A & U Stalker Telecommunications, Inc", "UTF-8");
            final HttpGet runReport = new HttpGet("http://localhost:8080/jasperserver/rest_v2/reports/reports/samples/Cascading_multi_select_report.pdf?" + reportParameters);

            System.out.println("Executing request " + runReport.getRequestLine());
            //CloseableHttpResponse response = httpclient.execute(httpget);
            try (CloseableHttpResponse response = httpclient.execute(runReport)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(response.getEntity());
            }
        } finally {
            httpclient.close();
        }
    }
}
