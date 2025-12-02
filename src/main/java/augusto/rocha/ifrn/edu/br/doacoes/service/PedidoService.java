package augusto.rocha.ifrn.edu.br.doacoes.service;


import augusto.rocha.ifrn.edu.br.doacoes.model.Pedido;
import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusPedido;
import augusto.rocha.ifrn.edu.br.doacoes.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criar(Pedido pedido){
        pedido.setStatus(StatusPedido.ABERTO);
        return pedidoRepository.save(pedido);
    }

    public Pedido buscaPorId(Long id){
        return pedidoRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }

    public List<Pedido> ListarTodos(){
        return pedidoRepository.findAll();
    }

    public List<Pedido> ListarPorStatus(StatusPedido status){
        return pedidoRepository.findByStatus(status);
    }

    public List<Pedido> buscarPorSolicitante(Usuario usuario){
        return pedidoRepository.findBySolicitante(usuario);
    }

    public List<Pedido> buscaPorTitulo(String titulo){
        return pedidoRepository.findByTitulo(titulo);
    }

    public Pedido atualizar(Long id, Pedido dados){
        Pedido pedido = buscaPorId(id);

        pedido.setTitulo(dados.getTitulo());
        pedido.setSolicitante(dados.getSolicitante());
        pedido.setDescricao(dados.getDescricao());
        pedido.setCategoria(dados.getCategoria());

        return pedidoRepository.save(pedido);
    }

    public void deletar(Long id){
        buscaPorId(id);
        pedidoRepository.deleteById(id);
    }
}
