import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;
public class ExpenseTracker {
    public static void main(String[] args){
     ExpenseManager ep = new ExpenseManager();
     Scanner sc = new Scanner(System.in);
     ep.LoadFromFile();
     while(true){
         System.out.println("1.Add Expense");
         System.out.println("2.Update Expense");
         System.out.println("3.Delete Expense");
         System.out.println("4.View all Expense");
         System.out.println("5.Calculate all Expense");
         System.out.println("6.Calculate Expense for specific month");
         System.out.println("7.Exit");
         System.out.print("Choice 1-7: ");
         int choice = sc.nextInt();
         sc.nextLine();
         switch(choice){
             case 1:
                 ep.addExpense();
                 break;
             case 2:
                 ep.updateExpense();
                 break;
             case 3:
                 ep.deleteExpense();
                 break;
             case 4:
                 ep.viewExpense();
                 break;
             case 5:
                 ep.calculateExpense();
                 break;
             case 6:
                 ep.calculateExpenseSpecificMonth();
                 break;
             case 7:
                 System.exit(0);
             default:
                 System.out.println("Invalid Number");
         }
      }
    }
}
class Expense{
    int id;
    String localDate;
    private String description;
    private double amount;
    Expense(int id,String localDate, String description, double amount){
        this.id = id;
        this.localDate = localDate;
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }
    public String getLocalDate(){
        return localDate;
    }
    public String getDescription(){
        return description;
    }
    public double getAmount(){
        return amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
}
class ExpenseManager{
    Scanner sc = new Scanner(System.in);
    ArrayList<Expense> ep = new ArrayList<>();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    int nextId = 1;
    void addExpense(){
        String date = LocalDate.now().toString();
        System.out.print("Description: ");
        String description = sc.nextLine();
        System.out.print("Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        ep.add(new Expense(nextId,date,description,amount));
        nextId++;
        savetoFile();
        System.out.println("Expense Added!");
    }

    void updateExpense(){
        System.out.print("Update Expense by ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        for(Expense e : ep){
            if(e.getId() == id){
                System.out.print("New Description: ");
                String description = sc.nextLine();
                System.out.print("Amount: ");
                double amount = sc.nextDouble();
                sc.nextLine();
                e.setDescription(description);
                e.setAmount(amount);
                System.out.println("Update Success!");
                savetoFile();
                return;
            }
        }
        System.out.println("Invalid ID");
    }
    void deleteExpense(){
        Expense found = null;
        System.out.print("Enter ID to delete Expense: ");
        int id = sc.nextInt();
        sc.nextLine();
        for(Expense e : ep){
            if(e.getId()== id){
                found = e;
                break;

            }
        }
        if (found == null){
            System.out.println("Invalid ID");
            return;
        }
        ep.remove(found);
        savetoFile();
        System.out.println("Delete Success!!");
    }
    void viewExpense(){
      if(ep.isEmpty()){
          System.out.println("No expense yet");
          return;
      }
      for(Expense e : ep){
          System.out.println("Id: " + e.getId() + "| Date: " + e.getLocalDate() + "| Description : " + e.getDescription()+"| Amount: " + e.getAmount());
      }
    }
    void calculateExpense(){
        double amount = 0;
        for(Expense e: ep){
            amount += e.getAmount();
        }
        System.out.println("Expense Calculated!");
        System.out.println("All expense : " + amount + "$");
    }
    void calculateExpenseSpecificMonth(){
        double total = 0;
        System.out.print("Enter Month from 1-12 to Calculate: ");
        int choice = sc.nextInt();
        sc.nextLine();
        if(choice < 1 || choice > 12){
            System.out.println("Bro lol invalid month");
            return;
        }
        for(Expense e: ep){
            LocalDate date = LocalDate.parse(e.getLocalDate());
            if(choice == date.getMonthValue()){
                total += e.getAmount();
            }
        }
        System.out.println("Expense of month " + choice + " is " + total + "$");
    }
    void savetoFile(){
       try(FileWriter writer = new FileWriter("Expense.json")){

           gson.toJson(ep,writer);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    void LoadFromFile(){
        try {
            File file = new File("Expense.json");
            if(!file.exists()){
                return;
            }
            FileReader fileReader = new FileReader(file);
            Type ListType = new TypeToken<ArrayList<Expense>>(){}.getType();
            ep = gson.fromJson(fileReader,ListType);
            if(ep == null){
                ep = new ArrayList<>();
            }
            int max = 0;
            for(Expense e: ep){
                if(e.getId() > max){
                    max = e.getId();
                }
            }
            nextId = max + 1;
            fileReader.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}

