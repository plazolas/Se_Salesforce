package gov.va.salesforceTests.classes;

import java.util.List;

public class Users {
    private List<User> users;

    public Users () {
        
    }
        
    public User getNextInActive() {
            return this.users.get(1);
    }
    
   
}
