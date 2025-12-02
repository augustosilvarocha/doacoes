package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item criar(Item item){
        return itemRepository.save(item);
    }

    public List<Item> listarTodos(){
        return itemRepository.findAll();
    }

    public Item buscaPorId(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
    }

    public List<Item> buscarPorCategoria(Categoria categoria) {
        return itemRepository.findByCategoria(categoria);
    }

    public List<Item> buscarPorStatus(StatusItem status) {
        return itemRepository.findByStatus(status);
    }

    public Item atualizar(Long id, Item dados){
        Item item = buscaPorId(id);
        item.setNome(dados.getNome());
        item.setDescricao(dados.getDescricao());
        item.setStatus(dados.getStatus());
        item.setCategoria(dados.getCategoria());
        item.setDoador(dados.getDoador());
        return itemRepository.save(item);
    }

    public void deletar(Long id) {
        buscaPorId(id);
        itemRepository.deleteById(id);
    }
}