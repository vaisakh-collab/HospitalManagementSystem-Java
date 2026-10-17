//temporary file
import java.util.Scanner;

public class CreateAdmin {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose administrator username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Choose administrator password: ");
        String password = scanner.nextLine();

        UserAccountDAO dao = new UserAccountDAO();

        boolean created = dao.createAccount(
            username,
            password,
            UserRole.ADMIN,
            null,
            null
        );

        System.out.println(
            created
                ? "Administrator account created."
                : "Account was not created."
        );

        scanner.close();
    }
}
