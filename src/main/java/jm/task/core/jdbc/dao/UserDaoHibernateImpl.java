package jm.task.core.jdbc.dao;

import com.mysql.cj.util.Util;
import jm.task.core.jdbc.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateQuery;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                String createTable = """
                        CREATE TABLE IF NOT EXISTS users
                                               (
                                                   id       BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                   name     VARCHAR(55) ,
                                                   lastName VARCHAR(50) ,
                                                   age      TINYINT     null
                                               );""";
                session.createSQLQuery(createTable).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Таблица отсутствует" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                String query = "DROP TABLE IF EXISTS users";
                session.createSQLQuery(query).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Таблица не удалена" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                User user = new User();
                user.setName(name);
                user.setLastName(lastName);
                user.setAge(age);
                session.save(user);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Пользователь не добавлен" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                User user = session.load(User.class, id);
                session.delete(user);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Пользователь не удален" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                String getAll = "SELECT user FROM User user";
                list = session.createQuery(getAll, User.class).getResultList();
                transaction.commit();
            } catch (Exception e) {
                System.out.println("User не получен" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = (Transaction) session.beginTransaction();
            try {
                session.createSQLQuery("TRUNCATE TABLE users").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Таблица не очищена" + e.getMessage());
                try {
                    transaction.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}

