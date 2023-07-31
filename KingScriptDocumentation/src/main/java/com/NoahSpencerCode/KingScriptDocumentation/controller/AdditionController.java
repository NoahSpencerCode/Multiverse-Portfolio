package com.NoahSpencerCode.KingScriptDocumentation.controller;

import  com.NoahSpencerCode.KingScriptDocumentation.model.Addition;
import com.NoahSpencerCode.KingScriptDocumentation.service.AdditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/addition")
@CrossOrigin
public class AdditionController {
    @Autowired
    private AdditionService additionService;

    @PostMapping("/add")
    public String add(@RequestBody Addition addition){
        additionService.saveAddition(addition);
        return "New addition is added";
    }

    @GetMapping("/getAll")
    public List<Addition> getAllAdditions(){
        return additionService.getAllAdditions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Addition> get(@PathVariable Integer id){
        try{
            Addition addition = additionService.get(id);
            return new ResponseEntity<Addition>(addition,HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Addition> update(@RequestBody Addition addition, @PathVariable Integer id){
        try{
            Addition existingAddition = additionService.get(id);
            existingAddition.setTitle(addition.getTitle());
            existingAddition.setBody(addition.getBody());
            additionService.saveAddition(existingAddition);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<Addition>(HttpStatus.NOT_FOUND);
        }
    }

}
