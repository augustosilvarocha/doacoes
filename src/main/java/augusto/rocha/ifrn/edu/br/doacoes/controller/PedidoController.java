package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoStatusUpdateDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.service.PedidoService;
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
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos de doação")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar todos os pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {
        List<PedidoResponseDTO> lista = pedidoService.listarTodos()
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(pedido));
    }

    @Operation(summary = "Buscar pedidos por usuário solicitante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        List<PedidoResponseDTO> lista = pedidoService.buscarPorSolicitante(usuario)
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Criar novo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedido = construirPedido(dto);
        Pedido salvo = pedidoService.criar(pedido);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PedidoResponseDTO.fromEntity(salvo));
    }

    @Operation(summary = "Atualizar pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido não pode ser atualizado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedido = construirPedido(dto);
        Pedido atualizado = pedidoService.atualizar(id, pedido);
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(atualizado));
    }

    @Operation(summary = "Atualizar status do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Mudança de status inválida")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody PedidoStatusUpdateDTO dto) {
        Pedido atualizado = pedidoService.atualizarStatus(id, dto.getStatus());
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(atualizado));
    }

    @Operation(summary = "Deletar pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido não pode ser deletado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Pedido construirPedido(PedidoRequestDTO dto) {
        Usuario solicitante = usuarioService.buscarPorId(dto.getSolicitanteId());
        return dto.toEntity(solicitante);
    }
}