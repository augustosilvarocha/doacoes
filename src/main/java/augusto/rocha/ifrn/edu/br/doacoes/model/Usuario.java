package augusto.rocha.ifrn.edu.br.doacoes.model;

import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Perfil;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Endereco endereco;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, max = 100)
    @Column(nullable = false, length = 100)
    private String nome;


    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    @Column(length = 11)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil = Perfil.AMBOS;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;
}