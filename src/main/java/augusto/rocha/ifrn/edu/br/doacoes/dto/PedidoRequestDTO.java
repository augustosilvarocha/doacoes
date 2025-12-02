package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    private String titulo;
    private String descricao;
    private String categoria;

    public Pedido toEntity() {
        Pedido pedido = new Pedido();
        pedido.setTitulo(this.titulo);
        pedido.setDescricao(this.descricao);
        pedido.setCategoria(Categoria.valueOf(this.categoria.toUpperCase()));
        pedido.setStatus(StatusPedido.ABERTO);
        return pedido;
    }
}