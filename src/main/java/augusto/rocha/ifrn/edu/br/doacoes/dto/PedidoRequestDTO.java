package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import lombok.Data;

@Data
public class PedidoRequestDTO {

    private Long solicitanteId;
    private String titulo;
    private String descricao;
    private Categoria categoria;

    public Pedido toEntity(Usuario solicitante) {
        Pedido pedido = new Pedido();
        pedido.setSolicitante(solicitante);
        pedido.setTitulo(this.titulo);
        pedido.setDescricao(this.descricao);
        pedido.setCategoria(this.categoria);
        return pedido;
    }
}