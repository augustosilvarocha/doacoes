package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.MatchRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.MatchResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Match;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import augusto.rocha.ifrn.edu.br.doacoes.service.MatchService;
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
@RequestMapping("/matches")
@Tag(name = "Matches", description = "API para gerenciamento de matches entre pedidos e itens")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @Operation(summary = "Listar todos os matches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> listarTodos() {
        List<MatchResponseDTO> lista = matchService.listarTodos()
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar match por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match encontrado"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> buscarPorId(@PathVariable Long id) {
        Match match = matchService.buscarPorId(id);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Listar matches pendentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/pendentes")
    public ResponseEntity<List<MatchResponseDTO>> listarPendentes() {
        List<MatchResponseDTO> lista = matchService.listarPorStatus(StatusMatch.PENDENTE)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Listar matches ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/ativos")
    public ResponseEntity<List<MatchResponseDTO>> listarAtivos() {
        List<MatchResponseDTO> lista = matchService.listarPorStatus(StatusMatch.ATIVO)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Listar matches de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MatchResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<MatchResponseDTO> lista = matchService.listarPorUsuario(idUsuario)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Criar novo match entre pedido e item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Match criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou item indisponível"),
            @ApiResponse(responseCode = "404", description = "Pedido ou item não encontrado")
    })
    @PostMapping
    public ResponseEntity<MatchResponseDTO> criar(@Valid @RequestBody MatchRequestDTO dto) {
        Match match = matchService.criar(dto.getIdPedido(), dto.getIdItem());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Doador aceita o match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match aceito pelo doador"),
            @ApiResponse(responseCode = "400", description = "Match não está pendente"),
            @ApiResponse(responseCode = "403", description = "Usuário não é o doador"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{idMatch}/doador/{idUsuario}/aceitar")
    public ResponseEntity<MatchResponseDTO> doadorAceitar(
            @PathVariable Long idMatch,
            @PathVariable Long idUsuario) {
        Match match = matchService.doadorAceitar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Doador recusa o match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match recusado pelo doador"),
            @ApiResponse(responseCode = "400", description = "Match não está pendente"),
            @ApiResponse(responseCode = "403", description = "Usuário não é o doador"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{idMatch}/doador/{idUsuario}/recusar")
    public ResponseEntity<MatchResponseDTO> doadorRecusar(
            @PathVariable Long idMatch,
            @PathVariable Long idUsuario) {
        Match match = matchService.doadorRecusar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Solicitante aceita o match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match aceito pelo solicitante"),
            @ApiResponse(responseCode = "400", description = "Match não está pendente"),
            @ApiResponse(responseCode = "403", description = "Usuário não é o solicitante"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{idMatch}/solicitante/{idUsuario}/aceitar")
    public ResponseEntity<MatchResponseDTO> solicitanteAceitar(
            @PathVariable Long idMatch,
            @PathVariable Long idUsuario) {
        Match match = matchService.solicitanteAceitar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Solicitante recusa o match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match recusado pelo solicitante"),
            @ApiResponse(responseCode = "400", description = "Match não está pendente"),
            @ApiResponse(responseCode = "403", description = "Usuário não é o solicitante"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{idMatch}/solicitante/{idUsuario}/recusar")
    public ResponseEntity<MatchResponseDTO> solicitanteRecusar(
            @PathVariable Long idMatch,
            @PathVariable Long idUsuario) {
        Match match = matchService.solicitanteRecusar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Concluir match ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match concluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Match não está ativo"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<MatchResponseDTO> concluir(@PathVariable Long id) {
        Match match = matchService.concluir(id);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Cancelar match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Match já concluído"),
            @ApiResponse(responseCode = "404", description = "Match não encontrado")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<MatchResponseDTO> cancelar(@PathVariable Long id) {
        Match match = matchService.cancelar(id);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }
}