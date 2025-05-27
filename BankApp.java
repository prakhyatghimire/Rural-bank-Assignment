import java.util.*;


interface Transaction {
    void deposit(double amount);
    void withdraw(double amount) throws InsufficientBalanceException;
}


class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}


abstract class BankAccount implements Transaction {
    private String accountNumber;
    private double balance;
    private String accountHolderName;

    public BankAccount(String holderName, String accNumber, double balance) {
        this.accountHolderName = holderName;
        this.accountNumber = accNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public abstract void withdraw(double amount) throws InsufficientBalanceException;

    public abstract void displayAccountInfo();
}


class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String holderName, String accNumber, double balance, double interestRate) {
        super(holderName, accNumber, balance);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = getBalance() * interestRate;
        setBalance(getBalance() + interest);
        System.out.println("Interest added: " + interest);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= getBalance()) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawn: " + amount);
        } else {
            throw new InsufficientBalanceException("Insufficient balance in savings account.");
        }
    }

    public void displayAccountInfo() {
        System.out.println("Savings Account - " + getAccountNumber());
        System.out.println("Holder: " + getAccountHolderName());
        System.out.println("Balance: " + getBalance());
    }
}


class CurrentAccount extends BankAccount {
    private double overdraftLimit;

    public CurrentAccount(String holderName, String accNumber, double balance, double overdraftLimit) {
        super(holderName, accNumber, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= getBalance() + overdraftLimit) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawn: " + amount);
        } else {
            throw new InsufficientBalanceException("Overdraft limit exceeded in current account.");
        }
    }

    public void displayAccountInfo() {
        System.out.println("Current Account - " + getAccountNumber());
        System.out.println("Holder: " + getAccountHolderName());
        System.out.println("Balance: " + getBalance());
    }
}


class Customer {
    private String name;
    private List<BankAccount> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }
}
public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Rural Bank of Nepal");
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        Customer customer = new Customer(name);

  
        customer.addAccount(new SavingsAccount("Ashmin", "SAV001", 5000, 0.04));
        customer.addAccount(new SavingsAccount("Prakhyat","SAV002",7000,0.12));
        customer.addAccount(new CurrentAccount("Raju", "CUR001", 2000, 1000));

        while (true) {
            System.out.println("\nChoose operation:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Add Interest");
            System.out.println("4. View Accounts");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice == 5) {
                System.out.println("Thank you for using the bank!");
                break;
            }

            
            List<BankAccount> accounts = customer.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". " + accounts.get(i).getAccountNumber());
            }

            System.out.print("Select account number: ");
            int accChoice = sc.nextInt();
            if (accChoice < 1 || accChoice > accounts.size()) {
                System.out.println("Invalid account choice.");
                continue;
            }

            BankAccount selectedAccount = accounts.get(accChoice - 1);

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = sc.nextDouble();
                        selectedAccount.deposit(depositAmount);
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = sc.nextDouble();
                        selectedAccount.withdraw(withdrawAmount);
                        break;
                    case 3:
                        if (selectedAccount instanceof SavingsAccount) {
                            ((SavingsAccount) selectedAccount).addInterest();
                        } else {
                            System.out.println("Interest can only be added to savings account.");
                        }
                        break;
                    case 4:
                        selectedAccount.displayAccountInfo();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InsufficientBalanceException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}

