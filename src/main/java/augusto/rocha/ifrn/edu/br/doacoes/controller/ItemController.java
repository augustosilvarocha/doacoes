package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.service.ItemService;
import augusto.rocha.ifrn.edu.br.doacoes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itens")
@Tag(name = "Itens", description = "API para gerenciamento de itens doados")
public class ItemController {

    private final ItemService itemService;
    private final UsuarioService usuarioService;

    public ItemController(ItemService itemService, UsuarioService usuarioService) {
        this.itemService = itemService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Listar todos os itens",
            description = "Retorna uma lista com todos os itens cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de itens retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarTodos() {
        List<ItemResponseDTO> lista = itemService.listarTodos()
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(
            summary = "Buscar item por ID",
            description = "Retorna um item específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarPorId(
            @Parameter(description = "ID do item a ser buscado", required = true)
            @PathVariable Long id) {
        Item item = itemService.buscarPorId(id);
        return ResponseEntity.ok(ItemResponseDTO.fromEntity(item));
    }

    @Operation(
            summary = "Criar novo item",
            description = "Cadastra um novo item no sistema associado a um doador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Item criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário doador não encontrado",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<ItemResponseDTO> criar(
            @Parameter(description = "Dados do item a ser criado", required = true)
            @Valid @RequestBody ItemRequestDTO dto) {
        Item item = construirItem(dto);
        Item salvo = itemService.criar(item);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ItemResponseDTO.fromEntity(salvo));
    }

    @Operation(
            summary = "Atualizar item existente",
            description = "Atualiza parcialmente os dados de um item existente (PATCH)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item ou usuário não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Item não pode ser atualizado (ex: já foi doado)",
                    content = @Content
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizar(
            @Parameter(description = "ID do item a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do item", required = true)
            @Valid @RequestBody ItemRequestDTO dto) {
        Item item = construirItem(dto);
        Item atualizado = itemService.atualizar(id, item);
        return ResponseEntity.ok(ItemResponseDTO.fromEntity(atualizado));
    }

    @Operation(
            summary = "Deletar item",
            description = "Remove um item do sistema. Itens já doados não podem ser deletados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Item deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Item não pode ser deletado (ex: já foi doado)",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do item a ser deletado", required = true)
            @PathVariable Long id) {
        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Item construirItem(ItemRequestDTO dto) {
        Usuario doador = usuarioService.buscarPorId(dto.getUsuarioId());
        return dto.toEntity(doador);
    }
}