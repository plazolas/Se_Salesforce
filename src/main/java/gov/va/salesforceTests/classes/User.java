package gov.va.salesforceTests.classes;

public class User {
    private String name;
    private String username;
    private String password;
    private Boolean active;

    public User (String name, String username, String password, Boolean active) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = active;
    }
        
    public String getName() {
            return this.name;
    }
    
    public String getUsername() {
            return this.username;
    }
    
    public String getPassword() {
            return this.password;
    }
    
    public Boolean isActive() {
            return this.active;
    }
}
