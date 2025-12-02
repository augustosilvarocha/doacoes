package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import lombok.Data;

@Data
public class ItemResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private StatusItem status;
    private Long doadorId;

    public static ItemResponseDTO fromEntity(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setTitulo(item.getNome());
        dto.setDescricao(item.getDescricao());
        dto.setCategoria(item.getCategoria());
        dto.setStatus(item.getStatus());
        dto.setDoadorId(item.getDoador().getId());
        return dto;
    }
}