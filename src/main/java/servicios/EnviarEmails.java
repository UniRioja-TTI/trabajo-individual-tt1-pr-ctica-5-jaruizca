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
        apiClient.setBasePath("http://localhost:8080");
        this.emailApi = new EmailApi(apiClient);
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        EmailResponse response = emailApi.emailPost(dest.getDireccion(), email).block();
        if (response == null) return false;
        return response.getDone() != null ? response.getDone() : false;
    }
}
