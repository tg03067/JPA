package com.green.greengram.exception;

import com.green.greengram.common.model.MyResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@SuperBuilder // 부모값까지 빌더패턴으로 작성가능. 상속관계 모두 SuperBuilder 써야함.
public class MyErrorResponse extends MyResponse<String> {
    private final List<ValidationError> valids ;

    // ValidationError 발생시 해당하는 에러메시지를 표시할 때 사용하는 객체
    // inner class 는 static 을 붙여주면 성능이 좋아진다.
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field ; // validation 에러가 발생된 멤버필드 명
        private final String message ; // validation 에러 메시지ㅁ

        // ValidationError 객체 생성을 담당하는 메소드
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField()) // 멤퍼필드 명 ex ) uid
                    .message(fieldError.getDefaultMessage()) // 메시지명 ex )  "아이디를 확인해 주세요."
                    .build() ;
        }
    }
}
