package edu.ria.springbatchmultipledatabases.batch;

import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import edu.ria.springbatchmultipledatabases.destination.model.TargetUsers;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DbProcessor implements ItemProcessor<SourceUsers, TargetUsers> {

    private static final Map<String, String> DEPT_NAMES = new HashMap<>();

    public DbProcessor() {
        DEPT_NAMES.put("Technology", "001");
        DEPT_NAMES.put("Operations", "002");
        DEPT_NAMES.put("Accounts", "003");
    }

    @Override
    public TargetUsers process(SourceUsers user) throws Exception {
        TargetUsers targetUsers = new TargetUsers();
        String deptCode = user.getDept();
        String dept = DEPT_NAMES.get(deptCode);
        targetUsers.setDept(dept);
        System.out.println(String.format("Converted from [%s] to [%s] ", deptCode, dept));
        targetUsers.setId(user.getId());
        targetUsers.setName(user.getName());
        targetUsers.setSalary(user.getSalary());
        return targetUsers;
    }
}
