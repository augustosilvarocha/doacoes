package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.MatchRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match não encontrado."));
    }

    public Match criar(Long idPedido, Long idItem) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));

        Item item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        if (item.getStatus() != StatusItem.DISPONIVEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item não está disponível.");
        }

        if (item.getCategoria() != pedido.getCategoria()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria do item e do pedido não coincidem.");
        }

        if (matchRepository.existsByItemIdAndStatus(idItem, StatusMatch.PENDENTE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este item já está reservado em outro match pendente.");
        }

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

    private void atualizarStatusSePronto(Match match) {
        if (match.getConfirmadoPorDoador() && match.getConfirmadoPorSolicitante()) {
            match.setStatus(StatusMatch.ATIVO);
        }
    }

    public Match concluir(Long id) {
        Match match = buscarPorId(id);

        if (match.getStatus() != StatusMatch.ATIVO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível concluir um match ativo.");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar um match concluído.");
        }

        match.setStatus(StatusMatch.CANCELADO);

        Item item = match.getItem();
        item.setStatus(StatusItem.DISPONIVEL);
        itemRepository.save(item);

        return matchRepository.save(match);
    }

    public Match solicitanteAceitar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);

        if (!match.getPedido().getSolicitante().getId().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não é o solicitante deste match.");
        }

        if (match.getStatus() != StatusMatch.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match não está pendente.");
        }

        match.setConfirmadoPorSolicitante(true);
        atualizarStatusSePronto(match);

        return matchRepository.save(match);
    }

    public Match solicitanteRecusar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);

        if (!match.getPedido().getSolicitante().getId().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não é o solicitante deste match.");
        }

        if (match.getStatus() != StatusMatch.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match não está pendente.");
        }

        match.setStatus(StatusMatch.CANCELADO);

        Item item = match.getItem();
        item.setStatus(StatusItem.DISPONIVEL);
        itemRepository.save(item);

        return matchRepository.save(match);
    }

    public Match doadorAceitar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);

        if (!match.getItem().getDoador().getId().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não é o doador deste item.");
        }

        if (match.getStatus() != StatusMatch.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match não está pendente.");
        }

        match.setConfirmadoPorDoador(true);
        atualizarStatusSePronto(match);

        return matchRepository.save(match);
    }

    public Match doadorRecusar(Long idMatch, Long idUsuario) {
        Match match = buscarPorId(idMatch);

        if (!match.getItem().getDoador().getId().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não é o doador deste item.");
        }

        if (match.getStatus() != StatusMatch.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match não está pendente.");
        }

        match.setStatus(StatusMatch.CANCELADO);

        Item item = match.getItem();
        item.setStatus(StatusItem.DISPONIVEL);
        itemRepository.save(item);

        return matchRepository.save(match);
    }

    public List<Match> listarPorUsuario(Long idUsuario) {
        return matchRepository.findByPedidoSolicitanteId(idUsuario);
    }
}