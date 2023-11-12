package allercheck.backend.domain.openapi.application;

import allercheck.backend.domain.openapi.presentaion.GetRecipeResponse;
import allercheck.backend.domain.openapi.exception.OpenApiConnectionFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeOpenApiService {

    @Value("${openapi.url}")
    private String url;
    @Value("${openapi.secret}")
    private String secret;
    private final RestTemplate restTemplate;

    public GetRecipeResponse getRecipeResponse(final int page, final String recipeName) {
        int startIdx = (page - 1) * 5;
        int endInx = page * 5;
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path("/{secret}/COOKRCP01/json/{startIdx}/{endIdx}/{recipeName}")
                .encode()
                .build()
                .expand(secret, startIdx, endInx, "RCP_NM=" + recipeName)
                .toUri();
        ResponseEntity<GetRecipeResponse> response = restTemplate.getForEntity(uri, GetRecipeResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new OpenApiConnectionFailureException();
        }
    }







}
