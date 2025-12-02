package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.Endereco;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Endereco endereco;

    @BeforeEach
    void setup() {
        endereco = new Endereco();
        endereco.setRua("Rua X");
        endereco.setCidade("Natal");
        endereco.setUf("RN");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123");
        usuario.setTelefone("9000-0000");
        usuario.setEndereco(endereco);
    }

    @Test
    void criar_DeveSalvarUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.criar(usuario);

        assertEquals(usuario, resultado);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> lista = usuarioService.listarTodos();

        assertEquals(1, lista.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void buscaPorId_Existente_DeveRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscaPorId(1L);

        assertEquals(usuario, encontrado);
    }

    @Test
    void buscaPorId_Inexistente_DeveLancarException() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.buscaPorId(1L));
    }

    @Test
    void listarPedidosDoUsuario_DeveRetornarPedidos() {
        Pedido p = new Pedido();
        when(pedidoRepository.findBySolicitante(usuario)).thenReturn(List.of(p));

        List<Pedido> pedidos = usuarioService.listarPedidosDoUsuario(usuario);

        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findBySolicitante(usuario);
    }

    @Test
    void listarItensDoUsuario_DeveRetornarItens() {
        Item item = new Item();
        when(itemRepository.findByDoador(usuario)).thenReturn(List.of(item));

        List<Item> itens = usuarioService.listarItensDoUsuario(usuario);

        assertEquals(1, itens.size());
        verify(itemRepository).findByDoador(usuario);
    }

    @Test
    void atualizar_DeveAtualizarDados() {
        Endereco novoEndereco = new Endereco();
        novoEndereco.setRua("Rua Nova");
        novoEndereco.setCidade("Mossoró");
        novoEndereco.setUf("RN");

        Usuario novosDados = new Usuario();
        novosDados.setNome("Novo Nome");
        novosDados.setEmail("novo@email.com");
        novosDados.setSenha("321");
        novosDados.setTelefone("98888-8888");
        novosDados.setEndereco(novoEndereco);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario atualizado = usuarioService.atualizar(1L, novosDados);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals("321", atualizado.getSenha());
        assertEquals("98888-8888", atualizado.getTelefone());
        assertEquals("Rua Nova", atualizado.getEndereco().getRua());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deletar_Existente_DeveDeletar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(1L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deletar_Inexistente_DeveLancarException() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.deletar(1L));
    }
}