package augusto.rocha.ifrn.edu.br.doacoes.service;

import augusto.rocha.ifrn.edu.br.doacoes.exception.ResourceNotFoundException;
import augusto.rocha.ifrn.edu.br.doacoes.model.Item;
import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.repository.ItemRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import augusto.rocha.ifrn.edu.br.doacoes.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ItemRepository itemRepository,
                          PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.itemRepository = itemRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Usuario criar(Usuario usuario) {
        validarUsuario(usuario);
        validarEmailUnico(usuario.getEmail(), null);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com id: " + id));
    }

    public List<Pedido> listarPedidosDoUsuario(Usuario usuario) {
        return pedidoRepository.findBySolicitante(usuario);
    }

    public List<Item> listarItensDoUsuario(Usuario usuario) {
        return itemRepository.findByDoador(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);

        if (StringUtils.hasText(dados.getNome())) {
            usuario.setNome(dados.getNome());
        }
        if (StringUtils.hasText(dados.getEmail())) {
            validarEmailUnico(dados.getEmail(), id);
            usuario.setEmail(dados.getEmail());
        }
        if (StringUtils.hasText(dados.getSenha())) {
            usuario.setSenha(dados.getSenha());
        }
        if (StringUtils.hasText(dados.getTelefone())) {
            usuario.setTelefone(dados.getTelefone());
        }
        if (dados.getEndereco() != null) {
            usuario.setEndereco(dados.getEndereco());
        }
        if (dados.getPerfil() != null) {
            usuario.setPerfil(dados.getPerfil());
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        validarDelecao(usuario);
        usuarioRepository.deleteById(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (!StringUtils.hasText(usuario.getNome())) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (!StringUtils.hasText(usuario.getEmail())) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (!usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (!StringUtils.hasText(usuario.getSenha())) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }
        if (usuario.getPerfil() == null) {
            throw new IllegalArgumentException("Perfil é obrigatório");
        }
    }

    private void validarEmailUnico(String email, Long usuarioId) {
        Usuario usuarioExistente = usuarioRepository.findByEmail(email);
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioId)) {
            throw new IllegalStateException("Email já cadastrado");
        }
    }

    private void validarDelecao(Usuario usuario) {
        List<Pedido> pedidos = pedidoRepository.findBySolicitante(usuario);
        if (!pedidos.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível deletar usuário com pedidos cadastrados");
        }

        List<Item> itens = itemRepository.findByDoador(usuario);
        if (!itens.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível deletar usuário com itens cadastrados");
        }
    }
}
