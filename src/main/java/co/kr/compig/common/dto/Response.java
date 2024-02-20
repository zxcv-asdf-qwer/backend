package co.kr.compig.common.dto;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private T data;
}