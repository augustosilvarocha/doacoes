package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, ItemRepository itemRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.itemRepository = itemRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Usuario criar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscaPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
                );
    }

    public List<Pedido> listarPedidosDoUsuario(Usuario usuario) {
        return pedidoRepository.findBySolicitante(usuario);
    }

    public List<Item> listarItensDoUsuario(Usuario usuario) {
        return itemRepository.findByDoador(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscaPorId(id);

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(dados.getSenha());
        usuario.setTelefone(dados.getTelefone());
        usuario.setEndereco(dados.getEndereco());
        usuario.setPerfil(dados.getPerfil());

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
                );

        usuarioRepository.delete(usuario);
    }
}