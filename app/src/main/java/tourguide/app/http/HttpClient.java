package tourguide.app.http;

import org.springframework.web.reactive.function.client.WebClient;

public class HttpClient {

    private static final WebClient webClient = WebClient.create();

    public static <T> T post(Class<T> clazz, String url, Object body) {
        return webClient.post().uri(url).bodyValue(body).retrieve().bodyToMono(clazz).block();
    }
    public static <T> T get(Class<T> clazz, String url) {
        return webClient.get().uri(url).retrieve().bodyToMono(clazz).block();
    }
}
