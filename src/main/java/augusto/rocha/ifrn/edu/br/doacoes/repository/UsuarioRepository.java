package augusto.rocha.ifrn.edu.br.doacoes.repository;

import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    List<Usuario> findByPerfil(Perfil perfil);
    Usuario findByEmail(String email);
}
