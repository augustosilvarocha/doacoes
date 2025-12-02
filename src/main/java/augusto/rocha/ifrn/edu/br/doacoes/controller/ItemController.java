package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.service.ItemService;
import augusto.rocha.ifrn.edu.br.doacoes.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;
    private final UsuarioService usuarioService;

    public ItemController(ItemService itemService, UsuarioService usuarioService) {
        this.itemService = itemService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listar_todos() {
        List<ItemResponseDTO> lista = itemService.listarTodos()
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscar_item(@PathVariable Long id) {
        Item item = itemService.buscaPorId(id);
        return ResponseEntity.ok(ItemResponseDTO.fromEntity(item));
    }

    @PostMapping("")
    public ResponseEntity<ItemResponseDTO> adicionar_item(@RequestBody ItemRequestDTO dto) {

        Usuario doador = usuarioService.buscaPorId(dto.getUsuarioId());

        Item item = dto.toEntity(doador);
        item.setDoador(doador);

        Item salvo = itemService.criar(item);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ItemResponseDTO.fromEntity(salvo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizar_item(@PathVariable Long id, @RequestBody ItemRequestDTO dto) {

        Usuario doador = usuarioService.buscaPorId(dto.getUsuarioId());

        Item item = dto.toEntity(doador);
        item.setDoador(doador);

        Item atualizado = itemService.atualizar(id, item);
        return ResponseEntity.ok(ItemResponseDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover_item(@PathVariable Long id) {
        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}