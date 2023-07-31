package com.NoahSpencerCode.KingScriptDocumentation.service;

import com.NoahSpencerCode.KingScriptDocumentation.model.Addition;
import com.NoahSpencerCode.KingScriptDocumentation.repository.AdditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionServiceImpl implements AdditionService {

    @Autowired
    private AdditionRepository additionRepository;

    @Override
    public Addition saveAddition(Addition addition) {

        return additionRepository.save(addition);
    }

    @Override
    public List<Addition> getAllAdditions() {
        return additionRepository.findAll();
    }

    public void delete(Integer id){
        additionRepository.deleteById(id);
    }

    public Addition get(Integer id){
        return additionRepository.findById(id).get();
    }
}


