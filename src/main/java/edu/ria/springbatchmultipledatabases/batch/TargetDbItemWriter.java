package edu.ria.springbatchmultipledatabases.batch;

import edu.ria.springbatchmultipledatabases.target.model.TargetUsers;
import edu.ria.springbatchmultipledatabases.target.repository.TargetUsersRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargetDbItemWriter implements ItemWriter<TargetUsers> {
    @Autowired
    private TargetUsersRepository targetUsersRepository;

    @Override
    public void write(List<? extends TargetUsers> users) throws Exception {
        System.out.println("Data saved for users: " + users);
        targetUsersRepository.saveAll(users);
    }
}
