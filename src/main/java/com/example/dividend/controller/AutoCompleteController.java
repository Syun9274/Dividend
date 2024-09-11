package com.example.dividend.controller;

import com.example.dividend.service.AutoCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutoCompleteController {

    private final AutoCompleteService autoCompleteService;

    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocompleteCompany(@RequestParam String keyword) {
        var result = autoCompleteService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }
}
