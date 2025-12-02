package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setup() {
        Usuario doador = new Usuario();
        doador.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setNome("Notebook");
        item.setDescricao("Notebook Dell");
        item.setCategoria(Categoria.ELETRONICOS);
        item.setStatus(StatusItem.DISPONIVEL);
        item.setDoador(doador);
    }

    @Test
    void test_criar_deve_salvar_item() {
        when(itemRepository.save(item)).thenReturn(item);

        Item resultado = itemService.criar(item);

        assertEquals(item, resultado);
        verify(itemRepository).save(item);
    }

    @Test
    void test_listar_todos_deve_retornar_lista() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> lista = itemService.listarTodos();

        assertEquals(1, lista.size());
        verify(itemRepository).findAll();
    }

    @Test
    void test_busca_por_id_com_id_existente_deve_retornar_item() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item encontrado = itemService.buscaPorId(1L);

        assertEquals(item, encontrado);
    }

    @Test
    void test_busca_por_id_com_id_inexistente_deve_lancar_exception() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> itemService.buscaPorId(1L));
    }

    @Test
    void test_buscar_por_categoria_deve_retornar_lista() {
        when(itemRepository.findByCategoria(Categoria.ELETRONICOS)).thenReturn(List.of(item));

        List<Item> itens = itemService.buscarPorCategoria(Categoria.ELETRONICOS);

        assertEquals(1, itens.size());
        verify(itemRepository).findByCategoria(Categoria.ELETRONICOS);
    }

    @Test
    void test_buscar_por_status_deve_retornar_lista() {
        when(itemRepository.findByStatus(StatusItem.DISPONIVEL)).thenReturn(List.of(item));

        List<Item> itens = itemService.buscarPorStatus(StatusItem.DISPONIVEL);

        assertEquals(1, itens.size());
        verify(itemRepository).findByStatus(StatusItem.DISPONIVEL);
    }

    @Test
    void test_atualizar_deve_atualizar_dados() {
        Item novos = new Item();
        Usuario novoDoador = new Usuario();
        novoDoador.setId(2L);

        novos.setNome("Novo Nome");
        novos.setDescricao("Nova Desc");
        novos.setCategoria(Categoria.MOVEIS);
        novos.setStatus(StatusItem.DOADO);
        novos.setDoador(novoDoador);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        Item atualizado = itemService.atualizar(1L, novos);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("Nova Desc", atualizado.getDescricao());
        assertEquals(Categoria.MOVEIS, atualizado.getCategoria());
        assertEquals(StatusItem.DOADO, atualizado.getStatus());
        assertEquals(novoDoador, atualizado.getDoador());

        verify(itemRepository).save(item);
    }

    @Test
    void test_deletar_com_id_existente_deve_deletar() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deletar(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void test_deletar_com_id_inexistente_deve_lancar_exception() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> itemService.deletar(1L));
    }
}