package com.example.ProjectBlog.Util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomMapper {

    @Autowired
    private ModelMapper modelMapper;

    //converts a List<T> into List<D> where T and D is an Object
    public <T, D> List<D> convertList(List<T> sourceList, Class<D> targetClass) {
        List<D> targetList = new ArrayList<>();
        for (T element : sourceList) {
            D targetElement = modelMapper.map(element, targetClass);
            targetList.add(targetElement);
        }
        return targetList;
    }

    public <T, D> D map(T source, Class<D> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}
