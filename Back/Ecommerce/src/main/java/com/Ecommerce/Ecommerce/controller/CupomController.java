package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.entity.Cupom;
import com.Ecommerce.Ecommerce.service.CupomService;
import com.Ecommerce.Ecommerce.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cupons")
public class CupomController {

    @Autowired
    private CupomService cupomService;

    @PostMapping
    public ResponseEntity<Cupom> criarCupom(@RequestBody Cupom cupom) {
        Cupom novoCupom = cupomService.criarCupom(cupom);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCupom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cupom> obterCupom(@PathVariable Long id) {
        return cupomService.obterCupomPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cupom> atualizarCupom(@PathVariable Long id, @RequestBody Cupom cupom) {
        Cupom cupomAtualizado = cupomService.atualizarCupom(id, cupom);
        return ResponseEntity.ok(cupomAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCupom(@PathVariable Long id) {
        cupomService.deletarCupom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarCupons() {
        return ResponseEntity.ok(cupomService.listarCupons());
    }

    @GetMapping("/validar/{codigo}")
    public ResponseEntity<?> validarCupom(@PathVariable String codigo) {
        return ResponseEntity.ok(cupomService.validarCupom(codigo));
    }

    @DeleteMapping
    public ResponseEntity<?> deletarTodosCupons() {
        cupomService.deletarTodosCupons();
        return ResponseEntity.noContent().build();
    }
}
//