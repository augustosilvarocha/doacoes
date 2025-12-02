package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Perfil;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private Perfil perfil;
    private EnderecoDTO endereco;

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setTelefone(this.telefone);
        usuario.setSenha(this.senha);
        usuario.setPerfil(this.perfil);
        if (this.endereco != null) {
            usuario.setEndereco(this.endereco.toEntity());
        }
        return usuario;
    }
}
