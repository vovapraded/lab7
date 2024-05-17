alter table s409397.ticket
    alter column creation_date type timestamp using creation_date::timestamp;

