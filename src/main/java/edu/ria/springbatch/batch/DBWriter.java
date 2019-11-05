package edu.ria.springbatch.batch;

import edu.ria.springbatch.model.Users;
import edu.ria.springbatch.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Users> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends Users> users) throws Exception {
        System.out.println("Data saved for users: " + users);
        userRepository.saveAll(users);
    }
}
