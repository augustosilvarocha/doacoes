package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.exception.ResourceNotFoundException;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void test_criar_deve_salvar_item() {
        Usuario doador = new Usuario();
        doador.setId(1L);

        Item item = new Item();
        item.setNome("Notebook");
        item.setDescricao("Dell Inspiron");
        item.setCategoria(Categoria.ELETRONICOS);
        item.setDoador(doador);

        when(itemRepository.save(any())).thenReturn(item);

        Item resultado = itemService.criar(item);

        assertNotNull(resultado);
        assertEquals("Notebook", resultado.getNome());
        verify(itemRepository).save(item);
    }

    @Test
    void test_buscar_por_id_existente() {
        Item item = new Item();
        item.setId(1L);
        item.setNome("Notebook");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item resultado = itemService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void test_buscar_por_id_inexistente_deve_lancar_exception() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.buscarPorId(999L);
        });
    }

    @Test
    void test_atualizar_deve_manter_doador() {
        Usuario doador = new Usuario();
        doador.setId(1L);

        Item itemExistente = new Item();
        itemExistente.setId(1L);
        itemExistente.setNome("Nome Antigo");
        itemExistente.setDoador(doador);
        itemExistente.setStatus(StatusItem.DISPONIVEL);

        Item dadosNovos = new Item();
        dadosNovos.setNome("Novo Nome");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemExistente));
        when(itemRepository.save(any())).thenReturn(itemExistente);

        Item resultado = itemService.atualizar(1L, dadosNovos);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals(1L, resultado.getDoador().getId());
    }

    @Test
    void test_deletar_existente() {
        Item item = new Item();
        item.setId(1L);
        item.setStatus(StatusItem.DISPONIVEL);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deletar(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void test_deletar_inexistente_deve_lancar_exception() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.deletar(999L);
        });
    }
}