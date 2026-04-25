import java.util.ArrayList;
import java.util.Scanner;

// ================= ABSTRACT CLASS =================
abstract class BankAccount {

    private int accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Amount Deposited Successfully!");
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public abstract void withdraw(double amount);

    public void displayDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: ₹" + balance);
        System.out.println("----------------------------");
    }
}

// ================= SAVINGS ACCOUNT =================
class SavingsAccount extends BankAccount {

    private static final double MIN_BALANCE = 1000;

    public SavingsAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    @Override
    public void withdraw(double amount) {

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount!");
            return;
        }

        if (getBalance() - amount >= MIN_BALANCE) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawal Successful!");
        } else {
            System.out.println("Cannot withdraw! Minimum balance ₹1000 must be maintained.");
        }
    }
}

// ================= CURRENT ACCOUNT =================
class CurrentAccount extends BankAccount {

    private static final double OVERDRAFT_LIMIT = 5000;

    public CurrentAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    @Override
    public void withdraw(double amount) {

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount!");
            return;
        }

        if (getBalance() - amount >= -OVERDRAFT_LIMIT) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawal Successful!");
        } else {
            System.out.println("Overdraft limit exceeded! Maximum overdraft ₹5000.");
        }
    }
}

// ================= SERVICE CLASS =================
class BankService {

    private ArrayList<BankAccount> accounts = new ArrayList<>();
    private int accountCounter = 1001;

    // Create Account
    public void createAccount(String name, double balance, int type) {

        BankAccount account;

        if (type == 1) {
            account = new SavingsAccount(accountCounter++, name, balance);
        } else {
            account = new CurrentAccount(accountCounter++, name, balance);
        }

        accounts.add(account);
        System.out.println("Account Created Successfully!");
        System.out.println("Your Account Number is: " + account.getAccountNumber());
    }

    // Find Account
    private BankAccount findAccount(int accNo) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber() == accNo) {
                return acc;
            }
        }
        return null;
    }

    // Deposit
    public void depositMoney(int accNo, double amount) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            acc.deposit(amount);
        } else {
            System.out.println("Account not found!");
        }
    }

    // Withdraw
    public void withdrawMoney(int accNo, double amount) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            acc.withdraw(amount);
        } else {
            System.out.println("Account not found!");
        }
    }

    // Transfer
    public void transferMoney(int fromAcc, int toAcc, double amount) {

        BankAccount sender = findAccount(fromAcc);
        BankAccount receiver = findAccount(toAcc);

        if (sender == null || receiver == null) {
            System.out.println("Invalid account number!");
            return;
        }

        if (amount <= 0) {
            System.out.println("Invalid transfer amount!");
            return;
        }

        if (sender.getBalance() >= amount) {
            sender.withdraw(amount);
            receiver.deposit(amount);
            System.out.println("Transfer Successful!");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    // Display All Accounts
    public void displayAllAccounts() {

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        for (BankAccount acc : accounts) {
            acc.displayDetails();
        }
    }
}

// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService service = new BankService();
        int choice;

        while (true) {

            System.out.println("\n===== Welcome to Web Plus Bank =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View All Accounts");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Account Holder Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Initial Balance: ");
                    double balance = sc.nextDouble();

                    System.out.println("Select Account Type:");
                    System.out.println("1. Savings");
                    System.out.println("2. Current");
                    int type = sc.nextInt();

                    service.createAccount(name, balance, type);
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    int depAcc = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double depAmt = sc.nextDouble();
                    service.depositMoney(depAcc, depAmt);
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    int witAcc = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double witAmt = sc.nextDouble();
                    service.withdrawMoney(witAcc, witAmt);
                    break;

                case 4:
                    System.out.print("Enter From Account Number: ");
                    int fromAcc = sc.nextInt();
                    System.out.print("Enter To Account Number: ");
                    int toAcc = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double transAmt = sc.nextDouble();
                    service.transferMoney(fromAcc, toAcc, transAmt);
                    break;

                case 5:
                    service.displayAllAccounts();
                    break;

                case 6:
                    System.out.println("Thank you for banking with us!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}