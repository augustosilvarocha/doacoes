package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.UsuarioRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.UsuarioResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @Operation(summary = "Listar pedidos do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        List<PedidoResponseDTO> lista = usuarioService.listarPedidosDoUsuario(usuario)
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Listar itens do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemResponseDTO>> listarItens(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        List<ItemResponseDTO> lista = usuarioService.listarItensDoUsuario(usuario)
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Criar novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = usuarioService.criar(dto.toEntity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UsuarioResponseDTO.fromEntity(usuario));
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = usuarioService.atualizar(id, dto.toEntity());
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @Operation(summary = "Deletar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Usuário possui pedidos ou itens")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}