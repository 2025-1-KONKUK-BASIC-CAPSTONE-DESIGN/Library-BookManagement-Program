package org.example.com.prompt.start;

import org.example.com.model.User;
import org.example.com.util.FileManager;
import org.example.com.util.Validator;

import java.util.Scanner;

public class SignUpPrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n🔐 회원가입을 시작합니다.");

        String id;
        while (true) {
            System.out.print("ID를 입력하세요: ");
            id = scanner.nextLine();

            if (!Validator.isValidId(id)) {
                System.out.println("❌ ID가 6자리 이상, 16 이하이며, 영문+숫자로만 구성되어야 합니다. 특수문자나 공백은 안됩니다.");
                continue;
            }

            if (FileManager.isIdExists(id)) {
                System.out.println("❌ 이미 존재하는 ID입니다. 다른 ID를 입력해주세요.");
                continue;
            }

            break;
        }

        String password;
        while (true) {
            System.out.print("비밀번호를 입력하세요: ");
            password = scanner.nextLine();

            if (!Validator.isValidPassword(password)) {
                System.out.println("❌ 비밀번호는 8자리 이상 16이하이며, 대소문자/숫자/특수문자를 포함해야 합니다.");
                continue;
            }
            break;
        }

        String phone;
        while (true) {
            System.out.print("전화번호를 입력하세요 (010 xxxx xxxx): ");
            phone = scanner.nextLine().trim();

            if (!Validator.isValidPhone(phone)) {
                System.out.println("❌ 전화번호 형식은 010 xxxx xxxx 입니다.");
                continue;
            }
            break;
        }

        String email;
        while (true) {
            System.out.print("메일주소를 입력하세요 (@gmail.com): ");
            email = scanner.nextLine().trim();

            if (!Validator.isValidEmail(email)) {
                System.out.println("❌ 이메일은 @gmail.com 도메인만 허용됩니다. 형식도 확인해주세요.");
                continue;
            }

            if (FileManager.isEmailExists(email)) {
                System.out.println("❌ 이미 등록된 이메일입니다. 다른 이메일을 입력해주세요.");
                continue;
            }

            break;
        }

        String birth;
        while (true) {
            System.out.print("생년월일을 입력하세요 (YYYY-MM-DD): ");
            birth = scanner.nextLine().trim();

            // date 확인
            String dateError = Validator.validateBirth(birth);
            if (!dateError.isEmpty()) {
                System.out.println(dateError);
                continue;
            }
            break;
        }

        User newUser = new User(id, password, phone, email, birth);
        FileManager.saveUser(newUser);

        System.out.println("✅ 회원가입이 완료되었습니다! 로그인 화면으로 이동합니다.");
    }
}