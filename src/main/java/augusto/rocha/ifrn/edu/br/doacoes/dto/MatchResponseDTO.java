package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Match;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import lombok.Data;

@Data
public class MatchResponseDTO {
    private Long id;
    private Long itemId;
    private Long pedidoId;
    private StatusMatch status;
    private Boolean confirmadoPorSolicitante;
    private Boolean confirmadoPorDoador;

    public static MatchResponseDTO fromEntity(Match match) {
        MatchResponseDTO dto = new MatchResponseDTO();
        dto.setId(match.getId());
        dto.setItemId(match.getItem().getId());
        dto.setPedidoId(match.getPedido().getId());
        dto.setStatus(match.getStatus());
        dto.setConfirmadoPorSolicitante(match.getConfirmadoPorSolicitante());
        dto.setConfirmadoPorDoador(match.getConfirmadoPorDoador());
        return dto;
    }
}