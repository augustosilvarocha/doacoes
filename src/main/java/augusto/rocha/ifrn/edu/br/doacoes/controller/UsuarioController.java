package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.UsuarioRequestDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.UsuarioResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.dto.ItemResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> dtoList = usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.buscaPorId(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscaPorId(id);
        List<PedidoResponseDTO> pedidosDto = usuarioService.listarPedidosDoUsuario(usuario)
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidosDto);
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemResponseDTO>> listarItensPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscaPorId(id);
        List<ItemResponseDTO> itensDto = usuarioService.listarItensDoUsuario(usuario)
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itensDto);
    }

    @PostMapping("")
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody UsuarioRequestDTO dto){
        Usuario usuario = usuarioService.criar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseDTO.fromEntity(usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto){
        Usuario usuario = usuarioService.atualizar(id, dto.toEntity());
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}