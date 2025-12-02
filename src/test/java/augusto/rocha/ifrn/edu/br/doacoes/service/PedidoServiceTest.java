package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setTitulo("Pedido de Roupa");
        pedido.setDescricao("Casaco de frio");
        pedido.setSolicitante(usuario);
        pedido.setCategoria(Categoria.ROUPA);
        pedido.setStatus(StatusPedido.ABERTO);
    }

    @Test
    void criar_DeveSalvarComStatusAberto() {
        Pedido novo = new Pedido();
        when(pedidoRepository.save(novo)).thenReturn(pedido);

        Pedido resultado = pedidoService.criar(novo);

        assertEquals(StatusPedido.ABERTO, resultado.getStatus());
        verify(pedidoRepository).save(novo);
    }

    @Test
    void buscaPorId_Existente_DeveRetornarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido encontrado = pedidoService.buscaPorId(1L);

        assertEquals(pedido, encontrado);
    }

    @Test
    void buscaPorId_Inexistente_DeveLancarException() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> pedidoService.buscaPorId(1L));
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> lista = pedidoService.ListarTodos();

        assertEquals(1, lista.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void listarPorStatus_DeveRetornarLista() {
        when(pedidoRepository.findByStatus(StatusPedido.ABERTO)).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.ListarPorStatus(StatusPedido.ABERTO);

        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findByStatus(StatusPedido.ABERTO);
    }

    @Test
    void buscarPorSolicitante_DeveRetornarLista() {
        when(pedidoRepository.findBySolicitante(usuario)).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.buscarPorSolicitante(usuario);

        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findBySolicitante(usuario);
    }

    @Test
    void buscaPorTitulo_DeveRetornarLista() {
        when(pedidoRepository.findByTitulo("Pedido de Roupa")).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.buscaPorTitulo("Pedido de Roupa");

        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findByTitulo("Pedido de Roupa");
    }

    @Test
    void atualizar_DeveAtualizarDados() {
        Pedido novosDados = new Pedido();
        Usuario novoSolicitante = new Usuario();
        novoSolicitante.setId(2L);

        novosDados.setTitulo("Novo Pedido");
        novosDados.setDescricao("Nova descrição");
        novosDados.setCategoria(Categoria.ELETRONICOS);
        novosDados.setSolicitante(novoSolicitante);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido atualizado = pedidoService.atualizar(1L, novosDados);

        assertEquals("Novo Pedido", atualizado.getTitulo());
        assertEquals("Nova descrição", atualizado.getDescricao());
        assertEquals(Categoria.ELETRONICOS, atualizado.getCategoria());
        assertEquals(novoSolicitante, atualizado.getSolicitante());

        verify(pedidoRepository).save(pedido);
    }

    @Test
    void deletar_Existente_DeveDeletar() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.deletar(1L);

        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void deletar_Inexistente_DeveLancarException() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> pedidoService.deletar(1L));
    }
}