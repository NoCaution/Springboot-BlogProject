package com.example.ProjectBlog.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private HttpStatusCode statusCode;
    private String message;
    private Object result;

    public APIResponse(HttpStatusCode statusCode,String message){
        this.statusCode =statusCode;
        this.message =message;
    }
}
