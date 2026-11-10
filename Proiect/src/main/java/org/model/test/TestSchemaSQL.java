package org.model.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestSchemaSQL {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        EntityManager em = emf.createEntityManager();
    }
}

/*
DROP SCHEMA postgres CASCADE;
DROP ROLE postgres;
CREATE ROLE postgres WITH SUPERUSER CREATEDB CREATEROLE LOGIN ENCRYPTED PASSWORD 'postgres';
CREATE SCHEMA postgres AUTHORIZATION postgres;
 */