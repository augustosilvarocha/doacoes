package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.exception.ResourceNotFoundException;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item criar(Item item) {
        validarItem(item);
        return itemRepository.save(item);
    }

    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    public Item buscarPorId(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item não encontrado com id: " + id));
    }

    public List<Item> buscarPorCategoria(Categoria categoria) {
        return itemRepository.findByCategoria(categoria);
    }

    public List<Item> buscarPorStatus(StatusItem status) {
        return itemRepository.findByStatus(status);
    }

    public Item atualizar(Long id, Item dados) {
        Item item = buscarPorId(id);

        validarAtualizacao(item);

        if (StringUtils.hasText(dados.getNome())) {
            item.setNome(dados.getNome());
        }
        if (StringUtils.hasText(dados.getDescricao())) {
            item.setDescricao(dados.getDescricao());
        }
        if (dados.getCategoria() != null) {
            item.setCategoria(dados.getCategoria());
        }
        if (dados.getStatus() != null) {
            validarMudancaStatus(item.getStatus(), dados.getStatus());
            item.setStatus(dados.getStatus());
        }

        return itemRepository.save(item);
    }

    public void deletar(Long id) {
        Item item = buscarPorId(id);
        validarDelecao(item);
        itemRepository.deleteById(id);
    }

    private void validarItem(Item item) {
        if (!StringUtils.hasText(item.getNome())) {
            throw new IllegalArgumentException("Nome do item é obrigatório");
        }
        if (item.getDoador() == null) {
            throw new IllegalArgumentException("Doador é obrigatório");
        }
        if (item.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
    }

    private void validarAtualizacao(Item item) {
        if (item.getStatus() == StatusItem.DOADO) {
            throw new IllegalStateException(
                    "Não é possível atualizar item já doado");
        }
    }

    private void validarMudancaStatus(StatusItem statusAtual, StatusItem novoStatus) {
        if (statusAtual == StatusItem.DOADO && novoStatus == StatusItem.DISPONIVEL) {
            throw new IllegalStateException(
                    "Não é possível reverter item doado para disponível");
        }
    }

    private void validarDelecao(Item item) {
        if (item.getStatus() == StatusItem.DOADO) {
            throw new IllegalStateException(
                    "Não é possível deletar item já doado");
        }
    }
}
