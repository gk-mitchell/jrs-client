package gordon.poc;

import com.jaspersoft.jasperserver.dto.resources.ClientResourceListWrapper;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.reporting.ReportOutputFormat;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;

public class App {
    public static void main( String[] args ) throws Exception {

        final RestClientConfiguration configuration = new RestClientConfiguration("http://localhost:8080/jasperserver");
        System.out.println("Created configuration...");

        final JasperserverRestClient client = new JasperserverRestClient(configuration);
        System.out.println("Created client...");

        final Session session = client.authenticate("jasperadmin", "jasperadmin");
        System.out.println("Created session...");

        final OperationResult<InputStream> result = session.reportingService()
            .report("/reports/samples/Cascading_multi_select_report")
            .prepareForRun(ReportOutputFormat.PDF, 1)
            .parameter("Cascading_name_single_select", "A & U Stalker Telecommunications, Inc")
            .run();

/*        OperationResult<ClientResourceListWrapper> result = client
            .authenticate("jasperadmin", "jasperadmin")
            .resourcesService()
            .resources()
            .search();
        ClientResourceListWrapper resourceListWrapper = result.getEntity();*/

      /*  final OperationResult<InputStream> r2 = client
            .authenticate("jasperadmin", "jasperadmin")
            .reportingService()
            .report("/reports/samples/Cascading_multi_select_report")
            .prepareForRun(ReportOutputFormat.PDF, 1)
            .parameter("Cascading_name_single_select", "A & U Stalker Telecommunications, Inc")
            .run();*/
        System.out.println("Called service ...");

        final InputStream report = result.getEntity();

        System.out.println("Output report ...");

        IOUtils.copyLarge(report, new FileOutputStream("c:\\temp\\report.pdf"));
    }
}
