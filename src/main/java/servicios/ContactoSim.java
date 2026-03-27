package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ResultadosApi;
import org.openapitools.client.api.SolicitudApi;
import org.openapitools.client.model.ResultsResponse;
import org.openapitools.client.model.Solicitud;
import org.openapitools.client.model.SolicitudResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactoSim implements InterfazContactoSim {
    private final SolicitudApi solicitudApi;
    private final ApiClient apiClient;
    private final ResultadosApi resultadosApi;
    private final String USER = "user";
    private final Map<Integer,Entidad> mapEntidades;

    public ContactoSim() {
        apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8080");
        solicitudApi = new SolicitudApi(apiClient);
        resultadosApi = new ResultadosApi(apiClient);

        mapEntidades = Stream.of("a","b","c","d","f")
            .map(n -> {
                Entidad e = new Entidad();
                e.setName(n);
                e.setId(n.charAt(0));
                return e;
            })
            .collect(Collectors.toMap(
                    Entidad::getId,  // clave
                    e -> e   // valor
            ));
    }

    @Override
    public int solicitarSimulation(DatosSolicitud datosSolicitud) {
        List<Integer> cantidadesIniciales = new ArrayList<>();
        List<String> nombreEntidades = new ArrayList<>();
        datosSolicitud.getNums().forEach((key,value)->{
            nombreEntidades.add(mapEntidades.get(key).getName());
            cantidadesIniciales.add(value);
        });
        Solicitud solicitud =  new Solicitud();
        solicitud.setCantidadesIniciales(cantidadesIniciales);
        solicitud.setNombreEntidades(nombreEntidades);
        SolicitudResponse response = solicitudApi.solicitudSolicitarPost(USER, solicitud).block();
        if (response == null) return -1;
        return response.getTokenSolicitud() != null ? response.getTokenSolicitud() : -1 ;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        ResultsResponse response = resultadosApi.resultadosPost(USER, ticket).block();
        if (response == null) return null;
        return parseData(response.getData());
    }

    @Override
    public List<Entidad> getEntities() {
        return this.mapEntidades.values().stream().toList();
    }

    @Override
    public boolean isValidEntityId(int id) {
        return this.mapEntidades.containsKey(id);
    }

    private DatosSimulation parseData(String dataString) {
        DatosSimulation datosSimulation = new DatosSimulation();
        try (BufferedReader reader = new BufferedReader(new StringReader(dataString))) {
            // Leemos el ancho inicial
            datosSimulation.setAnchoTablero(Integer.parseInt(reader.readLine()));

            /*
            FORMATO:
            t,y,x,color
            t -> tiempo
            y -> coordenada y
            x -> coordenada x
            color -> color con el que se debe pintar
             */

            String linea = null;
            Punto punto = null;
            Map<Integer, List<Punto>> puntos = new HashMap<>();
            while((linea = reader.readLine()) != null) {
                String[] grupos = linea.split(",");

                Integer t = Integer.parseInt(grupos[0]);
                // Si no se ha registrado el tiempo, se añade una nueva entrada
                if (!puntos.containsKey(t)) {
                    puntos.put(t, new ArrayList<>());
                }

                punto = new Punto();
                punto.setX(Integer.parseInt(grupos[1]));
                punto.setY(Integer.parseInt(grupos[2]));
                punto.setColor(grupos[3]);

                puntos.get(t).add(punto);
            }

            datosSimulation.setPuntos(puntos);
            datosSimulation.setMaxSegundos(puntos.keySet().stream().reduce((n,m)->{return n >= m ? n : m;}).get());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return datosSimulation;
    }
}