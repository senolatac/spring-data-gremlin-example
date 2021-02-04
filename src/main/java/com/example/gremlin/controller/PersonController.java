package com.example.gremlin.controller;

import com.example.gremlin.service.IPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:24
 */
@RestController
@RequestMapping("api/person")
@RequiredArgsConstructor
public class PersonController
{
    private final IPersonService personService;

    @DeleteMapping
    public ResponseEntity<?> deleteAll()
    {
        personService.deleteAll();
        return ResponseEntity.ok(true);
    }

    @PostMapping
    public ResponseEntity<?> saveTest()
    {
        personService.saveTestNetwork();
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<?> getAllPeople()
    {
        return ResponseEntity.ok(personService.findAllPersons());
    }

    @GetMapping("native")
    public ResponseEntity<?> nativeExample()
    {
        return ResponseEntity.ok(personService.nativeQueryTest());
    }

    @GetMapping("find/{name}")
    public ResponseEntity<?> getPersonByName(@PathVariable String name)
    {
        return ResponseEntity.ok(personService.findPersonByName(name));
    }

    @GetMapping("group-by-age")
    public ResponseEntity<?> groupByAge()
    {
        return ResponseEntity.ok(personService.groupByAge());
    }
}
