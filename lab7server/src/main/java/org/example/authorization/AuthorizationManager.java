package org.example.authorization;

import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.dao.FailedTransactionException;
import org.example.dao.UserDao;
import org.example.managers.HibernateManager;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class AuthorizationManager {
    @Setter
    private static UserDao userDao;
    public static ImmutablePair<Boolean,Boolean> checkLoginAndPassword(String login,String password)  throws FailedTransactionException{
        var loginCorrect = false;
        var passwordCorrect = false;

        var hashAndSalt=userDao.getPasswordAndSaltByLogin(login);
        if (hashAndSalt == null ) return new ImmutablePair<>(loginCorrect,passwordCorrect);
        loginCorrect = true;
        var hash = hashAndSalt.getLeft();
        var salt = hashAndSalt.getRight();
        BigInteger currentHash = null;
        try {
            currentHash = PasswordManager.getHash(password,salt);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        if (currentHash.equals(hash)) passwordCorrect = true;
        return new ImmutablePair<>(loginCorrect,passwordCorrect);
    }
    public static void register(String login,String password)  throws FailedTransactionException  {
        if (!loginIsUnique(login))
            throw new AuthorizationException("Пользователь с таким логином уже существует");
        saveNewPassword(login,password);
    }
    private static void saveNewPassword(String login,String password) throws FailedTransactionException {
        ImmutablePair<BigInteger, String> hashAndSalt = null;
        try {
            hashAndSalt = PasswordManager.getHash(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {

        }
        var hash = hashAndSalt.getLeft();
        var salt = hashAndSalt.getRight();
        userDao.insertUser(login,hash,salt);
    }
    private static boolean loginIsUnique(String login) throws FailedTransactionException {
        var hashAndSalt=userDao.getPasswordAndSaltByLogin(login);
        if (hashAndSalt == null)
            return  true;
        else
            return false;
    }
}
