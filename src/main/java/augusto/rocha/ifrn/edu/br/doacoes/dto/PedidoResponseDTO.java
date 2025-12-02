package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import lombok.Data;

@Data
public class PedidoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private StatusPedido status;
    private Long solicitanteId;

    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setTitulo(pedido.getTitulo());
        dto.setDescricao(pedido.getDescricao());
        dto.setCategoria(pedido.getCategoria());
        dto.setStatus(pedido.getStatus());
        dto.setSolicitanteId(pedido.getSolicitante().getId());
        return dto;
    }
}