package augusto.rocha.ifrn.edu.br.doacoes.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI doacoesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Doações")
                        .description("Gerenciamento de Usuários, Pedidos e Matches")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Augusto Rocha")
                                .email("seuemail@exemplo.com")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/seuusuario/doacoes")
                );
    }
}