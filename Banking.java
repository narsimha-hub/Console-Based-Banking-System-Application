import java.io.*;
import java.util.*;
public class Banking {
    static String UserMatch = "^[a-zA-Z0-9_]{8,15}$";
    static String PassMatch = "^[a-zA-Z0-9_!@#$%^&*]{8,15}$";
    static HashMap<String, String> Userdata = new HashMap<>();
    static String UserName;
    public static void registerUser(String UserName, String Password) {
        if (UserName.matches(UserMatch) && Password.matches(PassMatch)) {
            Userdata.put(UserName, Password);
            try {
                File file = new File("users.txt");
                Scanner sc = new Scanner(file);

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] details = line.split(":");
                    if (details.length == 2 && details[0].equals(UserName)) {
                        System.out.println("Username already exists");
                        sc.close();
                        return;
                    }
                }
                sc.close();
                FileWriter writer = new FileWriter("users.txt", true);
                writer.write(UserName + ":" + Password + ":0\n");
                writer.close();
                System.out.println("User registered successfully.");
            } catch (IOException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid username or password");
        }
    }
    public static void userLogin(String UserName, String Password) {
        try {
            File file = new File("users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(":");
                if (details.length == 3) {
                    String UserInFile = details[0];
                    String PassInFile = details[1];
                    if (UserInFile.equals(UserName) && PassInFile.equals(Password)) {
                        Banking.UserName=UserName;
                        System.out.println("Login successful");
                        sc.close();
                        return;
                    }
                }
            }
            sc.close();
            System.out.println("Login failed");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void deposit(double amount) throws IOException {
        File file = new File("users.txt");
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] details = line.split(":");
            if (details[0].equals(UserName)) {
                double balance = Double.parseDouble(details[2]);
                balance += amount;
                line = details[0] + ":" + details[1] + ":" + balance;
            }
            lines.add(line);
        }
        sc.close();

        FileWriter writer = new FileWriter("users.txt");
        for (String l : lines) {
            writer.write(l + "\n");
        }
        writer.close();
        System.out.println("Amount deposited successfully.");
    }

    public static void withdraw(double amount) throws IOException {
        File file = new File("users.txt");
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] details = line.split(":");
            if (details[0].equals(UserName)) {
                double balance = Double.parseDouble(details[2]);
                if(amount>balance){
                    System.out.println("Insufficient Balance");
                    return;
                }
                
                balance -= amount;
                line = details[0] + ":" + details[1] + ":" + balance;
            }
            lines.add(line);
        }
        sc.close();

        FileWriter writer = new FileWriter("users.txt");
        for (String l : lines) {
            writer.write(l + "\n");
        }
        writer.close();
        System.out.println("Amount Withdrawn successfully.");
        
    }
    public static void checkBalance(String UserName) throws FileNotFoundException{
        File file=new File("users.txt");
        Scanner sc=new Scanner(file);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] details=line.split(":");
            if(details[0].equals(UserName)){
                System.out.println("Remaining Balance is "+details[2]);
                sc.close();
                return;
            }
        }
        sc.close();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("users.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File creation failed");
        }
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Enter the choice:");
            System.out.println("a.Register\nb.Login\nc.press e to exit");
            String choice=sc.next();
            switch(choice){
                case "a":
                System.out.println("Create a UserName in between 8-15 characters");
                UserName=sc.next();
                System.out.println("Create a Password in between 8-15 characters ");
                String Password=sc.next();
                registerUser(UserName, Password);
                break;
                case "b":
                System.out.println("Enter UserName:");
                String EuserName=sc.next();
                System.out.println("Enter Password");
                String Epassword=sc.next();
                userLogin(EuserName, Epassword);
                System.out.println("You have logged in successfully You can do the following");
                while(true){
                    System.out.println("1.DepositMoney\n2.WithdrawMoney\n3.CheckBalance\n4.Exit");
                    int InnerChoice=sc.nextInt();
                    switch (InnerChoice) {
                        case 1:
                        System.out.println("Enter the amount to deposit");
                        double Damount=sc.nextDouble();
                        deposit(Damount);
                        break;
                        case 2:
                        System.out.println("Enter the amount to withdraw");
                        double wamount=sc.nextDouble();
                        withdraw(wamount);
                        break;
                        case 3:
                        checkBalance(UserName);
                        break;
                        case 4:
                        System.out.println("Logout successful");
                        return;
                    }
                }
                case "e":
                System.out.println("exitiing");
                return;
            }
        }
    }
}
