package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import lombok.Data;

@Data
public class PedidoStatusUpdateDTO {

    private StatusPedido status;
}
