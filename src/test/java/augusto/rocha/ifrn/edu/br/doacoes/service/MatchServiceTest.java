package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.MatchRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private MatchService matchService;

    private Pedido pedido;
    private Item item;

    @BeforeEach
    void setup() {
        Usuario doador = new Usuario();
        doador.setId(10L);

        Usuario solicitante = new Usuario();
        solicitante.setId(20L);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCategoria(Categoria.ROUPA);
        pedido.setSolicitante(solicitante);

        item = new Item();
        item.setId(2L);
        item.setStatus(StatusItem.DISPONIVEL);
        item.setCategoria(Categoria.ROUPA);
        item.setDoador(doador);
    }

    @Test
    void criar_DeveCriarMatchComItemReservado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(matchRepository.existsByItemIdAndStatus(2L, StatusMatch.PENDENTE)).thenReturn(false);

        Match salvo = new Match();
        salvo.setId(99L);
        when(matchRepository.save(any(Match.class))).thenReturn(salvo);

        Match resultado = matchService.criar(1L, 2L);

        assertEquals(99L, resultado.getId());
        assertEquals(StatusItem.RESERVADO, item.getStatus());
        assertEquals(StatusMatch.PENDENTE, resultado.getStatus());

        verify(itemRepository).save(item);
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    void criar_DeveLancarErro_SeItemNaoDisponivel() {
        item.setStatus(StatusItem.DOADO);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        assertThrows(ResponseStatusException.class, () -> matchService.criar(1L, 2L));
    }

    @Test
    void criar_DeveLancarErro_SeCategoriasDiferirem() {
        item.setCategoria(Categoria.ELETRONICOS);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        assertThrows(ResponseStatusException.class, () -> matchService.criar(1L, 2L));
    }

    @Test
    void criar_DeveLancarErro_SeItemJaEstiverEmMatchPendente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(matchRepository.existsByItemIdAndStatus(2L, StatusMatch.PENDENTE)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> matchService.criar(1L, 2L));
    }
}