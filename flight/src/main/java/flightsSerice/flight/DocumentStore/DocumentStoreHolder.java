package flightsSerice.flight.DocumentStore;


import net.ravendb.client.documents.DocumentStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentStoreHolder {
    private static DocumentStore documentStore;
    @Bean
    private static DocumentStore createDocumentStore() {
        String serverURL = "http://localhost:8080";
        String databaseName = "flightsDB";

        DocumentStore documentStore = new DocumentStore(new String[] { serverURL }, databaseName);

        documentStore.initialize();
        return documentStore;
    }}

