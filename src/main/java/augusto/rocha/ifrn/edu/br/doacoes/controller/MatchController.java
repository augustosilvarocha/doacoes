package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.MatchRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.MatchResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Match;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusMatch;
import augusto.rocha.ifrn.edu.br.doacoes.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/criar")
    public ResponseEntity<MatchResponseDTO> criarMatch(@RequestBody MatchRequestDTO dto) {
        Match match = matchService.criar(dto.getIdPedido(), dto.getIdItem());
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @PatchMapping("/{idMatch}/doador/{idUsuario}/aceitar")
    public ResponseEntity<MatchResponseDTO> doadorAceitar(@PathVariable Long idMatch, @PathVariable Long idUsuario) {
        Match match = matchService.doadorAceitar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @PatchMapping("/{idMatch}/doador/{idUsuario}/recusar")
    public ResponseEntity<MatchResponseDTO> doadorRecusar(@PathVariable Long idMatch, @PathVariable Long idUsuario) {
        Match match = matchService.doadorRecusar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @PatchMapping("/{idMatch}/solicitante/{idUsuario}/aceitar")
    public ResponseEntity<MatchResponseDTO> solicitanteAceitar(@PathVariable Long idMatch, @PathVariable Long idUsuario) {
        Match match = matchService.solicitanteAceitar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @PatchMapping("/{idMatch}/solicitante/{idUsuario}/recusar")
    public ResponseEntity<MatchResponseDTO> solicitanteRecusar(@PathVariable Long idMatch, @PathVariable Long idUsuario) {
        Match match = matchService.solicitanteRecusar(idMatch, idUsuario);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> listarTodos() {
        List<MatchResponseDTO> matches = matchService.listarTodos()
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> buscarPorId(@PathVariable Long id) {
        Match match = matchService.buscarPorId(id);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<MatchResponseDTO>> listarPendentes() {
        List<MatchResponseDTO> matches = matchService.listarPorStatus(StatusMatch.PENDENTE)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<MatchResponseDTO>> listarAtivos() {
        List<MatchResponseDTO> matches = matchService.listarPorStatus(StatusMatch.ATIVO)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(matches);
    }
}