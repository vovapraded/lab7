package org.common.managers;

import com.querydsl.core.annotations.Immutable;
import lombok.Getter;
import lombok.Setter;

import org.common.commands.authorization.NoAccessException;
import org.common.dao.interfaces.CollectionInDatabaseManager;
import org.common.dto.Ticket;
import org.common.dto.Venue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * The class that manages the collection
 */

    public  class Collection  {

    public static Collection getInstance() {
        return INSTANCE;
    }

    private static final Collection INSTANCE= new Collection();
    private final ReentrantLock crudLock = new ReentrantLock();
//    private final ReentrantLock readLock = new ReentrantLock();
    private Date currentDate;
    private ConcurrentHashMap<Long,Ticket> hashMap = new ConcurrentHashMap<>();
    @Setter @Getter
    private   CollectionInDatabaseManager ticketDao;



    private Collection(){
        currentDate = new Date();
    }
    public void clearCollection(String login){
        crudLock.lock();
        try {
            var deletedId = ticketDao.clear(login);
            hashMap.keySet().removeAll(deletedId);
        } finally {
            crudLock.unlock();
        }

    }
    public void insertElement(Ticket ticket){
        crudLock.lock();
        try {
            var ticketUpd=ticketDao.insert(ticket);
            if (ticketUpd.isPresent())
            hashMap.put(ticketUpd.get().getId(),ticketUpd.get());
        } finally {
            crudLock.unlock();
        }
    }
    public void updateTicket(Ticket ticket,String login){
        crudLock.lock();
        try {
            var ticketOptional = ticketDao.update(ticket,login);
            if (ticketOptional.isPresent())
                hashMap.put(ticketOptional.get().getId(),ticketOptional.get());
        }        finally {
            crudLock.unlock();
        }

    }
    public ArrayList<Venue> getAllVenue(){
        return (ArrayList<Venue>) hashMap.values().stream().map(Ticket::getVenue).collect(Collectors.toList());
    }
    public void addHashMap(HashMap<Long,Ticket> anotherHashMap){

        hashMap.putAll(anotherHashMap);
    }


    public boolean replaceIfGreater(Ticket ticket) {
       crudLock.lock();
       Optional<Ticket> result = null;
       try {
            result = ticketDao.replaceIfGreater(ticket);
           if (result.isPresent()){
               hashMap.put(result.get().getId(),result.get());
           }
       }finally {
           crudLock.unlock();
           return result.isPresent();
       }




    }

    public  void removeElement(Long id,String login) throws NoAccessException{
        crudLock.lock();
        try {
            ticketDao.removeTicket(id,login);
            hashMap.keySet().remove(id);
        } finally {
            crudLock.unlock();
        }
    }
    public void removeGreater(Ticket ourTicket,String login) {
        crudLock.lock();
        try {
            var deletedId=ticketDao.removeGreater(login,ourTicket.getPrice());
            hashMap.keySet().removeAll(deletedId);
        } finally {
            crudLock.unlock();
        }

    }

    public void removeGreaterKey(Long id,String login) {
        crudLock.lock();
        try {
            List<Long> deletedId=ticketDao.removeGreaterKey(login,id);
            hashMap.keySet().removeAll(deletedId);
        } finally {
            crudLock.unlock();
        }
//        ticketsToRemove.forEach(ticket -> removeElement(ticket));
    }

    public OptionalDouble getAveragePrice(){
            return hashMap.values().stream()
                    .mapToLong(Ticket::getPrice)
                    .average();

    }
    public List<Ticket> filterLessThanVenue(Long capacity){
        List<Ticket>  filtered = hashMap.values().stream()
                .filter((ticket) -> ( ticket.getVenue().getCapacity() == null || ticket.getVenue().getCapacity() < capacity))
                .collect(Collectors.toList());
        return filtered;
    }

    public ConcurrentHashMap<Long, Ticket>  getHashMap() {
        return hashMap;
    }


    public Date getCurrentDate() {
        return currentDate;
    }
    public int getCountOfElements(){
        return hashMap.size();
    }
}

