package augusto.rocha.ifrn.edu.br.doacoes.model;

import augusto.rocha.ifrn.edu.br.doacoes.model.enums.*;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    @NotNull(message = "Solicitante é obrigatório")
    private Usuario solicitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.ABERTO;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;
}