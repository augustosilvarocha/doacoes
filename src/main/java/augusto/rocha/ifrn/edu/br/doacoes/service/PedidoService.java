package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.exception.ResourceNotFoundException;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criar(Pedido pedido) {
        validarPedido(pedido);
        pedido.setStatus(StatusPedido.ABERTO);
        return pedidoRepository.save(pedido);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado com id: " + id));
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    public List<Pedido> buscarPorSolicitante(Usuario usuario) {
        return pedidoRepository.findBySolicitante(usuario);
    }

    public List<Pedido> buscarPorTitulo(String titulo) {
        return pedidoRepository.findByTitulo(titulo);
    }

    public Pedido atualizar(Long id, Pedido dados) {
        Pedido pedido = buscarPorId(id);

        validarAtualizacao(pedido);

        if (StringUtils.hasText(dados.getTitulo())) {
            pedido.setTitulo(dados.getTitulo());
        }
        if (StringUtils.hasText(dados.getDescricao())) {
            pedido.setDescricao(dados.getDescricao());
        }
        if (dados.getCategoria() != null) {
            pedido.setCategoria(dados.getCategoria());
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        validarMudancaStatus(pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        validarDelecao(pedido);
        pedidoRepository.deleteById(id);
    }

    private void validarPedido(Pedido pedido) {
        if (!StringUtils.hasText(pedido.getTitulo())) {
            throw new IllegalArgumentException("Título do pedido é obrigatório");
        }
        if (pedido.getSolicitante() == null) {
            throw new IllegalArgumentException("Solicitante é obrigatório");
        }
        if (pedido.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (!StringUtils.hasText(pedido.getDescricao())) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
    }

    private void validarAtualizacao(Pedido pedido) {
        if (pedido.getStatus() == StatusPedido.ATENDIDO) {
            throw new IllegalStateException(
                    "Não é possível atualizar pedido já atendido");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalStateException(
                    "Não é possível atualizar pedido cancelado");
        }
    }

    private void validarMudancaStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        if (statusAtual == StatusPedido.ATENDIDO && novoStatus == StatusPedido.ABERTO) {
            throw new IllegalStateException(
                    "Não é possível reabrir pedido já atendido");
        }
        if (statusAtual == StatusPedido.CANCELADO && novoStatus == StatusPedido.ABERTO) {
            throw new IllegalStateException(
                    "Não é possível reabrir pedido cancelado");
        }
    }

    private void validarDelecao(Pedido pedido) {
        if (pedido.getStatus() == StatusPedido.ATENDIDO) {
            throw new IllegalStateException(
                    "Não é possível deletar pedido já atendido");
        }
    }
}
