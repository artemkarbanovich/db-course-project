package karbanovich.fit.bstu.foodie;
import karbanovich.fit.bstu.foodie.models.Account;

public class AccountSingleton {
    private static Account instance = null;

    private AccountSingleton() { }

    public static void setAccount(String email, String token) {
        if(instance == null) {
            instance = new Account(email, token);
        }
    }
    
    public static Account getAccount() { return instance; }
    public static void destroyAccount() { instance = null; }
}
