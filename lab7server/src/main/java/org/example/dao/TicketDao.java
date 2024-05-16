package org.example.dao;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import org.common.commands.authorization.NoAccessException;
import org.common.dao.interfaces.CollectionInDatabaseManager;
import org.common.dto.Ticket;
import org.common.utility.InvalidFormatException;
import org.example.managers.HibernateManager;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.common.dto.QTicket.ticket;

public class TicketDao implements CollectionInDatabaseManager {
    private final SessionFactory sessionFactory;

    public TicketDao(HibernateManager hibernateManager) {

        sessionFactory = hibernateManager.getConfiguration().buildSessionFactory();
    }
    @Override
    public List<Long> removeGreater(String login, Long price) {
        @Cleanup var session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            var deletedTickets=  new JPAQuery<Ticket>(session)
                    .select(ticket.id)
                    .from(ticket)
                    .where(ticket.price.gt(price).and(ticket.createdBy.eq(login)) )
                    .fetch();
            new JPADeleteClause(session,ticket)
                    .where(ticket.price.gt(price).and(ticket.createdBy.eq(login)) )
                    .execute();
            session.getTransaction().commit();
            return deletedTickets;
        } catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }
    }
    @Override
    public HashMap<Long, Ticket> loadCollection() throws FailedTransactionException{
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            HashMap <Long,Ticket> ticketHashMap = (HashMap<Long, Ticket>) new JPAQuery<Ticket>(session)
                    .select(ticket)
                    .from(ticket)
                    .fetch().stream()
                    .collect(Collectors.toMap(
                            ticket -> ticket.getId(),
                            // Функция для извлечения значения
                            ticket -> ticket
                    ));
            session.getTransaction().commit();
            return ticketHashMap;
        }
        catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция загрузки не удалась");
        }

    }

    @Override
    public List<Long> clear(String login) throws FailedTransactionException {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            var deletedId=  new JPAQuery<Ticket>(session)
                    .select(ticket.id)
                    .from(ticket)
                    .where(ticket.createdBy.eq(login))
                    .fetch();
            new JPADeleteClause(session,ticket)
                    .where(ticket.createdBy.eq(login))
                    .execute();
            session.getTransaction().commit();
            return deletedId;
        } catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }

    }

    @Override
    public Optional<Ticket> insert(Ticket ticket) throws FailedTransactionException {
        @Cleanup var session = sessionFactory.openSession();
            session.beginTransaction();
        try {
            session.save(ticket);
            var ticketUpd=session.find(Ticket.class,ticket.getId());
            session.getTransaction().commit();
            return Optional.ofNullable(ticket);
        } catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }

    }
    @Override
    public Optional<Ticket> update(Ticket newTicket, String login) throws FailedTransactionException,NoAccessException {
        @Cleanup var session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Ticket oldTicket = session.get(Ticket.class, newTicket.getId());
            if (oldTicket.getCreatedBy().equals(login)) {
                session.merge(newTicket); // Заменяем существующий билет новым
                var newTicketUpd=session.find(Ticket.class,newTicket.getId());

                session.getTransaction().commit();
//                newTicketUpd.setVenue(session.find(Venue.class,newTicketUpd.getVenue()));
//                newTicketUpd.setCoordinates(session.find(Coordinates.class,newTicketUpd.getCoordinates())); // Обновляем состояние объекта в текущей сессии// Обновляем состояние объекта в текущей сессии
//



                return Optional.ofNullable(newTicketUpd);
            } else {
                session.getTransaction().rollback();
                throw new NoAccessException("Нет доступа");

            }
        }catch (Exception e){
            if (e instanceof NoAccessException) throw e;
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }


    }

    @Override
    public Ticket removeTicket(Long id, String login) throws FailedTransactionException, NoAccessException {
        @Cleanup var session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            var tick = new JPAQuery<Ticket>(session)
                    .select(ticket)
                    .from(ticket)
                    .where(ticket.id.eq(id).and(ticket.createdBy.eq(login)))
                    .fetchOne();
            if (tick == null) throw new NoAccessException("Нет доступа");

            new JPADeleteClause(session,ticket)
                    .where(ticket.id.eq(tick.getId()))
                    .execute();
            session.getTransaction().commit();
            return tick;
        } catch (Exception e){
            if (e instanceof NoAccessException) throw e;
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }

    }

    @Override
    public List<Long> removeGreaterKey(String login, Long id) {
        @Cleanup var session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                var deletedTickets=  new JPAQuery<Ticket>(session)
                        .select(ticket.id)
                        .from(ticket)
                        .where(ticket.id.gt(id).and(ticket.createdBy.eq(login)) )
                        .fetch();
                new JPADeleteClause(session,ticket)
                        .where(ticket.id.gt(id).and(ticket.createdBy.eq(login)) )
                        .execute();
                session.getTransaction().commit();
                return deletedTickets;
            } catch (Exception e){
                session.getTransaction().rollback();
                throw new FailedTransactionException("Транзакция не удалась");
            }
    }

    @Override
    public Optional<Ticket> replaceIfGreater(Ticket tick) {
        @Cleanup var session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            var oldTicket=  new JPAQuery<Ticket>(session)
                    .select(ticket)
                    .from(ticket)
                    .where(ticket.id.eq(tick.getId()) )
                    .fetchOne();
            if (oldTicket == null) throw new InvalidFormatException("Нет такого билета");
            if (!oldTicket.getCreatedBy().equals(tick.getCreatedBy())) throw new NoAccessException("Нет доступа");
            if (tick.getPrice()>oldTicket.getPrice()){
                session.merge(tick);
                var newTicketUpd = session.get(Ticket.class,oldTicket.getId());
                session.getTransaction().commit();
                return Optional.ofNullable(newTicketUpd);
            }else {
                session.getTransaction().commit();
                return Optional.empty();
            }

        } catch (Exception e){
            session.getTransaction().rollback();
            throw new FailedTransactionException("Транзакция не удалась");
        }
    }


}

