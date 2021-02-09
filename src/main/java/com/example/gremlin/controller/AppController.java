package com.example.gremlin.controller;

import com.example.gremlin.model.App;
import com.example.gremlin.service.IAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author sa
 * @date 5.02.2021
 * @time 11:34
 */
@RestController
@RequestMapping("api/app")
@RequiredArgsConstructor
public class AppController
{
    private final IAppService appService;

    @PostMapping
    public ResponseEntity<?> saveAppNetwork()
    {
        appService.saveAppNetwork();
        return ResponseEntity.ok(true);
    }

    @PostMapping("save")
    public ResponseEntity<?> saveApp(@RequestBody App app)
    {
        appService.saveApp(app);
        return ResponseEntity.ok(true);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getByTrackId(@PathVariable String id)
    {
        return ResponseEntity.ok(appService.findById(id));
    }

    @GetMapping("{trackId}")
    public ResponseEntity<?> getByTrackId(@PathVariable Long trackId)
    {
        return ResponseEntity.ok(appService.findByTrackId(trackId));
    }

    @PostMapping("calculate")
    public ResponseEntity<?> calculateVisibilityScores()
    {
        appService.calculateVisibilityScores();
        return ResponseEntity.ok(true);
    }

    @GetMapping("via-client")
    public ResponseEntity<?> getAllAppsViaClient()
    {
        return ResponseEntity.ok(appService.getAllAppsWithClient());
    }

    @GetMapping
    public ResponseEntity<?> getAllApps()
    {
        return ResponseEntity.ok(appService.getAllApps());
    }

    @GetMapping("edge")
    public ResponseEntity<?> getAllEdges()
    {
        return ResponseEntity.ok(appService.getAllEdges());
    }

    @DeleteMapping
    public ResponseEntity<?> dropNetwork()
    {
        appService.dropGraph();
        return ResponseEntity.ok(true);
    }
}
