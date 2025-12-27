package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.exception.ResourceNotFoundException;
import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.MatchRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ItemRepository itemRepository;
    private final PedidoRepository pedidoRepository;

    public MatchService(MatchRepository matchRepository, ItemRepository itemRepository, PedidoRepository pedidoRepository) {
        this.matchRepository = matchRepository;
        this.itemRepository = itemRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Match> listarTodos() {
        return matchRepository.findAll();
    }

    public Match buscarPorId(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match não encontrado com id: " + id));
    }

    public Match criar(Long idPedido, Long idItem) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado com id: " + idPedido));

        Item item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item não encontrado com id: " + idItem));

        validarCriacaoMatch(item, pedido);

        Match match = new Match();
        match.setItem(item);
        match.setPedido(pedido);
        match.setStatus(StatusMatch.PENDENTE);

        item.setStatus(StatusItem.RESERVADO);
        itemRepository.save(item);

        return matchRepository.save(match);
    }

    public List<Match> listarPorStatus(StatusMatch status) {
        return matchRepository.findByStatus(status);
    }

    public List<Match> listarPorUsuario(Long idUsuario) {
        return matchRepository.findByPedidoSolicitanteId(idUsuario);
    }

    public Match solicitanteAceitar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);
        validarSolicitante(match, idUsuario);
        validarStatusPendente(match);

        match.setConfirmadoPorSolicitante(true);
        atualizarStatusSePronto(match);

        return matchRepository.save(match);
    }

    public Match solicitanteRecusar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);
        validarSolicitante(match, idUsuario);
        validarStatusPendente(match);

        return cancelarMatch(match);
    }

    public Match doadorAceitar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);
        validarDoador(match, idUsuario);
        validarStatusPendente(match);

        match.setConfirmadoPorDoador(true);
        atualizarStatusSePronto(match);

        return matchRepository.save(match);
    }

    public Match doadorRecusar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);
        validarDoador(match, idUsuario);
        validarStatusPendente(match);

        return cancelarMatch(match);
    }

    public Match concluir(Long id) {
        Match match = buscarPorId(id);

        if (match.getStatus() != StatusMatch.ATIVO) {
            throw new IllegalStateException(
                    "Só é possível concluir um match ativo");
        }

        match.setStatus(StatusMatch.CONCLUIDO);
        match.setDataConclusao(LocalDateTime.now());

        Item item = match.getItem();
        item.setStatus(StatusItem.DOADO);
        itemRepository.save(item);

        return matchRepository.save(match);
    }

    public Match cancelar(Long id) {
        Match match = buscarPorId(id);

        if (match.getStatus() == StatusMatch.CONCLUIDO) {
            throw new IllegalStateException(
                    "Não é possível cancelar um match concluído");
        }

        return cancelarMatch(match);
    }

    private void validarCriacaoMatch(Item item, Pedido pedido) {
        if (item.getStatus() != StatusItem.DISPONIVEL) {
            throw new IllegalStateException("Item não está disponível");
        }

        if (item.getCategoria() != pedido.getCategoria()) {
            throw new IllegalArgumentException(
                    "Categoria do item e do pedido não coincidem");
        }

        if (matchRepository.existsByItemIdAndStatus(item.getId(), StatusMatch.PENDENTE)) {
            throw new IllegalStateException(
                    "Este item já está reservado em outro match pendente");
        }
    }

    private void validarSolicitante(Match match, Long idUsuario) {
        if (!match.getPedido().getSolicitante().getId().equals(idUsuario)) {
            throw new IllegalStateException(
                    "Usuário não é o solicitante deste match");
        }
    }

    private void validarDoador(Match match, Long idUsuario) {
        if (!match.getItem().getDoador().getId().equals(idUsuario)) {
            throw new IllegalStateException(
                    "Usuário não é o doador deste item");
        }
    }

    private void validarStatusPendente(Match match) {
        if (match.getStatus() != StatusMatch.PENDENTE) {
            throw new IllegalStateException("Match não está pendente");
        }
    }

    private void atualizarStatusSePronto(Match match) {
        if (match.getConfirmadoPorDoador() && match.getConfirmadoPorSolicitante()) {
            match.setStatus(StatusMatch.ATIVO);
        }
    }

    private Match cancelarMatch(Match match) {
        match.setStatus(StatusMatch.CANCELADO);

        Item item = match.getItem();
        item.setStatus(StatusItem.DISPONIVEL);
        itemRepository.save(item);

        return matchRepository.save(match);
    }
}
