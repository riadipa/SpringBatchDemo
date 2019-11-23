package edu.ria.springbatchmultipledatabases.batch;

import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import edu.ria.springbatchmultipledatabases.source.repository.SourceUsersRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbItemWriter implements ItemWriter<SourceUsers> {
    @Autowired
    private SourceUsersRepository sourceUsersRepository;

    @Override
    public void write(List<? extends SourceUsers> users) throws Exception {
        System.out.println("Data saved for users: " + users);
        sourceUsersRepository.saveAll(users);
    }
}
