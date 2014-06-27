package org.example.backend;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A domain object example. In a real application this would probably be a JPA
 * entity or DTO.
 */
public class PhoneBookEntry implements Serializable, Cloneable {

    @NotNull
    @Size(min=3,max = 40)
    private String name;

    @Size(max = 25)
    private String number;
    
    @NotNull
    @Pattern(regexp = ".+@.+\\.[a-z]+")
    private String email;

    public PhoneBookEntry(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public PhoneBookEntry() {
        this("", "", "");
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
