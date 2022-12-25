package jpabook.jpashop.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberForm{
    @NotEmpty(message = "회원 이름은 필수입니다.") private String name;
    private String city;
    private String street;
    private String zipcode;
}