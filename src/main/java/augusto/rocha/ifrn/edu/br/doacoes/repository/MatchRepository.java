package augusto.rocha.ifrn.edu.br.doacoes.repository;

import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByItem(Item item);
    List<Match> findByPedido(Pedido pedido);
    List<Match> findByStatus(StatusMatch status);
    List<Match> findByConfirmadoPorSolicitante(Boolean confirmado);
    List<Match> findByConfirmadoPorDoador(Boolean confirmado);
    List<Match> findByPedidoSolicitanteId(Long solicitanteId);
    boolean existsByItemIdAndStatus(Long itemId, StatusMatch status);
}