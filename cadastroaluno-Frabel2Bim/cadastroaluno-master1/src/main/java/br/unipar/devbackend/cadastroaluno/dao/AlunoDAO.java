package br.unipar.devbackend.cadastroaluno.dao;

import br.unipar.devbackend.cadastroaluno.model.Aluno;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AlunoDAO {

    private EntityManager em;

    // Construtor recebe o EntityManager
    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    // READ - Lista todos os alunos
    public List<Aluno> findAll() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class)
                .getResultList();
    }

    // READ - Busca aluno pelo ID
    public Aluno findById(Long id) {
        return em.find(Aluno.class, id);
    }

    // READ - Busca aluno pelo RA
    public Aluno findByRA(String ra) {
        return em.createQuery("SELECT a FROM Aluno a WHERE a.ra = :ra", Aluno.class)
                .setParameter("ra", ra)
                .getSingleResult();
    }

    // CREATE - Inserir aluno
    public Aluno inserirAluno(Aluno aluno) {
        try {
            em.getTransaction().begin();
            em.persist(aluno);
            em.getTransaction().commit();
            return aluno;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Erro ao inserir aluno: " + ex.getMessage());
            return null;
        }
    }

    // UPDATE - Editar aluno
    public Aluno editarAluno(Aluno aluno) {
        try {
            em.getTransaction().begin();
            Aluno atualizado = em.merge(aluno);
            em.getTransaction().commit();
            return atualizado; // retorna o aluno atualizado
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Erro ao editar aluno: " + ex.getMessage());
            return null;
        }
    }

    // DELETE - Remover aluno
    public boolean removerAluno(Long id) {
        try {
            em.getTransaction().begin();
            Aluno aluno = em.find(Aluno.class, id);
            if (aluno != null) {
                em.remove(aluno);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("Aluno não encontrado para remoção.");
                return false;
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Erro ao remover aluno: " + ex.getMessage());
            return false;
        }
    }
}
