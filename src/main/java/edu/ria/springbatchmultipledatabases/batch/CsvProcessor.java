package edu.ria.springbatchmultipledatabases.batch;

import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CsvProcessor implements ItemProcessor<SourceUsers, SourceUsers> {

    private static final Map<String, String> DEPT_NAMES = new HashMap<>();

    public CsvProcessor() {
        DEPT_NAMES.put("001", "Technology");
        DEPT_NAMES.put("002", "Operations");
        DEPT_NAMES.put("003", "Accounts");
    }

    @Override
    public SourceUsers process(SourceUsers user) throws Exception {
        String deptCode = user.getDept();
        String dept = DEPT_NAMES.get(deptCode);
        user.setDept(dept);
        System.out.println(String.format("Converted from [%s] to [%s] ", deptCode, dept));
        return user;
    }
}
