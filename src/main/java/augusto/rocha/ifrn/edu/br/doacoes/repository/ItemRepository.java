package augusto.rocha.ifrn.edu.br.doacoes.repository;

import augusto.rocha.ifrn.edu.br.doacoes.model.*;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Categoria;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategoria(Categoria categoria);
    List<Item> findByStatus(StatusItem status);
    List<Item> findByDoador(Usuario usuario);
    Optional<Item> findFirstByCategoriaAndStatusOrderByIdAsc(Categoria categoria, StatusItem status);

    @Query("SELECT DISTINCT i.doador FROM Item i")
    List<Usuario> findUsuariosQueJaDoaram();
}