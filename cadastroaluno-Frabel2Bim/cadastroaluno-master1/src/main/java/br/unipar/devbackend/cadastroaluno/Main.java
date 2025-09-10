package br.unipar.devbackend.cadastroaluno;

import br.unipar.devbackend.cadastroaluno.dao.AlunoDAO;
import br.unipar.devbackend.cadastroaluno.dao.EnderecoDAO;
import br.unipar.devbackend.cadastroaluno.model.Aluno;
import br.unipar.devbackend.cadastroaluno.model.Endereco;
import br.unipar.devbackend.cadastroaluno.util.EntityManagerUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inicializar conexão com banco
        EntityManagerUtil.getEmf();

        // Solicitar CEP
        System.out.print("Digite o CEP: ");
        String cep = scanner.nextLine().replaceAll("[^0-9]", "");

        // Buscar endereço no banco
        EnderecoDAO enderecoDAO = new EnderecoDAO(EntityManagerUtil.getEntityManager());
        Endereco endereco = enderecoDAO.findByCep(cep);

        if (endereco != null) {
            System.out.println("CEP encontrado no banco: " + endereco.getLogradouro());
        } else {
            System.out.println("Consultando ViaCEP...");
            endereco = consultarViaCep(cep);

            if (endereco != null) {
                enderecoDAO.inserirEndereco(endereco);
                System.out.println("Endereço salvo no banco!");
            } else {
                System.out.println("Erro ao consultar CEP. Tente novamente.");
                return;
            }
        }

        // Cadastrar aluno
        System.out.println("\n--- Cadastro de Aluno ---");
        Aluno aluno = new Aluno();

        System.out.print("Nome: ");
        aluno.setNome(scanner.nextLine());

        System.out.print("RA: ");
        aluno.setRa(scanner.nextLine());

        System.out.print("Telefone: ");
        aluno.setTelefone(scanner.nextLine());

        System.out.print("Email: ");
        aluno.setEmail(scanner.nextLine());

        aluno.setData_nasc(new Date());

        // Associar endereço ao aluno
        aluno.getEnderecos().add(endereco);
        endereco.setAluno(aluno);

        // Salvar aluno
        AlunoDAO alunoDAO = new AlunoDAO(EntityManagerUtil.getEntityManager());
        alunoDAO.inserirAluno(aluno);

        System.out.println("Aluno cadastrado com sucesso!");

        scanner.close();
        EntityManagerUtil.closeEntityManagerFactory();
    }

    private static Endereco consultarViaCep(String cep) {
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/xml/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Ler resposta
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            reader.close();

            // Parsear XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(resposta.toString().getBytes()));

            doc.getDocumentElement().normalize();

            // Verificar se CEP existe
            if (doc.getElementsByTagName("erro").getLength() > 0) {
                System.out.println("CEP não encontrado na API!");
                return null;
            }

            // Criar endereço
            Endereco endereco = new Endereco();
            endereco.setCep(getTagValue("cep", doc));
            endereco.setLogradouro(getTagValue("logradouro", doc));
            endereco.setBairro(getTagValue("bairro", doc));
            endereco.setLocalidade(getTagValue("localidade", doc));
            endereco.setUf(getTagValue("uf", doc));

            return endereco;

        } catch (Exception e) {
            System.out.println("Erro ao consultar API: " + e.getMessage());
            return null;
        }
    }

    private static String getTagValue(String tag, Document doc) {
        NodeList nodeList = doc.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}