package augusto.rocha.ifrn.edu.br.doacoes.controller;

import augusto.rocha.ifrn.edu.br.doacoes.dto.PedidoResponseDTO;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.service.PedidoService;
import augusto.rocha.ifrn.edu.br.doacoes.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        List<PedidoResponseDTO> lista = pedidoService.ListarTodos()
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscaPorId(id);
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(pedido));
    }

    @GetMapping("/buscar-por-usuario/{idUsuario}")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPedidoPorUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.buscaPorId(idUsuario);
        List<PedidoResponseDTO> lista = pedidoService.buscarPorSolicitante(usuario)
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    public static class CriarPedidoDTO {
        public Long solicitanteId;
        public String titulo;
        public String descricao;
        public String categoria;
    }

    @PostMapping("")
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody CriarPedidoDTO dto) {
        Usuario usuario = usuarioService.buscaPorId(dto.solicitanteId);

        Pedido pedido = new Pedido();
        pedido.setSolicitante(usuario);
        pedido.setTitulo(dto.titulo);
        pedido.setDescricao(dto.descricao);
        pedido.setCategoria(Categoria.valueOf(dto.categoria.toUpperCase()));

        Pedido salvo = pedidoService.criar(pedido);
        return ResponseEntity.status(201).body(PedidoResponseDTO.fromEntity(salvo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody CriarPedidoDTO dto) {
        Usuario usuario = usuarioService.buscaPorId(dto.solicitanteId);

        Pedido pedido = new Pedido();
        pedido.setSolicitante(usuario);
        pedido.setTitulo(dto.titulo);
        pedido.setDescricao(dto.descricao);
        pedido.setCategoria(Categoria.valueOf(dto.categoria.toUpperCase()));

        Pedido atualizado = pedidoService.atualizar(id, pedido);
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}