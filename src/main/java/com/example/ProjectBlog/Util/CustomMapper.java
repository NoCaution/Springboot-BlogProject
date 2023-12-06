package com.example.ProjectBlog.Util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CustomMapper extends  ModelMapper{

    //converts a List<T> into List<D> where T and D is an Object
    public <T, D> List<D> convertList(List<T> sourceList, Class<D> targetClass) {
        return sourceList
                .stream()
                .map(sourceElement-> map(sourceElement,targetClass))
                .toList();
    }
}
