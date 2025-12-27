package augusto.rocha.ifrn.edu.br.doacoes.model;

import augusto.rocha.ifrn.edu.br.doacoes.model.enums.*;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_item", nullable = false, unique = true)
    @NotNull(message = "Item é obrigatório")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @NotNull(message = "Pedido é obrigatório")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatch status = StatusMatch.PENDENTE;

    @Column(name = "confirmado_por_doador")
    private Boolean confirmadoPorDoador = false;

    @Column(name = "confirmado_por_solicitante")
    private Boolean confirmadoPorSolicitante = false;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Size(max = 500)
    @Column(length = 500)
    private String observacoes;
}