package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.openapitools.client.api.EmailApi;
import org.openapitools.client.model.EmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EnviarEmails implements InterfazEnviarEmails {
    private final EmailApi emailApi;

    public EnviarEmails() {
        this.emailApi = new EmailApi();
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        EmailResponse response = emailApi.emailPost(dest.getDireccion(), email).block();
        if (response == null) return false;
        return response.getDone() != null ? response.getDone() : false;
    }
}
