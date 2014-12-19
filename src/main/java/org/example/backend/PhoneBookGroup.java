
package org.example.backend;

/**
 *
 * @author Matti Tahvonen
 */
public class PhoneBookGroup {
    
    static int counter = 0;
    
    private String name;
    private int iidee = counter++;

    public PhoneBookGroup(String name) {
        this.name = name;
    }

    public PhoneBookGroup() {
    }

    public int getIidee() {
        return iidee;
    }

    public void setIidee(int iidee) {
        this.iidee = iidee;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" + "name=" + name + '}';
    }

}
