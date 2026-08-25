public class Person{
    private String name;
    private int age;
    private String phone;
    private String address;
    private String email;

    //constructor

    public Person(String name, int age, String phone, String address, String email){
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    //getters

    public String getName(){
        return this.name;
    }
    
    public int getAge(){
        return this.age;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getAddress(){
        return this.address;
    }

    public String getEmail(){
        return this.email;
    }

    //setters

    public String setName(String name){
        this.name = name;
    }
    
    public int setAge(int age){
        this.age = age;
    }

    public String setPhone(String phone){
        this.phone = phone;
    }

    public String setAddress(String address){
        this.address = address;
    }

    public String setEmail(String email){
        this.email = email;
    }
}