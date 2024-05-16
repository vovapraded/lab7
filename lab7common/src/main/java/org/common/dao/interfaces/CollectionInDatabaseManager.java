package org.common.dao.interfaces;

import org.common.dto.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface CollectionInDatabaseManager {
    List<Long> removeGreater(String login, Long price);

    HashMap<Long, Ticket> loadCollection();



    List<Long> clear( String login);

    Optional<Ticket> insert(Ticket ticket);



    Optional<Ticket> update(Ticket newTicket, String login);



    Ticket removeTicket(Long id, String login);

    List<Long> removeGreaterKey(String login, Long id);

    Optional<Ticket> replaceIfGreater(Ticket ticket);
}
