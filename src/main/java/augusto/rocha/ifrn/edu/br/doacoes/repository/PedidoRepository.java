package augusto.rocha.ifrn.edu.br.doacoes.repository;

import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCategoria(Categoria categoria);
    List<Pedido> findBySolicitante(Usuario solicitante);
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByTitulo(String titulo);
}
