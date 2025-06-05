package org.example.com.prompt.user;

import org.example.com.model.User;
import org.example.com.util.LoanManager;

import java.util.Scanner;

public class ExtendPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;
    private final LoanManager loanManager = new LoanManager();

    public ExtendPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        while(true) {
            if(currentUser.getTotalPenalty() > 0) {
                System.out.println("!!패널티 상태입니다. 도서를 연장할 수 없습니다.");
                return;
            }

            System.out.println("연장할 책의 ISBN을 입력해주세요::");
            String isbn = scanner.nextLine();

            //TODO: 입력한 ISBN이 사용자가 대출 중인 도서에 존재하는지 확인
            if (true) {
                System.out.println("!! 현재 대출 중인 도서가 아닙니다.");
            }

            //TODO: 이미 연장된 도서를 다시 연장하려고 할 경우
            if(true) {
                System.out.println("!! 해당 도서는 이미 연장되었습니다.");
            }

            //TODO: 올바른 ISBN이 입력되고 연장 가능할 경우, 해당 도서의 반납예정일에 +7일 연장되고 연장 여부가 'Y'로 변경됩니다.
            System.out.println();
        }
    }
}
