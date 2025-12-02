package augusto.rocha.ifrn.edu.br.doacoes.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@Embeddable
public class Endereco {

    @NotBlank(message = "Cidade é obrigatória")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "UF é obrigatória")
    @Pattern(regexp = "[A-Z]{2}", message = "UF deve ter 2 letras.")
    @Column(nullable = false, length = 2)
    private String uf;

    @Column(length = 100)
    private String rua;

    @Column(length = 100)
    private String bairro;
}
