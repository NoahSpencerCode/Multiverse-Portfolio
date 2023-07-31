package com.NoahSpencerCode.KingScriptDocumentation.service;

import com.NoahSpencerCode.KingScriptDocumentation.model.Addition;

import java.util.List;

public interface AdditionService {
    public Addition saveAddition(Addition addition);
    public List<Addition> getAllAdditions();

    public Addition get(Integer id);
}
