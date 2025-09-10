package br.unipar.devbackend.cadastroaluno.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public EntityManagerUtil() { }

    public static EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence
                    .createEntityManagerFactory("AlunoEnderecoPU");
            System.out.println("Conexão aberta com sucesso!");
        }
        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("Conexão fechada com sucesso!");
        }
    }

    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            em = getEmf().createEntityManager();
            System.out.println("EntityManager criado com sucesso!");
        }
        return em;
    }

}
