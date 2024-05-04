package org.example.dao;

import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.entity.QUser;
import org.example.entity.User;
import org.example.managers.HibernateManager;
import org.hibernate.SessionFactory;

import java.math.BigInteger;

import static org.example.entity.QUser.*;

public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao(HibernateManager hibernateManager) {

        sessionFactory = hibernateManager.getConfiguration().buildSessionFactory();
    }

    public ImmutablePair<BigInteger,String> getPasswordAndSaltByLogin(String login) throws FailedTransactionException{
        @Cleanup var session = sessionFactory.openSession();
        if (login == null) return null;
        session.beginTransaction();
        try {
            var hashAndSalt=new JPAQuery<QUser>(session)
                    .select(user.password, user.salt)
                    .from(user)
                    .where(user.login.eq(login))
                    .fetchOne();
            session.getTransaction().commit();
            if (hashAndSalt == null) return null;
            var hash = hashAndSalt.get(0,BigInteger.class);
            var salt = hashAndSalt.get(1,String.class);
            return new ImmutablePair<BigInteger,String>(hash,salt);
        }catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }

    }
    public void insertUser(String login, BigInteger hash, String salt) throws FailedTransactionException{
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            User user = User.builder()
                    .login(login)
                    .password(hash)
                    .salt(salt)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            throw new  FailedTransactionException("Транзакция не удалась");
        }


    }
}
