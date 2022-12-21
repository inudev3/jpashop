package jpabook.jpashop.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Builder
public class BookForm{
    private Long id;
    @NotEmpty(message = "상품 이름은 필수입니다.") private String name;

    public BookForm(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.author = author;
        this.isbn = isbn;
    }

    private int price;
    @PositiveOrZero(message = "수량은 양수여야 합니다.")
    private int stockQuantity;
    @NotEmpty(message = "저자명은 필수입니다.") private String author;
    private String isbn;
}