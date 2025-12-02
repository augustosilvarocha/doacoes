package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import lombok.Data;

@Data
public class ItemRequestDTO {
    private String nome;
    private String descricao;
    private Categoria categoria;
    private StatusItem statusItem;
    private Long usuarioId;

    public Item toEntity(Usuario usuario) {
        Item item = new Item();
        item.setNome(this.nome);
        item.setDescricao(this.descricao);
        item.setCategoria(this.categoria);
        item.setStatus(StatusItem.DISPONIVEL);
        item.setDoador(usuario);
        return item;
    }
}