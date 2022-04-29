import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/*
4 main steps:
1) build the HTTP call object
2) set HTTP 'body' as the values of the audio (Liviness, tempo, valence, etc...)
3) send the request
4) receives the request in JSON and parses it to a HashMap list. the return contains
the probability of each class, and the main class possibility

Funciona em 4 passos principais:

1) Constroi o objeto da chamada HTTP a ser enviado ao servico web do modelo
2) Coloca como o `body` da chamada HTTP as caracteristicas de audio que sao classificados
3) Envia a requisicao ao servico
4) Recebe a resposta em JSON e a converte para uma List de HashMaps. O retorno do modelo
   contem as caracteristicas de cada audio enviadas, a probabilidade de cada set de caracteristicas
   pertencer a uma classe e a classe prevista.
*/

public class Main {
    // Endpoint do modelo. Para mais informacoes, ver o seguinte
    // video no tempo ja marcado: https://youtu.be/jTUvOlWBuVw?t=188.
    // O endpoint esta presente no campo "REST Endpoint" no servico web do modelo.
    private static final String MODEL_URL = "http://e35623e2-44a2-48ed-acbc-37e9bdd71d7a.eastus2.azurecontainer.io/score";

    // Chave de API do seu servico na Azure. Para mais informacoes assistir o seguinte
    // video no tempo ja marcado: https://youtu.be/jTUvOlWBuVw?t=188
    // A chave esta presente no campo "Primary Key" no servico web do modelo.
    private static final String API_KEY = "ZzCL04bmMitv8HenuazzYQMDDBoQOJil";

    public static void main(String[] Args) throws Exception {
        // Construimos a nosso objeto HTTP que sera enviado ao servidor do modelo.
        // O `API_KEY` e utilizado nos headers e os dados enviados sao atribuidos ao objeto
        // na linha 43 por meio da funcao `.sampleData`
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MODEL_URL))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(sampleData()))
                .build();

        try {
            // Realiza-se a chamada HTTP para o servidor do modelo. O objeto `client` definido na linha 40
            // chama o metodo `#send` passando o request da linha 41, que e quem contem as informacoes da URL,
            // autenticacao com a API_KEY e os dados a serem classificados.
            HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertemos a reposta para uma List de objetos de HashMap. Nas linhas 86-111 ha um exemplo de retorno
            // da funcao `.responseMapBody`.
            List<Map<String, Object>> classification = responseMapBody(response.body());
            System.out.println(classification);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Cria caracteristicas fakes de audio a serem classificadas, neste caso ha somente um objeto
    // na lista, porem podem ser quantos forem necessarios. Mais explicadas no video https://youtu.be/jTUvOlWBuVw
    private static String sampleData() {
        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("class", "");
        item.put("valence", 3.9394);
        item.put("energy", 1);
        item.put("liveness", 1);
        item.put("speechiness", 1);
        item.put("instrumentalness", 1);
        item.put("tempo", 150);
        item.put("danceability", 8.393);
        item.put("acousticness", 0.992);

        array.add(item);

        return array.toString();
    }

    /*
    Recebe como argumento o retorno da chamada ao modelo, que e uma string, a
    converte para JSON e depois para uma lista de HashMap.

    Exemplo de retorno:
    [
      {
         "Scored Probabilities_feliz": 2.0532116841698607E-6,
         "Scored Probabilities_dormir": 9.48652830187802E-28,
         "Scored Probabilities_foco": 2.0256458418315827E-14,
         "Scored Probabilities_correr": 0.9982468202418197,
         "Scored Probabilities_gaming": 0.001751024180401978,
         "Scored Probabilities_energetico": 1.9553258735535537E-11,
         "Scored Probabilities_triste": 6.480293395209065E-16,
         "Scored Probabilities_calmo": 1.0234652002373781E-7,
         "liveness": 1.0,
         "tempo": 150.0,
         "valence": 3.9394,
         "instrumentalness": 1.0,
         "danceability: 8.393,
         "speechiness": 1.0,
         "acousticness": 0.992,
         "class": "",
         "energy" 1.0
         "Scored Labels": "correr",
      },
      {
        ...
      },
      ...
    ]

    Cada HashMap contem as caracteristicas de audio que foram enviadas para a classificacao (liveness,
    tempo, valence, instrumentalness, danceability, speechiness, acousticness, energy), a probabilidade
    delas pertencerem a cada uma das classes (Scored Probabilities_feliz, Scored Probabilities_dormir,
    Scored Probabilities_correr, Scored Probabilities_gaming, Scored Probabilities_energetico,
    Scored Probabilities_triste, Scored Probabilities_calmo) e, por fim, a classe prevista (Scored Labels),
    que nada mais e a classe com a maior probabilidade das caracteristicas pertencerem
    */
    private static List<Map<String, Object>> responseMapBody(String body) {
        Map<String, Object> hm;
        List<Map<String, Object>> res = new ArrayList<>();

        // Parseia a resposta em string para JSON
        Object obj = JSONValue.parse(body);
        JSONObject jsonObject = (JSONObject) obj;

        // O retorno do modelo vem dentro da chave `result`
        JSONArray objs = (JSONArray) jsonObject.get("result");

        // Iterar sobre os objetos do `result`, onde cada um representa o resultado da classificacao de um set
        // de caracteristicas de audio do Spotify
        for (Object _obj : objs) {
            hm = new HashMap<>();
            // Obtem o set de chaves do objeto e itera sobre eles, acessando o valor de cada um
            // e o adicionando no dicionario em Java a ser retornado
            for (Object o : ((JSONObject) _obj).keySet()) {
                String key = (String) o;
                hm.put(key, ((JSONObject) _obj).get(key));
            }
            res.add(hm);
        }

        return res;
    }
}
