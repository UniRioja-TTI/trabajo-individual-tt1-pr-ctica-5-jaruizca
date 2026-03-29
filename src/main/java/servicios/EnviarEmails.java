package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.EmailApi;
import org.openapitools.client.model.EmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EnviarEmails implements InterfazEnviarEmails {
    private final EmailApi emailApi;
    private final ApiClient apiClient;

    public EnviarEmails() {
        this.apiClient = new ApiClient();
        String host = System.getenv("SERVICIO_CONSUMIBLE_HOST");
        String port = System.getenv("SERVICIO_CONSUMIBLE_PORT");
        host = host != null ? host : "localhost";
        port = port != null ? port : "8888";
        apiClient.setBasePath("http://" + host + ":" + port);
        this.emailApi = new EmailApi(apiClient);
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        EmailResponse response = emailApi.emailPost(email, dest.getDireccion()).block();
        if (response == null) return false;
        return response.getDone() != null ? response.getDone() : false;
    }
}
