package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Usuario;
import augusto.rocha.ifrn.edu.br.doacoes.model.enums.Perfil;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private Perfil perfil;
    private EnderecoDTO endereco;

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setPerfil(usuario.getPerfil());
        if (usuario.getEndereco() != null) {
            dto.setEndereco(EnderecoDTO.fromEntity(usuario.getEndereco()));
        }
        return dto;
    }
}
