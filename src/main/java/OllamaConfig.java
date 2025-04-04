

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class OllamaConfig {

    private final String baseUrl;
    private final String modelName;

    @Inject
    public OllamaConfig(
            @ConfigProperty(name = "ollama.base.url", defaultValue = "http://localhost:11434") String baseUrl,
            @ConfigProperty(name = "ollama.model", defaultValue = "llama2") String modelName) {
        this.baseUrl = baseUrl;
        this.modelName = modelName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getModelName() {
        return modelName;
    }
}