package br.unipar.devbackend.cadastroaluno.dao;

import br.unipar.devbackend.cadastroaluno.model.Endereco;
import jakarta.persistence.EntityManager;
import java.util.List;

public class EnderecoDAO {

    private EntityManager em;

    // Construtor recebe o EntityManager
    public EnderecoDAO(EntityManager em) {
        this.em = em;
    }

    // READ - Lista todos os endereços
    public List<Endereco> findAll() {
        return em.createQuery("SELECT e FROM Endereco e", Endereco.class)
                .getResultList();
    }

    // CREATE - Salva um novo endereço
    public void save(Endereco endereco) {
        em.getTransaction().begin();
        em.persist(endereco);
        em.getTransaction().commit();
    }

    // UPDATE - Atualiza um endereço
    public void update(Endereco endereco) {
        em.getTransaction().begin();
        em.merge(endereco);
        em.getTransaction().commit();
    }

    // DELETE - Remove um endereço
    public void delete(Endereco endereco) {
        em.getTransaction().begin();
        em.remove(endereco);
        em.getTransaction().commit();
    }

    // FIND BY ID
    public Endereco findById(Long id) {
        return em.find(Endereco.class, id);
    }

    // FIND BY CEP
    public Endereco findByCep(String cep) {
        try {
            return em.createQuery("SELECT e FROM Endereco e WHERE e.cep = :cep", Endereco.class)
                    .setParameter("cep", cep)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Retorna null se não encontrar
        }
    }

    public void inserirEndereco(Endereco endereco) {
    }
}
