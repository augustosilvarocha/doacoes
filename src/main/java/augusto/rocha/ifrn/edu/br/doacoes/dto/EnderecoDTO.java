package augusto.rocha.ifrn.edu.br.doacoes.dto;

import augusto.rocha.ifrn.edu.br.doacoes.model.Endereco;
import lombok.Data;

@Data
public class EnderecoDTO {
    private String cidade;
    private String uf;
    private String rua;
    private String bairro;

    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setCidade(this.cidade);
        endereco.setUf(this.uf);
        endereco.setRua(this.rua);
        endereco.setBairro(this.bairro);
        return endereco;
    }

    public static EnderecoDTO fromEntity(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setCidade(endereco.getCidade());
        dto.setUf(endereco.getUf());
        dto.setRua(endereco.getRua());
        dto.setBairro(endereco.getBairro());
        return dto;
    }
}