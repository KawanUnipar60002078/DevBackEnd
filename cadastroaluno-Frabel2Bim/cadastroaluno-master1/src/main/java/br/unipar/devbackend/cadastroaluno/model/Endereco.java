package br.unipar.devbackend.cadastroaluno.model;

import jakarta.persistence.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.StringReader;
import java.util.Date;

@Getter
@Setter
@Entity
@XmlRootElement (name = "xmlcep") // nome da raiz do xml
@XmlAccessorType(XmlAccessType.FIELD) // os atributos que são mapeados
public class Endereco {


    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro = new Date(); // Data/hora do cadastro
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    @ManyToOne
    @JoinColumn(name = "aluno_id") //FK
    private Aluno aluno;

    public static Endereco unmarshalFromString(String stringXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Endereco.class);  // cria o contexto JAXB
        //objeto que faz o trabalho de converter o XML em objeto Java
        Unmarshaller nmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(stringXml); //lê a string como se fosse um arquivo
        return (Endereco) nmarshaller.unmarshal(reader); //coverte o XML em objeto Java


    }
}
